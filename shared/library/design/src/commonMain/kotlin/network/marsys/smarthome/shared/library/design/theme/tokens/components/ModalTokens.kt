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
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.color

object ModalTokens {
    val OverlayColor: Brush
        @Composable
        get() = SolidColor(PaletteTokens.Base.Black.copy(alpha = 0.5f))
    val ContainerColor: Brush
        @Composable
        get() = SolidColor(color(ColorKeyToken.BackgroundModal))
    val ContentColor: Color
        @Composable
        get() = color(ColorKeyToken.TextPrimary)
    val BorderColor: Color
        @Composable
        get() = Color.Unspecified

    val ModalShape: Shape = RoundedCornerShape(16.dp)

    val ModalHorizontalPadding: Dp = 24.dp
    val ModalVerticalPadding: Dp = 24.dp

    val OverlayHorizontalPadding: Dp = 16.dp
    val OverlayVerticalPadding: Dp = 16.dp
}
