package network.marsys.smarthome.shared.feature.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import network.marsys.smarthome.shared.feature.onboarding.navigation.rememberNavBackStack
import network.marsys.smarthome.shared.feature.onboarding.screens.AppearanceOnboardingScreenView
import network.marsys.smarthome.shared.feature.onboarding.screens.ConfigurationOnboardingScreenView
import network.marsys.smarthome.shared.feature.onboarding.screens.EntitiesOnboardingScreenView
import network.marsys.smarthome.shared.feature.onboarding.screens.InitialOnboardingScreenView
import network.marsys.smarthome.shared.feature.onboarding.screens.configuration.BackendUriError
import network.marsys.smarthome.shared.feature.onboarding.screens.configuration.ConfigurationOnboardingState
import network.marsys.smarthome.shared.library.design.ThemeSelection

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(OnboardingScreens::class) {
            subclass(OnboardingScreens.Initial::class, OnboardingScreens.Initial.serializer())
            subclass(OnboardingScreens.Entities::class, OnboardingScreens.Entities.serializer())
            subclass(OnboardingScreens.Appearance::class, OnboardingScreens.Appearance.serializer())
            subclass(OnboardingScreens.Configuration::class, OnboardingScreens.Configuration.serializer())
        }
    }
}

@Composable
fun OnboardingScreenView(
    onSelectTheme: (ThemeSelection) -> Unit,
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack<OnboardingScreens>(
        configuration = config,
        elements = arrayOf(OnboardingScreens.Initial),
    )

    NavDisplay(
        modifier = modifier
            .fillMaxSize(),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<OnboardingScreens.Initial> {
                InitialOnboardingScreenView(
                    navigateToEntities = {
                        backStack += OnboardingScreens.Entities
                    },
                )
            }

            entry<OnboardingScreens.Entities> {
                EntitiesOnboardingScreenView(
                    navigateToScenes = {
                        backStack += OnboardingScreens.Appearance
                    },
                    navigateBack = {
                        backStack.removeLast()
                    },
                )
            }

            entry<OnboardingScreens.Appearance> {
                AppearanceOnboardingScreenView(
                    onSelectTheme = onSelectTheme,
                    navigateToConfiguration = {
                        backStack += OnboardingScreens.Configuration
                    },
                    navigateBack = {
                        backStack.removeLast()
                    },
                )
            }

            entry<OnboardingScreens.Configuration> {
                val coroutineScope = rememberCoroutineScope()

                var state by retain {
                    mutableStateOf<ConfigurationOnboardingState>(ConfigurationOnboardingState.Idle())
                }

                val uriTextFieldState = rememberTextFieldState()

                ConfigurationOnboardingScreenView(
                    state = state,
                    uriTextFieldState = uriTextFieldState,
                    finishOnboarding = {
                        coroutineScope.launch {
                            determineConfigurationOnboardingState(
                                backendUri = uriTextFieldState.text.toString(),
                                onStateChange = {
                                    state = it
                                }
                            )
                        }
                    },
                    skipToDemo = {
                        // Not implemented yet.
                    },
                    navigateBack = {
                        backStack.removeLast()
                    },
                )
            }
        },
    )
}

private suspend fun determineConfigurationOnboardingState(
    backendUri: String,
    onStateChange: (ConfigurationOnboardingState) -> Unit,
) {
    if (backendUri.isEmpty()) {
        onStateChange.invoke(ConfigurationOnboardingState.Idle(
            backendUriError = BackendUriError.Empty,
        ))
    } else {
        onStateChange.invoke(ConfigurationOnboardingState.Processing)
        delay(2500L)
        onStateChange.invoke(ConfigurationOnboardingState.Idle(
            backendUriError = BackendUriError.Invalid,
        ))
    }
}
