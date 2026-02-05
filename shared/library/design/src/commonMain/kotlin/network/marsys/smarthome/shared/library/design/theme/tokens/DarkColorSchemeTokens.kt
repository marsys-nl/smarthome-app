package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

@Suppress("PropertyName")
internal object DarkColorSchemeTokens : ColorSchemeTokens {
    override val Background: Brush = SolidColor(PaletteTokens.Slate.Slate950)
    override val Container: Brush = SolidColor(PaletteTokens.Slate.Slate800.copy(alpha = .8f))
    override val ContainerSubtle: Brush = SolidColor(PaletteTokens.Slate.Slate700)
    override val ContentOnContainer: Color = PaletteTokens.Slate.Slate400
    override val ContentOnContainerEmphasized: Color = PaletteTokens.Base.White
    override val ContentOnContainerSubtle: Color = PaletteTokens.Slate.Slate300
    override val ContentOnPrimary: Color = PaletteTokens.Base.White
    override val Primary: Color = PaletteTokens.Amber.Amber500
}
