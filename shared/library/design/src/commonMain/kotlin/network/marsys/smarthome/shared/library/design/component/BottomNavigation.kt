package network.marsys.smarthome.shared.library.design.component

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.icons.Grid3x3
import network.marsys.smarthome.shared.library.design.icons.House
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.User
import network.marsys.smarthome.shared.library.design.icons.Zap
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.components.BottomNavigationTokens

@Composable
fun <T : Any> BottomNavigation(
    selectedNavigationItem: T?,
    onNavigationItemSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    colors: BottomNavigationColors = BottomNavigationDefaults.colors(),
    navigationItemProvider: BottomNavigationItemProviderScope<T>.() -> Unit,
) {
    val positions = remember { mutableStateMapOf<T, Int>() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(BottomNavigationDefaults.height())
            .background(colors.navigationBarBackgroundColor),
        contentAlignment = Alignment.TopCenter,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.navigationBarBorderColor),
        )

        val scope = BottomNavigationItemProviderScope<T>(colors = colors)
            .apply(navigationItemProvider)

        Box(
            modifier = Modifier
                .width(BottomNavigationDefaults.width())
                .fillMaxHeight(),
        ) {
            SelectedNavigationItemIndicator(
                selectedItemOffset = selectedNavigationItem?.let { positions[it] },
                colors = colors,
            )

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                scope.items.forEach { navigationItem ->
                    NavigationItem(
                        text = navigationItem.value.text,
                        icon = navigationItem.value.icon,
                        selected = navigationItem.value.screen == selectedNavigationItem,
                        colors = colors,
                        modifier = Modifier
                            .width(BottomNavigationDefaults.itemWidth())
                            .onGloballyPositioned { coordinates ->
                                positions[navigationItem.key] = coordinates.boundsInParent().left.toInt()
                            }
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onNavigationItemSelect.invoke(navigationItem.value.screen) },
                            ),
                    )
                }
            }
        }
    }
}

@Composable
private fun BoxScope.SelectedNavigationItemIndicator(
    selectedItemOffset: Int?,
    colors: BottomNavigationColors,
) {
    if (selectedItemOffset != null) {
        val animatedOffset by animateIntOffsetAsState(
            targetValue = IntOffset(selectedItemOffset, 0),
            animationSpec = tween(durationMillis = 200),
        )

        Box(
            modifier = Modifier
                .offset { animatedOffset }
                .width(BottomNavigationDefaults.itemWidth())
                .height(4.dp)
                .background(
                    brush = colors.selectedNavigationItemColor,
                    shape = CircleShape,
                )
                .align(Alignment.TopStart),
        )
    }
}

@Composable
private fun NavigationItem(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    colors: BottomNavigationItemColors,
    modifier: Modifier = Modifier,
) {
    val iconTintColor = colors.navigationItemIconColor(selected).value
    val contentColor = colors.navigationItemContentColor(selected).value

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .height(60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                icon = icon,
                modifier = Modifier
                    .padding(bottom = 4.dp),
                size = 24.dp,
                tint = iconTintColor,
            )

            Text(
                text = text,
                fontSize = 10.sp,
                color = contentColor,
            )
        }
    }
}

@DslMarker
private annotation class BottomNavigationItemDsl

@PublishedApi
internal data class BottomNavigationItem<T : Any>(
    val screen: T,
    val text: String,
    val icon: ImageVector,
)

@BottomNavigationItemDsl
class BottomNavigationItemProviderScope<T : Any>(
    internal val colors: BottomNavigationColors,
) {
    @PublishedApi
    internal val items = mutableMapOf<T, BottomNavigationItem<T>>()

    inline fun <reified K : T> item(
        screen: K,
        text: String,
        icon: ImageVector,
    ) {
        items[screen] = BottomNavigationItem(
            screen = screen,
            text = text,
            icon = icon,
        )
    }
}

internal interface BottomNavigationItemColors {
    @Composable
    fun navigationItemContentColor(selected: Boolean): State<Color>

    @Composable
    fun navigationItemIconColor(selected: Boolean): State<Color>
}

@Immutable
@ConsistentCopyVisibility
data class BottomNavigationColors internal constructor(
    internal val navigationBarBackgroundColor: Brush,
    internal val navigationBarBorderColor: Color,
    private val navigationItemContentColor: Color,
    private val navigationItemIconColor: Color,
    internal val selectedNavigationItemColor: Brush,
    private val selectedNavigationItemContentColor: Color,
    private val selectedNavigationItemIconColor: Color,
) : BottomNavigationItemColors {
    @Composable
    override fun navigationItemContentColor(selected: Boolean): State<Color> =
        rememberUpdatedState(
            if (selected) selectedNavigationItemContentColor else navigationItemContentColor,
        )

    @Composable
    override fun navigationItemIconColor(selected: Boolean): State<Color> =
        rememberUpdatedState(
            if (selected) selectedNavigationItemIconColor else navigationItemIconColor,
        )
}

object BottomNavigationDefaults {
    @Composable
    fun colors(
        navigationBarBackgroundColor: Brush = BottomNavigationTokens.NavigationBarBackgroundColor,
        navigationBarBorderColor: Color = BottomNavigationTokens.NavigationBarBorderColor,
        navigationItemContentColor: Color = BottomNavigationTokens.NavigationItemContentColor,
        navigationItemIconColor: Color = navigationItemContentColor,
        selectedNavigationItemColor: Brush = BottomNavigationTokens.SelectedNavigationItemColor,
        selectedNavigationItemContentColor: Color = BottomNavigationTokens.SelectedNavigationItemContentColor,
        selectedNavigationItemIconColor: Color = BottomNavigationTokens.SelectedNavigationItemIconColor,
    ): BottomNavigationColors = BottomNavigationColors(
        navigationBarBackgroundColor = navigationBarBackgroundColor,
        navigationBarBorderColor = navigationBarBorderColor,
        navigationItemContentColor = navigationItemContentColor,
        navigationItemIconColor = navigationItemIconColor,
        selectedNavigationItemColor = selectedNavigationItemColor,
        selectedNavigationItemContentColor = selectedNavigationItemContentColor,
        selectedNavigationItemIconColor = selectedNavigationItemIconColor,
    )

    @Composable
    fun height() = BottomNavigationTokens.BottomNavigationHeight

    @Composable
    fun width() = BottomNavigationTokens.BottomNavigationWidth

    @Composable
    fun itemWidth() = BottomNavigationTokens.BottomNavigationItemWidth
}

@PreviewScreenSizes
@Composable
private fun BottomNavigationPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    var selectedNavigationItem by remember { mutableStateOf("home") }

    SmartHomeTheme(
        theme = theme,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SmartHomeTheme.colors[ColorKeyToken.BackgroundPrimary]),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f),
            )

            BottomNavigation(
                selectedNavigationItem = selectedNavigationItem,
                onNavigationItemSelect = {
                    selectedNavigationItem = it
                },
            ) {
                item(
                    screen = "home",
                    text = "Home",
                    icon = Icons.House,
                )

                item(
                    screen = "rooms",
                    text = "Rooms",
                    icon = Icons.Grid3x3,
                )

                item(
                    screen = "scenes",
                    text = "Scenes",
                    icon = Icons.Zap,
                )

                item(
                    screen = "profile",
                    text = "Profile",
                    icon = Icons.User,
                )
            }
        }
    }
}
