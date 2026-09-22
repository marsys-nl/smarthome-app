package network.marsys.smarthome.shared.feature.initialization

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import network.marsys.smarthome.shared.domain.connection.ValidateBackendUriUseCase
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.coroutines.SuspendingActionStateMutator
import network.marsys.smarthome.shared.library.core.coroutines.handle
import network.marsys.smarthome.shared.library.core.coroutines.suspendingActionStateMutator
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import kotlin.time.Duration.Companion.seconds

internal typealias InitializationStateHolder =
    SuspendingActionStateMutator<InitializationScreenAction, InitializationScreenState>

class InitializationViewModel(
    private val applicationConfigurationRepository: ApplicationConfigurationRepository,
    private val validateBackendUriUseCase: ValidateBackendUriUseCase,
    coroutineScope: CoroutineScope,
) : ViewModel(viewModelScope = coroutineScope),
    InitializationStateHolder by coroutineScope.suspendingActionStateMutator(
        state = MutableInitializationScreenState(),
        producer = { state, actions ->
            var initializationJob: Job?

            initializationJob = launchInitializationMutations(
                applicationConfigurationRepository = applicationConfigurationRepository,
                validateBackendUriUseCase = validateBackendUriUseCase,
                state = state,
            )

            actions.handle(
                scope = this,
                keySelector = InitializationScreenAction::key,
            ) {
                when (type()) {
                    is InitializationScreenAction.RetryInitialization -> {
                        initializationJob?.cancel()
                        initializationJob = launchInitializationMutations(
                            applicationConfigurationRepository = applicationConfigurationRepository,
                            validateBackendUriUseCase = validateBackendUriUseCase,
                            state = state,
                        )
                    }
                }
            }
        },
    )

context(scope: CoroutineScope)
private fun launchInitializationMutations(
    applicationConfigurationRepository: ApplicationConfigurationRepository,
    validateBackendUriUseCase: ValidateBackendUriUseCase,
    state: MutableInitializationScreenState,
) = scope.launch {
    context(with = state) {
        if (applicationConfigurationRepository.isDemoMode.first()) {
            state.current = InitializationScreenState.Done
            return@launch
        }

        val uri = applicationConfigurationRepository.backendUri.first()
        val apiKey = applicationConfigurationRepository.apiKey.first()

        state.uri = uri ?: "…"

        if (uri == null) {
            state.current = InitializationScreenState.Error(
                step = InitializationScreenState.CheckSystemHealth,
            )
            return@launch
        }

        runInitializationStages(
            stages = listOf(
                Stage(
                    state = InitializationScreenState.CheckSystemHealth,
                    run = {
                        validateBackendUriUseCase.invoke(uri = uri, apiKey = apiKey)
                    },
                ),
                Stage(
                    state = InitializationScreenState.DownloadConfig,
                    run = {
                        delay(1.seconds)
                        Result.succeed(Unit)
                    },
                ),
                Stage(
                    state = InitializationScreenState.Authenticate,
                    run = {
                        delay(1.seconds)
                        Result.succeed(Unit)
                    },
                ),
            ),
        )
    }
}

context(state: MutableInitializationScreenState)
private suspend fun runInitializationStages(
    stages: List<Stage>,
) {
    for (stage in stages.sortedBy { it.state.order }) {
        state.current = stage.state

        @Suppress("BracesOnWhenStatements")
        when (stage.run()) {
            is Result.Success -> continue

            is Result.Failure -> {
                state.current = InitializationScreenState.Error(step = stage.state)
                return
            }
        }
    }

    state.current = InitializationScreenState.Complete
    delay(.5.seconds)
    state.current = InitializationScreenState.Done
}

private class Stage(
    val state: InitializationScreenState.Step,
    val run: suspend () -> Result<Unit, *>,
)

private class MutableInitializationScreenState : InitializationScreenState {
    override var current: InitializationScreenState.State by mutableStateOf(InitializationScreenState.Idle)
    override var uri: String by mutableStateOf("…")
}
