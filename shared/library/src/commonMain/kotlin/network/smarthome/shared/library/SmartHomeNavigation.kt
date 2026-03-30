package network.smarthome.shared.library

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
import network.marsys.smarthome.shared.feature.onboarding.OnboardingScreenView
import network.marsys.smarthome.shared.feature.onboarding.navigation.rememberNavBackStack
import network.marsys.smarthome.shared.library.store.OnboardingRepository
import network.smarthome.shared.library.screens.SmartHomeScreens
import org.koin.compose.koinInject

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
    onboardingRepository: OnboardingRepository = koinInject()
) {
    val isOnboardingFinished by onboardingRepository.isOnboardingFinished
        .collectAsStateWithLifecycle(initialValue = null)

    val initialScreen = when (isOnboardingFinished) {
        true -> SmartHomeScreens.Onboarding
        false -> SmartHomeScreens.Onboarding
        else -> return SmartHomeLoadingScreen()
    }

    val backStack = rememberNavBackStack<SmartHomeScreens>(
        configuration = config,
        elements = arrayOf(initialScreen),
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
