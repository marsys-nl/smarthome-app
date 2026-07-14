package network.marsys.smarthome.shared.library.design.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken

@Stable
@ConsistentCopyVisibility
data class ColorScheme internal constructor(
    private val backgroundPrimary: Color,
    private val backgroundSecondary: Color,
    private val backgroundSecondarySelected: Color,
    private val backgroundTertiary: Color,
    private val backgroundTertiaryAlternative: Color,
    private val backgroundTertiaryDisabled: Color,
    private val backgroundBrandPrimary: Color,
    private val backgroundBrandSecondary: Color,
    private val backgroundErrorPrimary: Color,
    private val backgroundErrorSecondary: Color,
    private val backgroundErrorSolid: Color,
    private val backgroundErrorSolidPressed: Color,
    private val backgroundWarningPrimary: Color,
    private val backgroundWarningSecondary: Color,
    private val backgroundWarningSolid: Color,
    private val backgroundWarningSolidPressed: Color,
    private val backgroundInfoPrimary: Color,
    private val backgroundInfoSecondary: Color,
    private val backgroundInfoSolid: Color,
    private val backgroundInfoSolidPressed: Color,
    private val backgroundSuccessPrimary: Color,
    private val backgroundSuccessSecondary: Color,
    private val backgroundSuccessSolid: Color,
    private val backgroundSuccessSolidPressed: Color,
    private val backgroundModal: Color,
    private val backgroundDimmed: Color,
    private val backgroundDisabled: Color,
    private val backgroundDisabledAlternative: Color,

    private val borderPrimary: Color,
    private val borderBrandPrimary: Color,
    private val borderBrandPrimaryDimmed: Color,
    private val borderErrorPrimary: Color,
    private val borderErrorSubtle: Color,
    private val borderWarningSubtle: Color,
    private val borderInfoSubtle: Color,
    private val borderSuccessSubtle: Color,

    private val foregroundPrimary: Color,
    private val foregroundPrimaryAlternative: Color,
    private val foregroundSecondary: Color,
    private val foregroundBrandPrimary: Color,
    private val foregroundErrorPrimary: Color,
    private val foregroundWarningPrimary: Color,
    private val foregroundInfoPrimary: Color,
    private val foregroundSuccessPrimary: Color,
    private val foregroundDisabled: Color,

    private val gradientBrandPrimaryToSecondary: Brush,
    private val gradientDimmedPrimaryToSecondary: Brush,

    private val textPrimary: Color,
    private val textSecondary: Color,
    private val textSecondaryAlternative: Color,
    private val textBrandOnBrand: Color,
    private val textErrorPrimary: Color,
    private val textErrorSecondary: Color,
    private val textWarningPrimary: Color,
    private val textWarningSecondary: Color,
    private val textInfoPrimary: Color,
    private val textInfoSecondary: Color,
    private val textSuccessPrimary: Color,
    private val textSuccessSecondary: Color,
    private val textDisabled: Color,
) {
    @Suppress("CyclomaticComplexMethod", "ktlint:standard:blank-line-between-when-conditions")
    operator fun get(token: ColorKeyToken): Color = when (token) {
        ColorKeyToken.BackgroundPrimary -> backgroundPrimary
        ColorKeyToken.BackgroundSecondary -> backgroundSecondary
        ColorKeyToken.BackgroundSecondarySelected -> backgroundSecondarySelected
        ColorKeyToken.BackgroundTertiary -> backgroundTertiary
        ColorKeyToken.BackgroundTertiaryAlternative -> backgroundTertiaryAlternative
        ColorKeyToken.BackgroundTertiaryDisabled -> backgroundTertiaryDisabled

        ColorKeyToken.BackgroundBrandPrimary -> backgroundBrandPrimary
        ColorKeyToken.BackgroundBrandSecondary -> backgroundBrandSecondary

        ColorKeyToken.BackgroundErrorPrimary -> backgroundErrorPrimary
        ColorKeyToken.BackgroundErrorSecondary -> backgroundErrorSecondary
        ColorKeyToken.BackgroundErrorSolid -> backgroundErrorSolid
        ColorKeyToken.BackgroundErrorSolidPressed -> backgroundErrorSolidPressed

        ColorKeyToken.BackgroundWarningPrimary -> backgroundWarningPrimary
        ColorKeyToken.BackgroundWarningSecondary -> backgroundWarningSecondary
        ColorKeyToken.BackgroundWarningSolid -> backgroundWarningSolid
        ColorKeyToken.BackgroundWarningSolidPressed -> backgroundWarningSolidPressed

        ColorKeyToken.BackgroundInfoPrimary -> backgroundInfoPrimary
        ColorKeyToken.BackgroundInfoSecondary -> backgroundInfoSecondary
        ColorKeyToken.BackgroundInfoSolid -> backgroundInfoSolid
        ColorKeyToken.BackgroundInfoSolidPressed -> backgroundInfoSolidPressed

        ColorKeyToken.BackgroundSuccessPrimary -> backgroundSuccessPrimary
        ColorKeyToken.BackgroundSuccessSecondary -> backgroundSuccessSecondary
        ColorKeyToken.BackgroundSuccessSolid -> backgroundSuccessSolid
        ColorKeyToken.BackgroundSuccessSolidPressed -> backgroundSuccessSolidPressed

        ColorKeyToken.BackgroundModal -> backgroundModal
        ColorKeyToken.BackgroundDimmed -> backgroundDimmed
        ColorKeyToken.BackgroundDisabled -> backgroundDisabled
        ColorKeyToken.BackgroundDisabledAlternative -> backgroundDisabledAlternative

        ColorKeyToken.BorderPrimary -> borderPrimary
        ColorKeyToken.BorderBrandPrimary -> borderBrandPrimary
        ColorKeyToken.BorderBrandPrimaryDimmed -> borderBrandPrimaryDimmed

        ColorKeyToken.BorderErrorPrimary -> borderErrorPrimary
        ColorKeyToken.BorderErrorSubtle -> borderErrorSubtle
        ColorKeyToken.BorderWarningSubtle -> borderWarningSubtle
        ColorKeyToken.BorderInfoSubtle -> borderInfoSubtle
        ColorKeyToken.BorderSuccessSubtle -> borderSuccessSubtle

        ColorKeyToken.ForegroundPrimary -> foregroundPrimary
        ColorKeyToken.ForegroundPrimaryAlternative -> foregroundPrimaryAlternative
        ColorKeyToken.ForegroundSecondary -> foregroundSecondary
        ColorKeyToken.ForegroundBrandPrimary -> foregroundBrandPrimary
        ColorKeyToken.ForegroundErrorPrimary -> foregroundErrorPrimary
        ColorKeyToken.ForegroundWarningPrimary -> foregroundWarningPrimary
        ColorKeyToken.ForegroundInfoPrimary -> foregroundInfoPrimary
        ColorKeyToken.ForegroundSuccessPrimary -> foregroundSuccessPrimary
        ColorKeyToken.ForegroundDisabled -> foregroundDisabled

        ColorKeyToken.TextPrimary -> textPrimary
        ColorKeyToken.TextSecondary -> textSecondary
        ColorKeyToken.TextSecondaryAlternative -> textSecondaryAlternative
        ColorKeyToken.TextBrandOnBrand -> textBrandOnBrand
        ColorKeyToken.TextErrorPrimary -> textErrorPrimary
        ColorKeyToken.TextErrorSecondary -> textErrorSecondary
        ColorKeyToken.TextWarningPrimary -> textWarningPrimary
        ColorKeyToken.TextWarningSecondary -> textWarningSecondary
        ColorKeyToken.TextInfoPrimary -> textInfoPrimary
        ColorKeyToken.TextInfoSecondary -> textInfoSecondary
        ColorKeyToken.TextSuccessPrimary -> textSuccessPrimary
        ColorKeyToken.TextSuccessSecondary -> textSuccessSecondary
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
