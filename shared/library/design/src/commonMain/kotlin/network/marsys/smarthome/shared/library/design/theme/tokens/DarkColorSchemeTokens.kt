package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.ui.graphics.Color

@Suppress("PropertyName", "RedundantSuppression")
internal object DarkColorSchemeTokens : ColorSchemeTokens {

    /**
     * Backgrounds.
     */

    override val BackgroundPrimary: Color = PaletteTokens.Slate.Slate950
    override val BackgroundSecondary: Color = PaletteTokens.Slate.Slate800
        .copy(alpha = .6f)
    override val BackgroundSecondarySelected: Color = PaletteTokens.Amber.Amber500
        .copy(alpha = .1f)
    override val BackgroundTertiary: Color = PaletteTokens.Slate.Slate700
    override val BackgroundTertiaryAlternative: Color = PaletteTokens.Slate.Slate800
    override val BackgroundTertiaryDisabled: Color = PaletteTokens.Slate.Slate700
    override val BackgroundBrandPrimary: Color = PaletteTokens.Amber.Amber500
    override val BackgroundBrandSecondary: Color = PaletteTokens.Emerald.Emerald500

    // Error
    override val BackgroundErrorPrimary: Color = PaletteTokens.Red.Red950
    override val BackgroundErrorSecondary: Color = PaletteTokens.Red.Red900
    override val BackgroundErrorSolid: Color = PaletteTokens.Red.Red600
    override val BackgroundErrorSolidPressed: Color = PaletteTokens.Red.Red500

    // Warning
    override val BackgroundWarningPrimary: Color = PaletteTokens.Yellow.Yellow950
    override val BackgroundWarningSecondary: Color = PaletteTokens.Yellow.Yellow900
    override val BackgroundWarningSolid: Color = PaletteTokens.Yellow.Yellow600
    override val BackgroundWarningSolidPressed: Color = PaletteTokens.Yellow.Yellow500

    // Info
    override val BackgroundInfoPrimary: Color = PaletteTokens.Blue.Blue950
    override val BackgroundInfoSecondary: Color = PaletteTokens.Blue.Blue900
    override val BackgroundInfoSolid: Color = PaletteTokens.Blue.Blue600
    override val BackgroundInfoSolidPressed: Color = PaletteTokens.Blue.Blue500

    // Success
    override val BackgroundSuccessPrimary: Color = PaletteTokens.Emerald.Emerald950
    override val BackgroundSuccessSecondary: Color = PaletteTokens.Emerald.Emerald900
    override val BackgroundSuccessSolid: Color = PaletteTokens.Emerald.Emerald600
    override val BackgroundSuccessSolidPressed: Color = PaletteTokens.Emerald.Emerald500

    override val BackgroundModal: Color = PaletteTokens.Slate.Slate900
    override val BackgroundDisabled: Color = PaletteTokens.Slate.Slate800
    override val BackgroundDisabledAlternative: Color = PaletteTokens.Slate.Slate800
        .copy(alpha = .3f)

    /**
     * Borders.
     */

    override val BorderPrimary: Color = PaletteTokens.Slate.Slate700

    // Brand
    override val BorderBrandPrimary: Color = PaletteTokens.Amber.Amber500
    override val BorderBrandPrimaryDimmed: Color = PaletteTokens.Amber.Amber500
        .copy(alpha = .3f)

    // Semantics
    override val BorderErrorPrimary: Color = PaletteTokens.Red.Red400
    override val BorderErrorSubtle: Color = PaletteTokens.Red.Red800
    override val BorderWarningSubtle: Color = PaletteTokens.Yellow.Yellow800
    override val BorderInfoSubtle: Color = PaletteTokens.Blue.Blue800
    override val BorderSuccessSubtle: Color = PaletteTokens.Emerald.Emerald800

    /**
     * Foregrounds.
     */

    override val ForegroundPrimary: Color = PaletteTokens.Slate.Slate400
    override val ForegroundPrimaryAlternative: Color = PaletteTokens.Base.White
    override val ForegroundSecondary: Color = PaletteTokens.Slate.Slate300

    // Brand
    override val ForegroundBrandPrimary: Color = PaletteTokens.Amber.Amber500

    // Semantics
    override val ForegroundErrorPrimary: Color = PaletteTokens.Red.Red400
    override val ForegroundWarningPrimary: Color = PaletteTokens.Yellow.Yellow400
    override val ForegroundInfoPrimary: Color = PaletteTokens.Blue.Blue400
    override val ForegroundSuccessPrimary: Color = PaletteTokens.Emerald.Emerald400

    override val ForegroundDisabled: Color = PaletteTokens.Slate.Slate700

    /**
     * Texts.
     */

    override val TextPrimary: Color = PaletteTokens.Base.White
    override val TextSecondary: Color = PaletteTokens.Slate.Slate400
    override val TextSecondaryAlternative: Color = PaletteTokens.Slate.Slate400

    // Brand
    override val TextBrandOnBrand: Color = PaletteTokens.Amber.Amber300

    // Semantics
    override val TextErrorPrimary: Color = PaletteTokens.Red.Red500
    override val TextErrorSecondary: Color = PaletteTokens.Red.Red600
    override val TextWarningPrimary: Color = PaletteTokens.Yellow.Yellow500
    override val TextWarningSecondary: Color = PaletteTokens.Yellow.Yellow600
    override val TextInfoPrimary: Color = PaletteTokens.Blue.Blue500
    override val TextInfoSecondary: Color = PaletteTokens.Blue.Blue600
    override val TextSuccessPrimary: Color = PaletteTokens.Emerald.Emerald500
    override val TextSuccessSecondary: Color = PaletteTokens.Emerald.Emerald600

    override val TextDisabled: Color = PaletteTokens.Slate.Slate600
}
