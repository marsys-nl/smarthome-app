package network.marsys.smarthome.shared.library.design.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.DarkColorSchemeTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.LightColorSchemeTokens

@Stable
@ConsistentCopyVisibility
data class ColorScheme internal constructor(
    private val backgroundPrimary: Color,
    private val backgroundSecondary: Color,
    private val backgroundSecondarySelected: Color,
    private val backgroundTertiary: Color,
    private val backgroundBrandPrimary: Color,
    private val backgroundBrandSecondary: Color,
    private val backgroundModal: Color,
    private val backgroundDisabled: Color,
    private val borderPrimary: Color,
    private val borderBrandPrimary: Color,
    private val borderBrandPrimaryDimmed: Color,
    private val foregroundPrimary: Color,
    private val foregroundPrimaryAlternative: Color,
    private val foregroundBrandPrimary: Color,
    private val foregroundDisabled: Color,
    private val gradientBrandPrimaryToSecondary: Brush,
    private val gradientDimmedPrimaryToSecondary: Brush,
    private val textPrimary: Color,
    private val textSecondary: Color,
    private val textDisabled: Color,
) {
    @Suppress("CyclomaticComplexMethod")
    operator fun get(token: ColorKeyToken): Color = when (token) {
        ColorKeyToken.BackgroundPrimary -> backgroundPrimary
        ColorKeyToken.BackgroundSecondary -> backgroundSecondary
        ColorKeyToken.BackgroundSecondarySelected -> backgroundSecondarySelected
        ColorKeyToken.BackgroundTertiary -> backgroundTertiary
        ColorKeyToken.BackgroundBrandPrimary -> backgroundBrandPrimary
        ColorKeyToken.BackgroundBrandSecondary -> backgroundBrandSecondary
        ColorKeyToken.BackgroundModal -> backgroundModal
        ColorKeyToken.BackgroundDisabled -> backgroundDisabled
        ColorKeyToken.BorderPrimary -> borderPrimary
        ColorKeyToken.BorderBrandPrimary -> borderBrandPrimary
        ColorKeyToken.BorderBrandPrimaryDimmed -> borderBrandPrimaryDimmed
        ColorKeyToken.ForegroundPrimary -> foregroundPrimary
        ColorKeyToken.ForegroundPrimaryAlternative -> foregroundPrimaryAlternative
        ColorKeyToken.ForegroundBrandPrimary -> foregroundBrandPrimary
        ColorKeyToken.ForegroundDisabled -> foregroundDisabled
        ColorKeyToken.TextPrimary -> textPrimary
        ColorKeyToken.TextSecondary -> textSecondary
        ColorKeyToken.TextDisabled -> textDisabled
    }

    operator fun get(token: GradientKeyToken): Brush = when (token) {
        GradientKeyToken.BrandPrimaryToSecondary -> gradientBrandPrimaryToSecondary
        GradientKeyToken.DimmedPrimaryToSecondary -> gradientDimmedPrimaryToSecondary
    }

    companion object {
        val lightColorScheme = lightColorScheme()
        val darkColorScheme = darkColorScheme()
    }
}

val LocalColorScheme = staticCompositionLocalOf<ColorScheme> {
    error("No color scheme provided")
}

val LocalContentColor = staticCompositionLocalOf<Color> {
    error("No content color provided")
}

internal fun darkColorScheme(
    backgroundPrimary: Color = DarkColorSchemeTokens.BackgroundPrimary,
    backgroundSecondary: Color = DarkColorSchemeTokens.BackgroundSecondary,
    backgroundSecondarySelected: Color = DarkColorSchemeTokens.BackgroundSecondarySelected,
    backgroundTertiary: Color = DarkColorSchemeTokens.BackgroundTertiary,
    backgroundBrandPrimary: Color = DarkColorSchemeTokens.BackgroundBrandPrimary,
    backgroundBrandSecondary: Color = DarkColorSchemeTokens.BackgroundBrandSecondary,
    backgroundModal: Color = DarkColorSchemeTokens.BackgroundModal,
    backgroundDisabled: Color = DarkColorSchemeTokens.BackgroundDisabled,
    borderPrimary: Color = DarkColorSchemeTokens.BorderPrimary,
    borderBrandPrimary: Color = DarkColorSchemeTokens.BorderBrandPrimary,
    borderBrandPrimaryDimmed: Color = DarkColorSchemeTokens.BorderBrandPrimaryDimmed,
    foregroundPrimary: Color = DarkColorSchemeTokens.ForegroundPrimary,
    foregroundPrimaryAlternative: Color = DarkColorSchemeTokens.ForegroundPrimaryAlternative,
    foregroundBrandPrimary: Color = DarkColorSchemeTokens.ForegroundBrandPrimary,
    foregroundDisabled: Color = DarkColorSchemeTokens.ForegroundDisabled,
    gradientBrandPrimaryToSecondary: Brush = DarkColorSchemeTokens.GradientBrandPrimaryToSecondary,
    gradientDimmedPrimaryToSecondary: Brush = DarkColorSchemeTokens.GradientDimmedPrimaryToSecondary,
    textPrimary: Color = DarkColorSchemeTokens.TextPrimary,
    textSecondary: Color = DarkColorSchemeTokens.TextSecondary,
    textDisabled: Color = DarkColorSchemeTokens.TextDisabled,
) = ColorScheme(
    backgroundPrimary = backgroundPrimary,
    backgroundSecondary = backgroundSecondary,
    backgroundSecondarySelected = backgroundSecondarySelected,
    backgroundTertiary = backgroundTertiary,
    backgroundBrandPrimary = backgroundBrandPrimary,
    backgroundBrandSecondary = backgroundBrandSecondary,
    backgroundModal = backgroundModal,
    backgroundDisabled = backgroundDisabled,
    borderPrimary = borderPrimary,
    borderBrandPrimary = borderBrandPrimary,
    borderBrandPrimaryDimmed = borderBrandPrimaryDimmed,
    foregroundPrimary = foregroundPrimary,
    foregroundPrimaryAlternative = foregroundPrimaryAlternative,
    foregroundBrandPrimary = foregroundBrandPrimary,
    foregroundDisabled = foregroundDisabled,
    gradientBrandPrimaryToSecondary = gradientBrandPrimaryToSecondary,
    gradientDimmedPrimaryToSecondary = gradientDimmedPrimaryToSecondary,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    textDisabled = textDisabled,
)

internal fun lightColorScheme(
    backgroundPrimary: Color = LightColorSchemeTokens.BackgroundPrimary,
    backgroundSecondary: Color = LightColorSchemeTokens.BackgroundSecondary,
    backgroundSecondarySelected: Color = LightColorSchemeTokens.BackgroundSecondarySelected,
    backgroundTertiary: Color = LightColorSchemeTokens.BackgroundTertiary,
    backgroundBrandPrimary: Color = LightColorSchemeTokens.BackgroundBrandPrimary,
    backgroundBrandSecondary: Color = LightColorSchemeTokens.BackgroundBrandSecondary,
    backgroundModal: Color = LightColorSchemeTokens.BackgroundModal,
    backgroundDisabled: Color = LightColorSchemeTokens.BackgroundDisabled,
    borderPrimary: Color = LightColorSchemeTokens.BorderPrimary,
    borderBrandPrimary: Color = LightColorSchemeTokens.BorderBrandPrimary,
    borderBrandPrimaryDimmed: Color = LightColorSchemeTokens.BorderBrandPrimaryDimmed,
    foregroundPrimary: Color = LightColorSchemeTokens.ForegroundPrimary,
    foregroundPrimaryAlternative: Color = LightColorSchemeTokens.ForegroundPrimaryAlternative,
    foregroundBrandPrimary: Color = LightColorSchemeTokens.ForegroundBrandPrimary,
    foregroundDisabled: Color = LightColorSchemeTokens.ForegroundDisabled,
    gradientBrandPrimaryToSecondary: Brush = LightColorSchemeTokens.GradientBrandPrimaryToSecondary,
    gradientDimmedPrimaryToSecondary: Brush = LightColorSchemeTokens.GradientDimmedPrimaryToSecondary,
    textPrimary: Color = LightColorSchemeTokens.TextPrimary,
    textSecondary: Color = DarkColorSchemeTokens.TextSecondary,
    textDisabled: Color = LightColorSchemeTokens.TextDisabled,
) = ColorScheme(
    backgroundPrimary = backgroundPrimary,
    backgroundSecondary = backgroundSecondary,
    backgroundSecondarySelected = backgroundSecondarySelected,
    backgroundTertiary = backgroundTertiary,
    backgroundBrandPrimary = backgroundBrandPrimary,
    backgroundBrandSecondary = backgroundBrandSecondary,
    backgroundModal = backgroundModal,
    backgroundDisabled = backgroundDisabled,
    borderPrimary = borderPrimary,
    borderBrandPrimary = borderBrandPrimary,
    borderBrandPrimaryDimmed = borderBrandPrimaryDimmed,
    foregroundPrimary = foregroundPrimary,
    foregroundPrimaryAlternative = foregroundPrimaryAlternative,
    foregroundBrandPrimary = foregroundBrandPrimary,
    foregroundDisabled = foregroundDisabled,
    gradientBrandPrimaryToSecondary = gradientBrandPrimaryToSecondary,
    gradientDimmedPrimaryToSecondary = gradientDimmedPrimaryToSecondary,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    textDisabled = textDisabled,
)
