package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Suppress("FunctionName")
object GradientTokens {
    object Amber {
        object Amber400 {
            val ToEmerald400 = linearGradient(
                from = PaletteTokens.Amber.Amber400,
                to = PaletteTokens.Emerald.Emerald400,
            )

            val ToOrange500 = linearGradient(
                from = PaletteTokens.Amber.Amber400,
                to = PaletteTokens.Orange.Orange500,
            )

            fun ToEmerald400(alpha: Float) = linearGradient(
                from = PaletteTokens.Amber.Amber400,
                to = PaletteTokens.Emerald.Emerald400,
                alpha = alpha,
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

    object Emerald {
        object Emerald400 {
            val ToTeal600 = linearGradient(
                from = PaletteTokens.Emerald.Emerald400,
                to = PaletteTokens.Teal.Teal600,
            )
        }
    }

    object Green {
        object Green400 {
            val ToEmerald600 = linearGradient(
                from = PaletteTokens.Green.Green400,
                to = PaletteTokens.Emerald.Emerald600,
            )
        }
    }

    object Indigo {
        object Indigo400 {
            val ToPurple600 = linearGradient(
                from = PaletteTokens.Indigo.Indigo400,
                to = PaletteTokens.Purple.Purple600,
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

    private fun linearGradient(
        from: Color,
        to: Color,
        alpha: Float = 1f,
    ) = Brush.linearGradient(
        colors = listOf(
            from.copy(alpha = alpha),
            to.copy(alpha = alpha),
        ),
    )
}
