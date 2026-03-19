package network.marsys.smarthome.shared.feature.onboarding

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import network.marsys.smarthome.shared.feature.onboarding.screens.configuration.BackendUriError
import network.marsys.smarthome.shared.feature.onboarding.screens.configuration.ConfigurationOnboardingState
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.store.AppearancePreferencesRepository

class OnboardingViewModel(
    private val appearancePreferencesRepository: AppearancePreferencesRepository,
) : ViewModel() {
    private val configurationState = MutableStateFlow<ConfigurationOnboardingState>(
        value = ConfigurationOnboardingState.Idle(),
    )
    val configuration = configurationState.asStateFlow()

    val uriTextFieldState = TextFieldState()

    fun finishOnboarding() {
        if (configurationState.value is ConfigurationOnboardingState.Processing) {
            return
        }

        viewModelScope.launch {
            validateBackendUri(uri = uriTextFieldState.text.toString())
        }
    }

    fun onSelectTheme(theme: ThemeSelection) {
        viewModelScope.launch {
            appearancePreferencesRepository.setTheme(theme)
        }
    }

    private suspend fun validateBackendUri(uri: String) {
        if (uri.isBlank()) {
            configurationState.update {
                ConfigurationOnboardingState.Idle(
                    backendUriError = BackendUriError.Empty,
                )
            }
        } else {
            configurationState.update { ConfigurationOnboardingState.Processing }

            delay(timeMillis = 2500L)

            configurationState.update {
                ConfigurationOnboardingState.Idle(
                    backendUriError = BackendUriError.Invalid,
                )
            }
        }
    }
}
