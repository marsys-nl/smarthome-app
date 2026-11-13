package network.marsys.smarthome.shared.application.hub

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import network.marsys.smarthome.shared.application.hub.ports.outbound.SmartHomeEventSource
import network.marsys.smarthome.shared.application.hub.ports.outbound.SmartHomeEventSourceException
import network.marsys.smarthome.shared.domain.connection.ConnectionState
import network.marsys.smarthome.shared.domain.connection.SynchronizeEntitiesUseCase
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.Result.Companion.fail
import network.marsys.smarthome.shared.library.core.Result.Companion.succeed
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.model.ConnectionConfiguration
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.seconds

internal class SmartHomeSession(
    private val source: SmartHomeEventSource,
    private val handlers: Collection<SmartHomeEventHandler>,
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

            val connectionCompleted = CompletableDeferred<Result<Unit, Reason>>()
            val synchronizationCompleted = CompletableDeferred<Unit>()

            job = scope.launch {
                run(
                    connectionCompleted = connectionCompleted,
                    synchronizationCompleted = synchronizationCompleted,
                )
            }

            try {
                var connection: Result<Unit, Reason>? = null

                withTimeoutOrNull(TIMEOUT) {
                    connection = connectionCompleted.await()
                    if (connection is Result.Success) {
                        synchronizationCompleted.await()
                    }
                }

                connection ?: run {
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

    private suspend fun handle(
        event: SmartHomeEvent,
        synchronizationCompleted: CompletableDeferred<Unit>,
    ) {
        if (event is SmartHomeEvent.SyncRequired) {
            state.update { ConnectionState.Connected.Synchronizing }
        }

        try {
            handlers.forEach {
                it.handle(event = event)
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: Exception) {
            // No-op. Exceptions should be handled in the handler.
        }

        if (event is SmartHomeEvent.SyncRequired) {
            state.update { ConnectionState.Connected.Idle }
            synchronizationCompleted.complete(value = Unit)
        }
    }

    private suspend fun run(
        connectionCompleted: CompletableDeferred<Result<Unit, Reason>>,
        synchronizationCompleted: CompletableDeferred<Unit>,
    ) {
        state.update { ConnectionState.Connecting }

        val reason = try {
            source.events()
                .buffer(Channel.UNLIMITED)
                .collect { event ->
                    state.update { ConnectionState.Connected.Idle }
                    connectionCompleted.complete(value = succeed(with = Unit))

                    handle(
                        event = event,
                        synchronizationCompleted = synchronizationCompleted,
                    )
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

        val completed = connectionCompleted.complete(value = fail(with = reason))
        synchronizationCompleted.complete(value = Unit)

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
