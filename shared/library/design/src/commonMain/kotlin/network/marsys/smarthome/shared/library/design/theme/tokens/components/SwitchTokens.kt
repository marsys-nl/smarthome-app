package network.marsys.smarthome.shared.library.design.theme.tokens.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.color

object SwitchTokens {
    val CheckedTrackColor: Color
        @Composable
        get() = color(ColorKeyToken.BackgroundBrandSecondary)
    val UncheckedTrackColor: Color
        @Composable
        get() = color(ColorKeyToken.BackgroundTertiary)
    val CheckedThumbColor: Color
        @Composable
        get() = color(ColorKeyToken.ForegroundPrimaryAlternative)
    val UncheckedThumbColor: Color
        @Composable
        get() = color(ColorKeyToken.ForegroundPrimaryAlternative)
    val DisabledCheckedTrackColor: Color
        @Composable
        get() = color(ColorKeyToken.BackgroundDisabled)
    val DisabledUncheckedTrackColor: Color
        @Composable
        get() = color(ColorKeyToken.BackgroundDisabled)
    val DisabledCheckedThumbColor: Color
        @Composable
        get() = color(ColorKeyToken.ForegroundDisabled)
    val DisabledUncheckedThumbColor: Color
        @Composable
        get() = color(ColorKeyToken.ForegroundDisabled)

    val TrackWidthRegular: Dp = 48.dp
    val TrackHeightRegular: Dp = 28.dp
    val TrackWidthSmall: Dp = 36.dp
    val TrackHeightSmall: Dp = 20.dp
    val TrackShape: Shape = CircleShape

    val ThumbPaddingRegular: Dp = 4.dp
    val ThumbPaddingSmall: Dp = 2.dp
    val ThumbShadowRadius: Dp = 6.dp
    val ThumbShadowSpread: Dp = -4.dp
    val ThumbShadowOffset: DpOffset = DpOffset(0.dp, 4.dp)
    val ThumbShadowColor: Color = PaletteTokens.Base.Black
    val ThumbShape: Shape = CircleShape
}
