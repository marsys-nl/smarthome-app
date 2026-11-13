package network.marsys.smarthome.shared.feature.initialization

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import network.marsys.smarthome.shared.domain.authentication.AuthenticateUseCase
import network.marsys.smarthome.shared.domain.connection.CheckSystemHealthUseCase
import network.marsys.smarthome.shared.domain.connection.ConnectionState
import network.marsys.smarthome.shared.domain.connection.DownloadConfigurationUseCase
import network.marsys.smarthome.shared.domain.connection.EstablishConnectionUseCase
import network.marsys.smarthome.shared.domain.connection.ObserveConnectionUseCase
import network.marsys.smarthome.shared.domain.connection.SynchronizeEntitiesUseCase
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.coroutines.SuspendingActionStateMutator
import network.marsys.smarthome.shared.library.core.coroutines.handle
import network.marsys.smarthome.shared.library.core.coroutines.suspendingActionStateMutator
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.model.ConnectionConfiguration
import kotlin.time.Duration.Companion.seconds

internal typealias InitializationStateHolder =
    SuspendingActionStateMutator<InitializationScreenAction, InitializationScreenState>

class InitializationViewModel(
    private val applicationConfigurationRepository: ApplicationConfigurationRepository,
    private val authenticateUseCase: AuthenticateUseCase,
    private val checkSystemHealthUseCase: CheckSystemHealthUseCase,
    private val downloadConfigurationUseCase: DownloadConfigurationUseCase,
    private val synchronizeEntitiesUseCase: SynchronizeEntitiesUseCase,
    private val establishConnectionUseCase: EstablishConnectionUseCase,
    private val observeConnectionUseCase: ObserveConnectionUseCase,
    coroutineScope: CoroutineScope,
) : ViewModel(viewModelScope = coroutineScope),
    InitializationStateHolder by initializationStateHolder(
        scope = coroutineScope,
        observeConnectionUseCase = observeConnectionUseCase,
        initialize = { state ->
            state.initialize(
                applicationConfigurationRepository = applicationConfigurationRepository,
                authenticateUseCase = authenticateUseCase,
                checkSystemHealthUseCase = checkSystemHealthUseCase,
                downloadConfigurationUseCase = downloadConfigurationUseCase,
                synchronizeEntitiesUseCase = synchronizeEntitiesUseCase,
                establishConnectionUseCase = establishConnectionUseCase,
            )
        },
    )

private fun initializationStateHolder(
    scope: CoroutineScope,
    observeConnectionUseCase: ObserveConnectionUseCase,
    initialize: suspend (MutableInitializationScreenState) -> Unit,
): InitializationStateHolder {
    val state = MutableInitializationScreenState()
    var job: Job? = null

    fun restart() {
        job?.cancel()
        job = scope.launch {
            initialize.invoke(state)
        }
    }

    return scope.suspendingActionStateMutator(state = state) { state, actions ->
        if (job?.isActive != true && state.current != InitializationScreenState.Done) {
            restart()
        }

        launch {
            observeConnectionUseCase.invoke()
                .filter { it == ConnectionState.Lost }
                .collect {
                    if (state.current == InitializationScreenState.Done) {
                        restart()
                    }
                }
        }

        actions.handle(
            scope = this,
            keySelector = InitializationScreenAction::key,
        ) {
            when (val action = type()) {
                is InitializationScreenAction.RetryInitialization -> action.flow.collect {
                    restart()
                }
            }
        }
    }
}

private suspend fun MutableInitializationScreenState.initialize(
    applicationConfigurationRepository: ApplicationConfigurationRepository,
    authenticateUseCase: AuthenticateUseCase,
    checkSystemHealthUseCase: CheckSystemHealthUseCase,
    downloadConfigurationUseCase: DownloadConfigurationUseCase,
    synchronizeEntitiesUseCase: SynchronizeEntitiesUseCase,
    establishConnectionUseCase: EstablishConnectionUseCase,
) {
    val configuration = applicationConfigurationRepository.connection.first()

    if (configuration is ConnectionConfiguration.Demo) {
        current = InitializationScreenState.Done
        return
    }

    uri = when (val connection = configuration) {
        is ConnectionConfiguration.Backend -> connection.uri
        else -> "…"
    }

    runInitialization {
        stage(InitializationScreenState.CheckSystemHealth) {
            checkSystemHealthUseCase.invoke()
        }

        val configuration = stage(InitializationScreenState.DownloadConfiguration) {
            downloadConfigurationUseCase.invoke()
        }

        stage(InitializationScreenState.Authenticate) {
            authenticateUseCase.invoke(
                issuer = configuration.authUri,
                clientIdentifier = "smarthome-app",
            )
        }

        stage(InitializationScreenState.SynchronizeApplicationState) {
            // Synchronize first
            synchronizeEntitiesUseCase.invoke()

            // Establish connection afterwards
            establishConnectionUseCase.invoke()
        }
    }
}

private suspend fun MutableInitializationScreenState.runInitialization(
    block: suspend InitializationScope.() -> Unit,
) {
    try {
        block.invoke(InitializationScope(state = this))
    } catch (failure: StageFailure) {
        current = InitializationScreenState.Error(step = failure.step)
        return
    }

    current = InitializationScreenState.Complete
    delay(.5.seconds)
    current = InitializationScreenState.Done
}

class StageFailure(
    val step: InitializationScreenState.Step,
) : Exception()

private class InitializationScope(
    private val state: MutableInitializationScreenState,
) {
    suspend fun <T> stage(
        step: InitializationScreenState.Step,
        block: suspend () -> Result<T, *>,
    ): T {
        state.current = step

        return when (val result = block()) {
            is Result.Success -> result.value
            is Result.Failure -> throw StageFailure(step = step)
        }
    }
}

private class MutableInitializationScreenState : InitializationScreenState {
    override var current: InitializationScreenState.State by mutableStateOf(InitializationScreenState.Idle)
    override var uri: String by mutableStateOf("…")
}
