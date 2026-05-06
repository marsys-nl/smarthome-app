package network.marsys.smarthome.shared.feature.onboarding.screens.configuration

sealed interface ConfigurationOnboardingState {
    data class Idle(
        val backendValidationError: BackendValidationError? = null,
    ) : ConfigurationOnboardingState

    data object Processing : ConfigurationOnboardingState
}

sealed interface BackendValidationError {
    data object Empty : BackendValidationError
    data object InvalidUri : BackendValidationError
    data object InvalidApiKey : BackendValidationError
}
