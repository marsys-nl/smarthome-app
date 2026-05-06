package network.marsys.smarthome.shared.feature.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import network.marsys.smarthome.shared.feature.onboarding.navigation.rememberNavBackStack
import network.marsys.smarthome.shared.feature.onboarding.screens.AppearanceOnboardingScreenView
import network.marsys.smarthome.shared.feature.onboarding.screens.ConfigurationOnboardingScreenView
import network.marsys.smarthome.shared.feature.onboarding.screens.EntitiesOnboardingScreenView
import network.marsys.smarthome.shared.feature.onboarding.screens.InitialOnboardingScreenView
import org.koin.compose.viewmodel.koinViewModel

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
@Suppress("LongMethod")
fun OnboardingScreenView(
    modifier: Modifier = Modifier,
    onboardingSessionKey: Int = 0,
    viewModel: OnboardingViewModel = koinViewModel(key = "onboarding-session-$onboardingSessionKey"),
) {
    val backStack = rememberNavBackStack<OnboardingScreens>(
        onboardingSessionKey,
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
                    onSelectTheme = viewModel::selectTheme,
                    navigateToConfiguration = {
                        backStack += OnboardingScreens.Configuration
                    },
                    navigateBack = {
                        backStack.removeLast()
                    },
                )
            }

            entry<OnboardingScreens.Configuration> {
                val state by viewModel.configuration
                    .collectAsStateWithLifecycle()

                ConfigurationOnboardingScreenView(
                    state = state,
                    uriTextFieldState = viewModel.uriTextFieldState,
                    apiKeyTextFieldState = viewModel.apiKeyTextFieldState,
                    finishOnboarding = viewModel::finishOnboarding,
                    skipToDemo = viewModel::skipToDemo,
                    navigateBack = {
                        backStack.removeLast()
                    },
                )
            }
        },
    )
}
