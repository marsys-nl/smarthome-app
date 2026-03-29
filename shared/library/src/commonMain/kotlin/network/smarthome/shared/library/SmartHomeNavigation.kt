package network.smarthome.shared.library

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import network.marsys.smarthome.shared.feature.onboarding.OnboardingScreenView
import network.marsys.smarthome.shared.feature.onboarding.navigation.rememberNavBackStack
import network.smarthome.shared.library.screens.SmartHomeScreens

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(SmartHomeScreens::class) {
            subclass(SmartHomeScreens.Onboarding::class, SmartHomeScreens.Onboarding.serializer())
            subclass(SmartHomeScreens.Dashboard::class, SmartHomeScreens.Dashboard.serializer())
        }
    }
}

@Composable
fun SmartHomeNavigation(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack<SmartHomeScreens>(
        configuration = config,
        elements = arrayOf(SmartHomeScreens.Onboarding),
    )

    NavDisplay(
        modifier = modifier
            .fillMaxSize(),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<SmartHomeScreens.Onboarding> {
                OnboardingScreenView()
            }

            entry<SmartHomeScreens.Dashboard> {
                // No implementation yet.
            }
        },
    )
}
