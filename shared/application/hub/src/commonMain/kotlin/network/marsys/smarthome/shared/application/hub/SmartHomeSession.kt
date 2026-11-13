package network.marsys.smarthome.shared.application.hub

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import network.marsys.smarthome.shared.application.hub.ports.outbound.SmartHomeEventSource
import network.marsys.smarthome.shared.application.hub.ports.outbound.SmartHomeEventSourceException
import network.marsys.smarthome.shared.domain.connection.ConnectionState
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.Result.Companion.fail
import network.marsys.smarthome.shared.library.core.Result.Companion.succeed
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.model.ConnectionConfiguration
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.seconds

internal class SmartHomeSession(
    private val source: SmartHomeEventSource,
    applicationConfigurationRepository: ApplicationConfigurationRepository,
    private val scope: CoroutineScope,
) {
    private val mutex = Mutex()
    private var job: Job? = null

    val state: StateFlow<ConnectionState>
        field = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)

    init {
        scope.launch {
            applicationConfigurationRepository.connection
                .filter { it !is ConnectionConfiguration.Backend }
                .collect { disconnect() }
        }
    }

    suspend fun connect(): Result<Unit, Reason> =
        mutex.withLock {
            stop()

            val connected = CompletableDeferred<Result<Unit, Reason>>()

            job = scope.launch {
                run(connected = connected)
            }

            try {
                withTimeoutOrNull(TIMEOUT) {
                    connected.await()
                } ?: run {
                    stop()
                    fail(with = Reason.Timeout)
                }
            } catch (exception: CancellationException) {
                withContext(NonCancellable) {
                    stop()
                }

                throw exception
            }
        }

    suspend fun disconnect() =
        mutex.withLock {
            stop()
        }

    private suspend fun run(
        connected: CompletableDeferred<Result<Unit, Reason>>,
    ) {
        state.update { ConnectionState.Connecting }

        val reason = try {
            source.events()
                .buffer(Channel.UNLIMITED)
                .collect { event ->
                    state.update { ConnectionState.Connected.Idle }
                    connected.complete(succeed(with = Unit))

                    when (event) {
                        is SmartHomeEvent.SyncRequired -> {
                            // No-op for now.
                        }

                        is SmartHomeEvent.Platform -> {
                            // No-op for now.
                        }
                    }
                }

            Reason.Unreachable
        } catch (exception: SmartHomeEventSourceException) {
            when (exception) {
                is SmartHomeEventSourceException.Unreachable ->
                    Reason.Unreachable

                is SmartHomeEventSourceException.Unauthorized ->
                    Reason.Unauthorized

                is SmartHomeEventSourceException.Unknown ->
                    Reason.Unknown
            }
        } catch (_: Exception) {
            currentCoroutineContext().ensureActive()
            Reason.Unknown
        }

        val completed = connected.complete(fail(with = reason))

        state.update {
            if (completed) {
                ConnectionState.Disconnected
            } else {
                ConnectionState.Lost
            }
        }
    }

    private suspend fun stop() {
        job?.cancelAndJoin()
        job = null

        state.update { ConnectionState.Disconnected }
    }

    sealed interface Reason {
        data object Unreachable : Reason
        data object Unauthorized : Reason
        data object Timeout : Reason
        data object Unknown : Reason
    }

    companion object {
        private val TIMEOUT = 10.seconds
    }
}
