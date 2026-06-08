package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Suppress("ComplexInterface", "PropertyName")
internal interface ColorSchemeTokens {
    val BackgroundPrimary: Color
    val BackgroundSecondary: Color
    val BackgroundSecondarySelected: Color
    val BackgroundTertiary: Color
    val BackgroundTertiaryDisabled: Color
    val BackgroundBrandPrimary: Color
    val BackgroundBrandSecondary: Color
    val BackgroundError: Color get() =
        PaletteTokens.Red.Red500.copy(alpha = .15f)
    val BackgroundModal: Color
    val BackgroundDimmed: Color get() =
        PaletteTokens.Base.White.copy(alpha = .2f)
    val BackgroundDisabled: Color
    val BorderPrimary: Color
    val BorderBrandPrimary: Color
    val BorderBrandPrimaryDimmed: Color
    val ForegroundPrimary: Color
    val ForegroundPrimaryAlternative: Color
    val ForegroundBrandPrimary: Color
    val ForegroundError: Color get() =
        PaletteTokens.Red.Red400
    val ForegroundDisabled: Color
    val GradientBrandPrimaryToSecondary: Brush get() =
        GradientTokens.Amber.Amber400.ToEmerald400
    val GradientDimmedPrimaryToSecondary: Brush get() =
        GradientTokens.Amber.Amber400.ToEmerald400(alpha = .2f)
    val TextPrimary: Color
    val TextSecondary: Color
    val TextSecondaryAlternative: Color
    val TextBrandOnBrand: Color
    val TextError: Color get() =
        PaletteTokens.Red.Red400
    val TextDisabled: Color
}
