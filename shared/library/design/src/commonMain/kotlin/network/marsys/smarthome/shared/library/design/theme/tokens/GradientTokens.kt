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

    object Blue {
        object Blue400 {
            val ToBlue600 = ToBlue600(1f)

            fun ToBlue600(alpha: Float) = Brush.linearGradient(
                colors = listOf(
                    PaletteTokens.Blue.Blue400.copy(alpha = alpha),
                    PaletteTokens.Blue.Blue600.copy(alpha = alpha),
                ),
            )
        }
    }

    object Rose {
        object Rose400 {
            val ToRose600 = ToRose600(1f)

            fun ToRose600(alpha: Float) = Brush.linearGradient(
                colors = listOf(
                    PaletteTokens.Rose.Rose400.copy(alpha = alpha),
                    PaletteTokens.Rose.Rose600.copy(alpha = alpha),
                ),
            )
        }
    }
}
