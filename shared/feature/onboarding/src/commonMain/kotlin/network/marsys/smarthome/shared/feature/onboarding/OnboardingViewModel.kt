package network.marsys.smarthome.shared.feature.onboarding

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import network.marsys.smarthome.shared.domain.connection.ValidateBackendUriUseCase
import network.marsys.smarthome.shared.feature.onboarding.screens.configuration.BackendValidationError
import network.marsys.smarthome.shared.feature.onboarding.screens.configuration.ConfigurationOnboardingState
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.store.AppearancePreferencesRepository
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.OnboardingRepository

class OnboardingViewModel(
    private val appearancePreferencesRepository: AppearancePreferencesRepository,
    private val applicationConfigurationRepository: ApplicationConfigurationRepository,
    private val onboardingRepository: OnboardingRepository,
    private val validateBackendUriUseCase: ValidateBackendUriUseCase,
    coroutineScope: CoroutineScope? = null,
) : ViewModel(
    viewModelScope = coroutineScope ?: CoroutineScope(
        context = Dispatchers.Main.immediate + SupervisorJob(),
    ),
) {
    private var validationJob: Job? = null

    private val configurationState = MutableStateFlow<ConfigurationOnboardingState>(
        value = ConfigurationOnboardingState.Idle(),
    )
    val configuration = configurationState.asStateFlow()

    val uriTextFieldState = TextFieldState()
    val apiKeyTextFieldState = TextFieldState()

    fun cancelValidation() {
        validationJob?.cancel()
        validationJob = null

        if (configurationState.value is ConfigurationOnboardingState.Processing) {
            configurationState.update { ConfigurationOnboardingState.Idle() }
        }
    }

    fun finishOnboarding() {
        if (configurationState.value is ConfigurationOnboardingState.Processing) {
            return
        }

        validationJob = viewModelScope.launch {
            validateBackendUri(
                uri = uriTextFieldState.text.toString(),
                apiKey = apiKeyTextFieldState.text.toString()
                    .takeIf { it.isNotBlank() },
            )
        }
    }

    fun selectTheme(theme: ThemeSelection) {
        viewModelScope.launch {
            appearancePreferencesRepository.setTheme(theme)
        }
    }

    fun skipToDemo() {
        if (configurationState.value is ConfigurationOnboardingState.Processing) {
            return
        }

        viewModelScope.launch {
            configurationState.update { ConfigurationOnboardingState.Processing }

            applicationConfigurationRepository.setDemoMode(true)
            onboardingRepository.finishOnboarding()
        }
    }

    private suspend fun validateBackendUri(uri: String, apiKey: String?) {
        if (uri.isBlank()) {
            configurationState.update {
                ConfigurationOnboardingState.Idle(
                    backendValidationError = BackendValidationError.Empty,
                )
            }
        } else {
            configurationState.update { ConfigurationOnboardingState.Processing }

            when (val result = validateBackendUriUseCase.invoke(uri, apiKey)) {
                is Result.Success -> {
                    applicationConfigurationRepository.setApiKey(apiKey)
                    applicationConfigurationRepository.setBackendUri(uri)
                    applicationConfigurationRepository.setDemoMode(false)

                    onboardingRepository.finishOnboarding()
                }

                is Result.Failure -> {
                    configurationState.update {
                        ConfigurationOnboardingState.Idle(
                            backendValidationError = when (result.value) {
                                ValidateBackendUriUseCase.Reason.Unauthenticated ->
                                    BackendValidationError.InvalidApiKey

                                else ->
                                    BackendValidationError.InvalidUri
                            },
                        )
                    }
                }
            }
        }
    }
}
