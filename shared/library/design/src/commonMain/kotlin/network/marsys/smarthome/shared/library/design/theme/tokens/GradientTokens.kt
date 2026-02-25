package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.ui.graphics.Brush

object GradientTokens {
    object Amber {
        object Amber400 {
            val ToEmerald400 = Brush.linearGradient(
                colors = listOf(
                    PaletteTokens.Amber.Amber400,
                    PaletteTokens.Emerald.Emerald400,
                ),
            )
        }
    }
}
