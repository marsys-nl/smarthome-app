package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

internal object InvertedPrimaryColorSchemeTokens : ColorSchemeTokens {
    override val Background: Brush = SolidColor(PaletteTokens.Amber.Amber500)
    override val Container: Brush = SolidColor(PaletteTokens.Amber.Amber600.copy(alpha = .3f))
    override val ContainerSubtle: Brush = SolidColor(PaletteTokens.Amber.Amber400)
    override val ContentOnContainer: Color = PaletteTokens.Slate.Slate700
    override val ContentOnContainerEmphasized: Color = PaletteTokens.Slate.Slate800
    override val ContentOnContainerSubtle: Color = PaletteTokens.Slate.Slate600
    override val ContentOnPrimary: Color = PaletteTokens.Base.White
    override val Primary: Color = PaletteTokens.Amber.Amber500
}
