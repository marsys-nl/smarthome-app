package network.marsys.smarthome.shared.library.design.theme.tokens.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.color
import network.marsys.smarthome.shared.library.design.theme.tokens.gradient

object ButtonTokens {
    val ButtonShape: Shape = RoundedCornerShape(16.dp)

    val ButtonHorizontalPadding: Dp = 16.dp
    val ButtonVerticalPadding: Dp = 16.dp
}

object ButtonColorTokens {
    val BackgroundColor: Brush
        @Composable
        get() = gradient(GradientKeyToken.BrandPrimaryToSecondary)

    val ContentColor: Color
        @Composable
        get() = PaletteTokens.Base.White

    val BorderColor: Color
        @Composable
        get() = Color.Unspecified

    val DisabledBackgroundColor: Brush
        @Composable
        get() = SolidColor(color(ColorKeyToken.BackgroundDisabled))

    val DisabledContentColor: Color
        @Composable
        get() = color(ColorKeyToken.TextDisabled)

    val PressedBackgroundColor: Brush
        @Composable
        get() = gradient(GradientKeyToken.BrandPrimaryToSecondaryPressed)

    val PressedContentColor: Color
        @Composable
        get() = PaletteTokens.Base.White
}

object ErrorButtonTokens {
    val BackgroundColor: Brush
        @Composable
        get() = SolidColor(color(ColorKeyToken.BackgroundErrorSecondary))

    val ContentColor: Color
        @Composable
        get() = color(ColorKeyToken.ForegroundErrorPrimary)

    val BorderColor: Color
        @Composable
        get() = Color.Unspecified

    val DisabledBackgroundColor: Brush
        @Composable
        get() = SolidColor(color(ColorKeyToken.BackgroundDisabled))

    val DisabledContentColor: Color
        @Composable
        get() = color(ColorKeyToken.TextDisabled)

    val PressedBackgroundColor: Brush
        @Composable
        get() = SolidColor(color(ColorKeyToken.BackgroundErrorSecondaryPressed))

    val PressedContentColor: Color
        @Composable
        get() = color(ColorKeyToken.ForegroundErrorPrimary)
}

object InfoButtonTokens {
    val BackgroundColor: Brush
        @Composable
        get() = SolidColor(color(ColorKeyToken.BackgroundInfoSecondary))

    val ContentColor: Color
        @Composable
        get() = color(ColorKeyToken.ForegroundInfoPrimary)

    val BorderColor: Color
        @Composable
        get() = Color.Unspecified

    val DisabledBackgroundColor: Brush
        @Composable
        get() = SolidColor(color(ColorKeyToken.BackgroundDisabled))

    val DisabledContentColor: Color
        @Composable
        get() = color(ColorKeyToken.TextDisabled)

    val PressedBackgroundColor: Brush
        @Composable
        get() = SolidColor(color(ColorKeyToken.BackgroundInfoSecondaryPressed))

    val PressedContentColor: Color
        @Composable
        get() = color(ColorKeyToken.ForegroundInfoPrimary)
}
