package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

@Suppress("PropertyName")
internal object LightColorSchemeTokens : ColorSchemeTokens {
    override val Background: Brush = SolidColor(PaletteTokens.Slate.Slate50)
    override val Container: Brush = SolidColor(PaletteTokens.Base.White)
    override val ContainerSubtle: Brush = SolidColor(PaletteTokens.Slate.Slate100)
    override val ContentOnContainer: Color = PaletteTokens.Slate.Slate500
    override val ContentOnContainerEmphasized: Color = PaletteTokens.Slate.Slate800
    override val ContentOnContainerSubtle: Color = PaletteTokens.Slate.Slate400
    override val ContentOnPrimary: Color = PaletteTokens.Base.White
    override val Primary: Color = PaletteTokens.Amber.Amber500
}
