package network.marsys.smarthome.shared.library.design.theme.tokens.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.color

object ButtonTokens {
    val BackgroundColor: Color
        @Composable
        get() = color(ColorKeyToken.BackgroundBrandPrimary)
    val ContentColor: Color
        @Composable
        get() = color(ColorKeyToken.TextPrimary)
    val BorderColor: Color
        @Composable
        get() = Color.Unspecified
    val DisabledBackgroundColor: Color
        @Composable
        get() = color(ColorKeyToken.BackgroundDisabled)
    val DisabledContentColor: Color
        @Composable
        get() = color(ColorKeyToken.TextDisabled)

    val ButtonShape: Shape = RoundedCornerShape(16.dp)

    val ButtonHorizontalPadding: Dp = 16.dp
    val ButtonVerticalPadding: Dp = 24.dp
}
