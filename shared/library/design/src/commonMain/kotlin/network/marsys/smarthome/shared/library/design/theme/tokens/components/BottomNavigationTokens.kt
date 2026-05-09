package network.marsys.smarthome.shared.library.design.theme.tokens.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.color
import network.marsys.smarthome.shared.library.design.theme.tokens.gradient

object BottomNavigationTokens {
    val NavigationBarBackgroundColor: Brush
        @Composable
        get() = SolidColor(color(ColorKeyToken.BackgroundSecondary))

    val NavigationBarBorderColor: Color
        @Composable
        get() = color(ColorKeyToken.BorderPrimary)

    val NavigationItemContentColor: Color
        @Composable
        get() = color(ColorKeyToken.TextDisabled)

    val SelectedNavigationItemColor: Brush
        @Composable
        get() = gradient(GradientKeyToken.BrandPrimaryToSecondary)

    val SelectedNavigationItemContentColor: Color
        @Composable
        get() = color(ColorKeyToken.TextPrimary)

    val SelectedNavigationItemIconColor: Color
        @Composable
        get() = color(ColorKeyToken.ForegroundBrandPrimary)

    val BottomNavigationHeight: Dp
        get() = 92.dp

    val BottomNavigationWidth: Dp
        get() = 410.dp

    val BottomNavigationItemWidth: Dp
        get() = 60.dp
}
