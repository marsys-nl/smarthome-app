package network.smarthome.shared.library.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import network.marsys.smarthome.shared.feature.dashboard.DashboardScreenView
import network.marsys.smarthome.shared.feature.onboarding.navigation.rememberNavBackStack
import network.marsys.smarthome.shared.feature.profile.ProfileScreenView
import network.marsys.smarthome.shared.library.design.component.BottomNavigation
import network.marsys.smarthome.shared.library.design.component.BottomNavigationItemProviderScope
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Grid3x3
import network.marsys.smarthome.shared.library.design.icons.House
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.User
import network.marsys.smarthome.shared.library.design.icons.Zap
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.library.navigation.NavigationDestination
import network.marsys.smarthome.shared.library.resources.SmartHomeRes
import network.marsys.smarthome.shared.library.resources.bottom_navigation_item_home
import network.marsys.smarthome.shared.library.resources.bottom_navigation_item_profile
import network.marsys.smarthome.shared.library.resources.bottom_navigation_item_rooms
import network.marsys.smarthome.shared.library.resources.bottom_navigation_item_scenes
import network.marsys.smarthome.shared.library.store.AppearancePreferencesRepository
import network.marsys.smarthome.shared.modal.appearance.AppAppearanceModalContent
import network.marsys.smarthome.shared.modal.entity.EntityDetailModal
import network.smarthome.shared.library.screens.SmartHomeScreen
import org.koin.compose.koinInject

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(SmartHomeScreen::class) {
            subclass(SmartHomeScreen.Dashboard::class, SmartHomeScreen.Dashboard.serializer())
            subclass(SmartHomeScreen.Rooms::class, SmartHomeScreen.Rooms.serializer())
            subclass(SmartHomeScreen.Scenes::class, SmartHomeScreen.Scenes.serializer())
            subclass(SmartHomeScreen.Profile::class, SmartHomeScreen.Profile.serializer())

            subclass(SmartHomeScreen.AppAppearance::class, SmartHomeScreen.AppAppearance.serializer())
            subclass(SmartHomeScreen.EntityDetails::class, SmartHomeScreen.EntityDetails.serializer())
        }
    }
}

@Composable
@Suppress("LongMethod")
internal fun MainScreenNavigation(
    modifier: Modifier = Modifier,
    appearancePreferencesRepository: AppearancePreferencesRepository = koinInject(),
) {
    val modalStrategy = remember { ModalSceneStrategy<SmartHomeScreen>() }
    val backStack = rememberNavBackStack<SmartHomeScreen>(
        configuration = config,
        elements = arrayOf(SmartHomeScreen.Dashboard),
    )

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        sceneStrategies = listOf(modalStrategy),
        entryProvider = entryProvider {
            entry<SmartHomeScreen.Dashboard> {
                MainScreenNavigationItemWrapper(
                    backStack = backStack,
                ) {
                    DashboardScreenView(
                        onNavigate = { target ->
                            with(backStack) {
                                handleNavigationDestination(target)
                            }
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
                    ProfileScreenView(
                        onNavigate = { target ->
                            with(backStack) {
                                handleNavigationDestination(target)
                            }
                        },
                    )
                }
            }

            entry<SmartHomeScreen.AppAppearance>(
                metadata = ModalSceneStrategy.modal(),
            ) {
                val coroutineScope = rememberCoroutineScope()

                AppAppearanceModalContent(
                    onDismissRequest = { backStack.removeLastOrNull() },
                    onSelectTheme = {
                        coroutineScope.launch {
                            appearancePreferencesRepository.setTheme(it)
                        }
                    },
                )
            }

            entry<SmartHomeScreen.EntityDetails>(
                metadata = ModalSceneStrategy.modal(),
            ) {
                EntityDetailModal(
                    entity = it.entity,
                    onDismissRequest = { backStack.removeLastOrNull() },
                )
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
            .background(LocalColorScheme.current[ColorKeyToken.BackgroundPrimary])
            .then(blurModifier)
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .safeContentPadding(),
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

context(backStack: NavBackStack<SmartHomeScreen>)
private fun handleNavigationDestination(target: NavigationDestination) {
    if (target is NavigationDestination.MainNavigationDestination) {
        backStack.clear()
    }

    backStack += when (target) {
        is NavigationDestination.Areas ->
            SmartHomeScreen.Rooms

        is NavigationDestination.ChangeAppAppearanceModal ->
            SmartHomeScreen.AppAppearance

        is NavigationDestination.EntityDetailModal ->
            SmartHomeScreen.EntityDetails(target.entity)
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
            icon = Icons.Grid3x3,
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
