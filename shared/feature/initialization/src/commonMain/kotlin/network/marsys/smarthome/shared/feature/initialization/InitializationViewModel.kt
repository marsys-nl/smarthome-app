package network.marsys.smarthome.shared.feature.initialization

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import network.marsys.smarthome.shared.library.core.coroutines.SuspendingActionStateMutator
import network.marsys.smarthome.shared.library.core.coroutines.handle
import network.marsys.smarthome.shared.library.core.coroutines.suspendingActionStateMutator
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import kotlin.time.Duration.Companion.seconds

internal typealias InitializationStateHolder =
    SuspendingActionStateMutator<InitializationScreenAction, InitializationScreenState>

class InitializationViewModel(
    private val applicationConfigurationRepository: ApplicationConfigurationRepository,
    coroutineScope: CoroutineScope,
) : ViewModel(viewModelScope = coroutineScope),
    InitializationStateHolder by coroutineScope.suspendingActionStateMutator(
        state = MutableInitializationScreenState(),
        producer = { state, actions ->
            var connectionValidationJob: Job?

            launchDemoModeMutations(
                applicationConfigurationRepository = applicationConfigurationRepository,
                state = state,
            )

            launchUriMutations(
                applicationConfigurationRepository = applicationConfigurationRepository,
                state = state,
            )

            connectionValidationJob = launchConnectionValidationMutations(
                state = state,
            )

            actions.handle(
                scope = this,
                keySelector = InitializationScreenAction::key,
            ) {
                when (type()) {
                    is InitializationScreenAction.RetryInitialization -> {
                        connectionValidationJob?.cancel()
                        connectionValidationJob = launchConnectionValidationMutations(
                            state = state,
                        )
                    }
                }
            }
        },
    )

context(scope: CoroutineScope)
private fun launchDemoModeMutations(
    applicationConfigurationRepository: ApplicationConfigurationRepository,
    state: MutableInitializationScreenState,
) {
    scope.launch {
        applicationConfigurationRepository.isDemoMode.collect {
            if (it) {
                state.current = InitializationScreenState.Done
            }
        }
    }
}

context(scope: CoroutineScope)
private fun launchUriMutations(
    applicationConfigurationRepository: ApplicationConfigurationRepository,
    state: MutableInitializationScreenState,
) {
    scope.launch {
        applicationConfigurationRepository.backendUri.collect {
            state.uri = it ?: "…"
        }
    }
}

context(scope: CoroutineScope)
private fun launchConnectionValidationMutations(
    state: MutableInitializationScreenState,
) = scope.launch {
    state.current = InitializationScreenState.CheckSystemHealth
    delay(1.seconds)
    state.current = InitializationScreenState.DownloadConfig
    delay(2.seconds)
    state.current = InitializationScreenState.Error(
        step = InitializationScreenState.DownloadConfig,
    )
}

private class MutableInitializationScreenState : InitializationScreenState {
    override var current: InitializationScreenState.State by mutableStateOf(InitializationScreenState.Idle)
    override var uri: String by mutableStateOf("…")
}
