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
    val ForegroundDisabled: Color
    val GradientBrandPrimaryToSecondary: Brush get() =
        GradientTokens.Amber.Amber400.ToEmerald400
    val GradientDimmedPrimaryToSecondary: Brush get() =
        GradientTokens.Amber.Amber400.ToEmerald400(alpha = .2f)
    val TextPrimary: Color
    val TextSecondary: Color
    val TextSecondaryAlternative: Color
    val TextDisabled: Color
}
