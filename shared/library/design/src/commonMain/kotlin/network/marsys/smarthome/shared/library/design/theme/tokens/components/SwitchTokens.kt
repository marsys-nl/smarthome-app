package network.marsys.smarthome.shared.library.design.theme.tokens.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens

object SwitchTokens {
    val CheckedTrackColor: Color = PaletteTokens.Emerald.Emerald500
    val UncheckedTrackColor: Color = PaletteTokens.Neutral.Neutral200
    val CheckedThumbColor: Color = PaletteTokens.Base.White
    val UncheckedThumbColor: Color = PaletteTokens.Base.White
    val DisabledCheckedTrackColor: Color = PaletteTokens.Neutral.Neutral100
    val DisabledUncheckedTrackColor: Color = PaletteTokens.Neutral.Neutral100
    val DisabledCheckedThumbColor: Color = PaletteTokens.Base.White
    val DisabledUncheckedThumbColor: Color = PaletteTokens.Base.White

    val TrackWidth: Dp = 36.dp
    val TrackHeight: Dp = 20.dp
    val TrackShape: Shape = CircleShape

    val ThumbPadding: Dp = 2.dp
    val ThumbShadowRadius: Dp = 6.dp
    val ThumbShadowSpread: Dp = -4.dp
    val ThumbShadowOffset: DpOffset = DpOffset(0.dp, 4.dp)
    val ThumbShadowColor: Color = PaletteTokens.Base.Black
    val ThumbShape: Shape = CircleShape
}
