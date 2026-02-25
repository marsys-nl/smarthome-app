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
    private val backgroundTertiary: Color,
    private val backgroundBrandPrimary: Color,
    private val backgroundBrandSecondary: Color,
    private val backgroundDisabled: Color,
    private val borderPrimary: Color,
    private val foregroundPrimaryAlternative: Color,
    private val foregroundDisabled: Color,
    private val gradientBrandPrimaryToSecondary: Brush,
    private val textPrimary: Color,
    private val textDisabled: Color,
) {
    operator fun get(token: ColorKeyToken): Color = when (token) {
        ColorKeyToken.BackgroundPrimary -> backgroundPrimary
        ColorKeyToken.BackgroundSecondary -> backgroundSecondary
        ColorKeyToken.BackgroundTertiary -> backgroundTertiary
        ColorKeyToken.BackgroundBrandPrimary -> backgroundBrandPrimary
        ColorKeyToken.BackgroundBrandSecondary -> backgroundBrandSecondary
        ColorKeyToken.BackgroundDisabled -> backgroundDisabled
        ColorKeyToken.BorderPrimary -> borderPrimary
        ColorKeyToken.ForegroundPrimaryAlternative -> foregroundPrimaryAlternative
        ColorKeyToken.ForegroundDisabled -> foregroundDisabled
        ColorKeyToken.TextPrimary -> textPrimary
        ColorKeyToken.TextDisabled -> textDisabled
    }

    operator fun get(token: GradientKeyToken): Brush = when (token) {
        GradientKeyToken.BrandPrimaryToSecondary -> gradientBrandPrimaryToSecondary
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
    backgroundTertiary: Color = DarkColorSchemeTokens.BackgroundTertiary,
    backgroundBrandPrimary: Color = DarkColorSchemeTokens.BackgroundBrandPrimary,
    backgroundBrandSecondary: Color = DarkColorSchemeTokens.BackgroundBrandSecondary,
    backgroundDisabled: Color = DarkColorSchemeTokens.BackgroundDisabled,
    borderPrimary: Color = DarkColorSchemeTokens.BorderPrimary,
    foregroundPrimaryAlternative: Color = DarkColorSchemeTokens.ForegroundPrimaryAlternative,
    foregroundDisabled: Color = DarkColorSchemeTokens.ForegroundDisabled,
    gradientBrandPrimaryToSecondary: Brush = DarkColorSchemeTokens.GradientBrandPrimaryToSecondary,
    textPrimary: Color = DarkColorSchemeTokens.TextPrimary,
    textDisabled: Color = DarkColorSchemeTokens.TextDisabled,
) = ColorScheme(
    backgroundPrimary = backgroundPrimary,
    backgroundSecondary = backgroundSecondary,
    backgroundTertiary = backgroundTertiary,
    backgroundBrandPrimary = backgroundBrandPrimary,
    backgroundBrandSecondary = backgroundBrandSecondary,
    backgroundDisabled = backgroundDisabled,
    borderPrimary = borderPrimary,
    foregroundPrimaryAlternative = foregroundPrimaryAlternative,
    foregroundDisabled = foregroundDisabled,
    gradientBrandPrimaryToSecondary = gradientBrandPrimaryToSecondary,
    textPrimary = textPrimary,
    textDisabled = textDisabled,
)

internal fun lightColorScheme(
    backgroundPrimary: Color = LightColorSchemeTokens.BackgroundPrimary,
    backgroundSecondary: Color = LightColorSchemeTokens.BackgroundSecondary,
    backgroundTertiary: Color = LightColorSchemeTokens.BackgroundTertiary,
    backgroundBrandPrimary: Color = LightColorSchemeTokens.BackgroundBrandPrimary,
    backgroundBrandSecondary: Color = LightColorSchemeTokens.BackgroundBrandSecondary,
    backgroundDisabled: Color = LightColorSchemeTokens.BackgroundDisabled,
    borderPrimary: Color = LightColorSchemeTokens.BorderPrimary,
    foregroundPrimaryAlternative: Color = LightColorSchemeTokens.ForegroundPrimaryAlternative,
    foregroundDisabled: Color = LightColorSchemeTokens.ForegroundDisabled,
    gradientBrandPrimaryToSecondary: Brush = LightColorSchemeTokens.GradientBrandPrimaryToSecondary,
    textPrimary: Color = LightColorSchemeTokens.TextPrimary,
    textDisabled: Color = LightColorSchemeTokens.TextDisabled,
) = ColorScheme(
    backgroundPrimary = backgroundPrimary,
    backgroundSecondary = backgroundSecondary,
    backgroundTertiary = backgroundTertiary,
    backgroundBrandPrimary = backgroundBrandPrimary,
    backgroundBrandSecondary = backgroundBrandSecondary,
    backgroundDisabled = backgroundDisabled,
    borderPrimary = borderPrimary,
    foregroundPrimaryAlternative = foregroundPrimaryAlternative,
    foregroundDisabled = foregroundDisabled,
    gradientBrandPrimaryToSecondary = gradientBrandPrimaryToSecondary,
    textPrimary = textPrimary,
    textDisabled = textDisabled,
)
