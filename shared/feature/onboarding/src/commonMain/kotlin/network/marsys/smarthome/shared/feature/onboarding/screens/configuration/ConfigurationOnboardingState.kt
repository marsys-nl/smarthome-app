package network.marsys.smarthome.shared.feature.onboarding.screens.configuration

sealed interface ConfigurationOnboardingState {
    data class Idle(
        val backendUriError: BackendUriError? = null,
    ) : ConfigurationOnboardingState

    data object Processing : ConfigurationOnboardingState
}

sealed interface BackendUriError {
    data object Empty : BackendUriError
    data object Invalid : BackendUriError
}
