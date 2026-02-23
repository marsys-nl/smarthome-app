package network.marsys.smarthome.shared.feature.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import network.marsys.smarthome.shared.feature.onboarding.navigation.rememberNavBackStack
import network.marsys.smarthome.shared.feature.onboarding.screens.InitialOnboardingScreenView
import network.marsys.smarthome.shared.library.design.component.Text

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(OnboardingScreens::class) {
            subclass(OnboardingScreens.Initial::class, OnboardingScreens.Initial.serializer())
            subclass(OnboardingScreens.Entities::class, OnboardingScreens.Entities.serializer())
            subclass(OnboardingScreens.Scenes::class, OnboardingScreens.Scenes.serializer())
            subclass(OnboardingScreens.Configuration::class, OnboardingScreens.Configuration.serializer())
        }
    }
}

@Composable
fun OnboardingScreenView(
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
                Text("Not yet implemented")
            }

            entry<OnboardingScreens.Scenes> {
                Text("Not yet implemented")
            }

            entry<OnboardingScreens.Configuration> {
                Text("Not yet implemented")
            }
        },
    )
}
