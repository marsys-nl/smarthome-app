package network.smarthome.shared.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import network.marsys.smarthome.shared.feature.onboarding.OnboardingScreenView
import network.marsys.smarthome.shared.feature.onboarding.navigation.rememberNavBackStack
import network.marsys.smarthome.shared.library.design.component.Button
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
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
    onboardingRepository: OnboardingRepository = koinInject(),
) {
    var onboardingSessionKey by remember { mutableIntStateOf(0) }

    val isOnboardingFinished by onboardingRepository.isOnboardingFinished
        .collectAsStateWithLifecycle(initialValue = null)

    val initialScreen = when (isOnboardingFinished) {
        true -> SmartHomeScreens.Dashboard
        false -> SmartHomeScreens.Onboarding
        else -> return SmartHomeLoadingScreen()
    }

    val backStack = rememberNavBackStack<SmartHomeScreens>(
        configuration = config,
        elements = arrayOf(initialScreen),
    )

    LaunchedEffect(isOnboardingFinished) {
        val targetScreen = when (isOnboardingFinished) {
            true -> SmartHomeScreens.Dashboard
            else -> SmartHomeScreens.Onboarding
        }

        backStack.clear()
        backStack += targetScreen

        if (isOnboardingFinished == false) {
            onboardingSessionKey++
        }
    }

    NavDisplay(
        modifier = modifier
            .fillMaxSize(),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<SmartHomeScreens.Onboarding> {
                OnboardingScreenView(
                    onboardingSessionKey = onboardingSessionKey,
                )
            }

            entry<SmartHomeScreens.Dashboard> {
                SmartHomeDashboard()
            }
        },
    )
}

@Composable
private fun SmartHomeDashboard(
    modifier: Modifier = Modifier,
    applicationConfigurationRepository: ApplicationConfigurationRepository = koinInject(),
    onboardingRepository: OnboardingRepository = koinInject(),
) {
    val demoMode by applicationConfigurationRepository.isDemoMode
        .collectAsStateWithLifecycle(initialValue = false)

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val coroutineScope = rememberCoroutineScope()

            Text(text = "Dashboard" + if (demoMode) " (demo)" else "")
            Button(
                onClick = {
                    coroutineScope.launch {
                        onboardingRepository.resetOnboarding()
                    }
                }
            ) {
                Text(text = "Reset onboarding")
            }
        }
    }
}
