package network.marsys.smarthome.shared.feature.initialization

import androidx.compose.runtime.Stable

@Stable
interface InitializationScreenState {
    val current: State
    val uri: String

    @Stable
    sealed interface State

    @Stable
    sealed class Step(
        val order: Int,
    ) : State

    data object Idle : State

    data object CheckSystemHealth : Step(order = 1)
    data object DownloadConfig : Step(order = 2)
    data object Authenticate : Step(order = 3)

    data object Complete : State
    data object Done : State

    data class Error(
        val step: Step,
    ) : State
}

sealed class InitializationScreenAction(val key: String) {
    data object RetryInitialization : InitializationScreenAction("RetryInitialization")
}
