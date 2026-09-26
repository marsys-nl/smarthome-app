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
import network.marsys.smarthome.shared.domain.connection.CheckSystemHealthUseCase
import network.marsys.smarthome.shared.domain.connection.DownloadConfigurationUseCase
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.Result.Companion.succeed
import network.marsys.smarthome.shared.library.core.coroutines.SuspendingActionStateMutator
import network.marsys.smarthome.shared.library.core.coroutines.handle
import network.marsys.smarthome.shared.library.core.coroutines.suspendingActionStateMutator
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import kotlin.time.Duration.Companion.seconds

internal typealias InitializationStateHolder =
    SuspendingActionStateMutator<InitializationScreenAction, InitializationScreenState>

class InitializationViewModel(
    private val applicationConfigurationRepository: ApplicationConfigurationRepository,
    private val checkSystemHealthUseCase: CheckSystemHealthUseCase,
    private val downloadConfigurationUseCase: DownloadConfigurationUseCase,
    coroutineScope: CoroutineScope,
) : ViewModel(viewModelScope = coroutineScope),
    InitializationStateHolder by coroutineScope.suspendingActionStateMutator(
        state = MutableInitializationScreenState(),
        producer = { state, actions ->
            var initializationJob: Job?

            initializationJob = launchInitializationMutations(
                applicationConfigurationRepository = applicationConfigurationRepository,
                checkSystemHealthUseCase = checkSystemHealthUseCase,
                downloadConfigurationUseCase = downloadConfigurationUseCase,
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
                            checkSystemHealthUseCase = checkSystemHealthUseCase,
                            downloadConfigurationUseCase = downloadConfigurationUseCase,
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
    checkSystemHealthUseCase: CheckSystemHealthUseCase,
    downloadConfigurationUseCase: DownloadConfigurationUseCase,
    state: MutableInitializationScreenState,
) = scope.launch {
    context(with = state) {
        if (applicationConfigurationRepository.isDemoMode.first()) {
            state.current = InitializationScreenState.Done
            return@launch
        }

        val uri = applicationConfigurationRepository.backendUri.first()
        state.uri = uri ?: "…"

        state.runInitialization {
            stage(InitializationScreenState.CheckSystemHealth) {
                checkSystemHealthUseCase.invoke()
            }

            val configuration = stage(InitializationScreenState.DownloadConfiguration) {
                downloadConfigurationUseCase.invoke()
            }

            val user = stage(InitializationScreenState.Authenticate) {
                delay(1.seconds)
                succeed(with = Unit)
            }

            //
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
    val reason: Any?,
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
            is Result.Failure -> throw StageFailure(step = step, reason = result.value)
        }
    }
}

private class MutableInitializationScreenState : InitializationScreenState {
    override var current: InitializationScreenState.State by mutableStateOf(InitializationScreenState.Idle)
    override var uri: String by mutableStateOf("…")
}
