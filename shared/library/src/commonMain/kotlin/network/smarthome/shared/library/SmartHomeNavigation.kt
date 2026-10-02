package network.smarthome.shared.library

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import network.marsys.smarthome.shared.feature.onboarding.OnboardingScreenView
import network.marsys.smarthome.shared.feature.onboarding.navigation.rememberNavBackStack
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.model.ConnectionConfiguration
import network.smarthome.shared.library.main.MainScreenView
import org.koin.compose.koinInject

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(SmartHomeNavigationFlow::class) {
            subclass(SmartHomeNavigationFlow.Onboarding::class, SmartHomeNavigationFlow.Onboarding.serializer())
            subclass(SmartHomeNavigationFlow.Main::class, SmartHomeNavigationFlow.Main.serializer())
        }
    }
}

@Composable
fun SmartHomeNavigation(
    modifier: Modifier = Modifier,
    applicationConfigurationRepository: ApplicationConfigurationRepository = koinInject(),
) {
    var onboardingSessionKey by retain { mutableIntStateOf(0) }

    val configuration by applicationConfigurationRepository.connection
        .collectAsStateWithLifecycle(initialValue = ConnectionConfiguration.Unconfigured)

    val initialScreen = when (configuration) {
        is ConnectionConfiguration.Unconfigured -> SmartHomeNavigationFlow.Onboarding
        else -> SmartHomeNavigationFlow.Main
    }

    val backStack = rememberNavBackStack<SmartHomeNavigationFlow>(
        configuration = config,
        elements = arrayOf(initialScreen),
    )

    LaunchedEffect(configuration) {
        val targetScreen = when (configuration) {
            is ConnectionConfiguration.Unconfigured -> SmartHomeNavigationFlow.Onboarding
            else -> SmartHomeNavigationFlow.Main
        }

        if (backStack.lastOrNull() == targetScreen && backStack.size == 1) return@LaunchedEffect

        backStack.clear()
        backStack += targetScreen

        if (configuration is ConnectionConfiguration.Unconfigured) {
            onboardingSessionKey++
        }
    }

    NavDisplay(
        modifier = modifier
            .fillMaxSize(),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<SmartHomeNavigationFlow.Onboarding> {
                OnboardingScreenView(
                    onboardingSessionKey = onboardingSessionKey,
                )
            }

            entry<SmartHomeNavigationFlow.Main> {
                MainScreenView()
            }
        },
    )
}
