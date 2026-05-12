package network.smarthome.shared.library.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import network.marsys.smarthome.shared.feature.onboarding.navigation.rememberNavBackStack
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.component.BottomNavigation
import network.marsys.smarthome.shared.library.design.component.Button
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Grid
import network.marsys.smarthome.shared.library.design.icons.House
import network.marsys.smarthome.shared.library.design.icons.HousePlug
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.User
import network.marsys.smarthome.shared.library.design.icons.Zap
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.OnboardingRepository
import network.smarthome.shared.library.screens.SmartHomeScreen
import org.koin.compose.koinInject

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(SmartHomeScreen::class) {
            subclass(SmartHomeScreen.Dashboard::class, SmartHomeScreen.Dashboard.serializer())
            subclass(SmartHomeScreen.Rooms::class, SmartHomeScreen.Rooms.serializer())
            subclass(SmartHomeScreen.Scenes::class, SmartHomeScreen.Scenes.serializer())
            subclass(SmartHomeScreen.Profile::class, SmartHomeScreen.Profile.serializer())
        }
    }
}

@Composable
internal fun MainScreenNavigation(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack<SmartHomeScreen>(
        configuration = config,
        elements = arrayOf(SmartHomeScreen.Dashboard),
    )

    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        NavDisplay(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<SmartHomeScreen.Dashboard> {
                    MainScreenDashboardScreenView()
                }

                entry<SmartHomeScreen.Rooms> {
                    MainScreenPlaceholderScreenView(
                        screen = SmartHomeScreen.Rooms,
                    )
                }

                entry<SmartHomeScreen.Scenes> {
                    MainScreenPlaceholderScreenView(
                        screen = SmartHomeScreen.Scenes,
                    )
                }

                entry<SmartHomeScreen.Profile> {
                    MainScreenPlaceholderScreenView(
                        screen = SmartHomeScreen.Profile,
                    )
                }
            },
        )

        BottomNavigation(
            selectedNavigationItem = backStack.firstOrNull(),
            onNavigationItemSelect = { screen ->
                backStack.clear()
                backStack += screen
            },
            navigationItemProvider = {
                item(
                    screen = SmartHomeScreen.Dashboard,
                    text = "Home",
                    icon = Icons.House,
                )

                item(
                    screen = SmartHomeScreen.Rooms,
                    text = "Rooms",
                    icon = Icons.Grid,
                )

                item(
                    screen = SmartHomeScreen.Scenes,
                    text = "Scenes",
                    icon = Icons.Zap,
                )

                item(
                    screen = SmartHomeScreen.Profile,
                    text = "Profile",
                    icon = Icons.User,
                )
            },
        )
    }
}

@Composable
private fun MainScreenDashboardScreenView(
    modifier: Modifier = Modifier,
    applicationConfigurationRepository: ApplicationConfigurationRepository = koinInject(),
    onboardingRepository: OnboardingRepository = koinInject(),
) {
    val demoMode by applicationConfigurationRepository.isDemoMode
        .collectAsStateWithLifecycle(initialValue = false)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SmartHomeTheme.colors[ColorKeyToken.BackgroundPrimary]),
        contentAlignment = Alignment.Center,
    ) {
        Column(
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
                },
            ) {
                Text(text = "Reset onboarding")
            }
        }
    }
}

@Composable
private fun MainScreenPlaceholderScreenView(
    screen: SmartHomeScreen,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SmartHomeTheme.colors[ColorKeyToken.BackgroundPrimary]),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = screen.toString())
        }
    }
}
