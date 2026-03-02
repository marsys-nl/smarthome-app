package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.ui.graphics.Brush

@Suppress("FunctionName")
object GradientTokens {
    object Amber {
        object Amber400 {
            val ToEmerald400 = ToEmerald400(1f)

            fun ToEmerald400(alpha: Float) = Brush.linearGradient(
                colors = listOf(
                    PaletteTokens.Amber.Amber400.copy(alpha = alpha),
                    PaletteTokens.Emerald.Emerald400.copy(alpha = alpha),
                ),
            )
        }
    }
}
