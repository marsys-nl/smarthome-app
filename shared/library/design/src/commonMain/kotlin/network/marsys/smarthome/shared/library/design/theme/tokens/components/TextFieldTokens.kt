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
import network.marsys.smarthome.shared.library.design.theme.tokens.color

object TextFieldTokens {
    val BackgroundColor: Brush
        @Composable
        get() = SolidColor(color(ColorKeyToken.BackgroundSecondary))
    val ContentColor: Color
        @Composable
        get() = color(ColorKeyToken.TextPrimary)
    val PlaceholderColor: Color
        @Composable
        get() = color(ColorKeyToken.TextSecondary)
    val SupportingTextColor: Color
        @Composable
        get() = color(ColorKeyToken.TextSecondary)
    val BorderColor: Color
        @Composable
        get() = color(ColorKeyToken.BorderPrimary)
    val FocusedOutlineColor: Color
        @Composable
        get() = color(ColorKeyToken.ForegroundPrimaryAlternative)
    val DisabledBackgroundColor: Brush
        @Composable
        get() = SolidColor(color(ColorKeyToken.BackgroundDisabled))
    val DisabledContentColor: Color
        @Composable
        get() = color(ColorKeyToken.TextDisabled)

    val TextInputShape: Shape = RoundedCornerShape(6.dp)

    val TextInputHorizontalPadding: Dp = 12.dp
    val TextInputVerticalPadding: Dp = 8.dp
}
