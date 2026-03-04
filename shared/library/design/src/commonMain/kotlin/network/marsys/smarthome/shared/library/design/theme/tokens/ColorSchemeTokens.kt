package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Suppress("ComplexInterface", "PropertyName")
internal interface ColorSchemeTokens {
    val BackgroundPrimary: Color
    val BackgroundSecondary: Color
    val BackgroundTertiary: Color
    val BackgroundBrandPrimary: Color
    val BackgroundBrandSecondary: Color
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
    val TextDisabled: Color
}
