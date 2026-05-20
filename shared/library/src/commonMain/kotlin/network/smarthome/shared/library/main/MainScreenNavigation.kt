package network.smarthome.shared.library.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import network.marsys.smarthome.shared.feature.dashboard.DashboardScreenView
import network.marsys.smarthome.shared.feature.onboarding.navigation.rememberNavBackStack
import network.marsys.smarthome.shared.library.design.component.BottomNavigation
import network.marsys.smarthome.shared.library.design.component.BottomNavigationItemProviderScope
import network.marsys.smarthome.shared.library.design.component.Button
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Grid
import network.marsys.smarthome.shared.library.design.icons.House
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.User
import network.marsys.smarthome.shared.library.design.icons.Zap
import network.marsys.smarthome.shared.library.resources.SmartHomeRes
import network.marsys.smarthome.shared.library.resources.bottom_navigation_item_home
import network.marsys.smarthome.shared.library.resources.bottom_navigation_item_profile
import network.marsys.smarthome.shared.library.resources.bottom_navigation_item_rooms
import network.marsys.smarthome.shared.library.resources.bottom_navigation_item_scenes
import network.marsys.smarthome.shared.library.resources.demo_user
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.OnboardingRepository
import network.smarthome.shared.library.screens.SmartHomeScreen
import org.jetbrains.compose.resources.stringResource
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
@Suppress("LongMethod")
internal fun MainScreenNavigation(
    modifier: Modifier = Modifier,
) {
    val modalStrategy = remember { ModalSceneStrategy<SmartHomeScreen>() }
    val backStack = rememberNavBackStack<SmartHomeScreen>(
        configuration = config,
        elements = arrayOf(SmartHomeScreen.Dashboard),
    )

    NavDisplay(
        modifier = modifier
            .fillMaxSize(),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        sceneStrategies = listOf(modalStrategy),
        entryProvider = entryProvider {
            entry<SmartHomeScreen.Dashboard> {
                MainScreenNavigationItemWrapper(
                    backStack = backStack,
                ) {
                    MainScreenDashboardScreenView(
                        onChangeAppearanceClick = {
                            backStack += SmartHomeScreen.AppAppearance
                        },
                    )
                }
            }

            entry<SmartHomeScreen.Rooms> {
                MainScreenNavigationItemWrapper(
                    backStack = backStack,
                ) {
                    MainScreenPlaceholderScreenView(
                        screen = SmartHomeScreen.Rooms,
                    )
                }
            }

            entry<SmartHomeScreen.Scenes> {
                MainScreenNavigationItemWrapper(
                    backStack = backStack,
                ) {
                    MainScreenPlaceholderScreenView(
                        screen = SmartHomeScreen.Scenes,
                    )
                }
            }

            entry<SmartHomeScreen.Profile> {
                MainScreenNavigationItemWrapper(
                    backStack = backStack,
                ) {
                    MainScreenPlaceholderScreenView(
                        screen = SmartHomeScreen.Profile,
                    )
                }
            }

            entry<SmartHomeScreen.AppAppearance>(
                metadata = ModalSceneStrategy.modal(),
            ) {
                Text(text = "Modal preview")
            }
        },
    )
}

@Composable
private fun MainScreenNavigationItemWrapper(
    backStack: NavBackStack<SmartHomeScreen>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val blurModifier = when (backStack.lastOrNull() is SmartHomeScreen.Modal) {
        true -> Modifier.blur(4.dp)
        false -> Modifier
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .then(blurModifier),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .safeContentPadding()
                .weight(1f),
        ) {
            content.invoke()
        }

        BottomNavigation(
            selectedNavigationItem = backStack.firstOrNull(),
            onNavigationItemSelect = { screen ->
                backStack.clear()
                backStack += screen
            },
            navigationItemProvider = bottomNavigationItems(),
        )
    }
}

@Composable
private fun bottomNavigationItems(): BottomNavigationItemProviderScope<SmartHomeScreen>.() -> Unit {
    val homeNavigationItemTextResource = stringResource(SmartHomeRes.string.bottom_navigation_item_home)
    val roomsNavigationItemTextResource = stringResource(SmartHomeRes.string.bottom_navigation_item_rooms)
    val scenesNavigationItemTextResource = stringResource(SmartHomeRes.string.bottom_navigation_item_scenes)
    val profileNavigationItemTextResource = stringResource(SmartHomeRes.string.bottom_navigation_item_profile)

    return {
        item(
            screen = SmartHomeScreen.Dashboard,
            text = homeNavigationItemTextResource,
            icon = Icons.House,
        )

        item(
            screen = SmartHomeScreen.Rooms,
            text = roomsNavigationItemTextResource,
            icon = Icons.Grid,
        )

        item(
            screen = SmartHomeScreen.Scenes,
            text = scenesNavigationItemTextResource,
            icon = Icons.Zap,
        )

        item(
            screen = SmartHomeScreen.Profile,
            text = profileNavigationItemTextResource,
            icon = Icons.User,
        )
    }
}

@Composable
private fun MainScreenDashboardScreenView(
    onChangeAppearanceClick: () -> Unit,
    modifier: Modifier = Modifier,
    applicationConfigurationRepository: ApplicationConfigurationRepository = koinInject(),
    onboardingRepository: OnboardingRepository = koinInject(),
) {
    val demoMode by applicationConfigurationRepository.isDemoMode
        .collectAsStateWithLifecycle(initialValue = false)

    DashboardScreenView(
        name = if (demoMode) {
            stringResource(SmartHomeRes.string.demo_user)
        } else {
            "Niels"
        },
        onChangeAppearanceClick = onChangeAppearanceClick,
        modifier = modifier,
    ) {
        val coroutineScope = rememberCoroutineScope()

        Button(
            modifier = Modifier
                .padding(top = 32.dp),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
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

@Composable
private fun MainScreenPlaceholderScreenView(
    screen: SmartHomeScreen,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
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
