package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.ui.graphics.Color

@Suppress("PropertyName", "RedundantSuppression")
internal object LightColorSchemeTokens : ColorSchemeTokens {

    /**
     * Backgrounds.
     */

    override val BackgroundPrimary: Color = PaletteTokens.Slate.Slate50
    override val BackgroundSecondary: Color = PaletteTokens.Base.White
    override val BackgroundSecondarySelected: Color = PaletteTokens.Amber.Amber50
    override val BackgroundTertiary: Color = PaletteTokens.Slate.Slate200
    override val BackgroundTertiaryAlternative: Color = PaletteTokens.Slate.Slate200
    override val BackgroundTertiaryDisabled: Color = PaletteTokens.Slate.Slate100
    override val BackgroundBrandPrimary: Color = PaletteTokens.Amber.Amber500
    override val BackgroundBrandSecondary: Color = PaletteTokens.Emerald.Emerald500

    // Error
    override val BackgroundErrorPrimary: Color = PaletteTokens.Red.Red100
    override val BackgroundErrorSecondary: Color = PaletteTokens.Red.Red200
    override val BackgroundErrorSolid: Color = PaletteTokens.Red.Red600
    override val BackgroundErrorSolidPressed: Color = PaletteTokens.Red.Red700

    // Warning
    override val BackgroundWarningPrimary: Color = PaletteTokens.Yellow.Yellow100
    override val BackgroundWarningSecondary: Color = PaletteTokens.Yellow.Yellow200
    override val BackgroundWarningSolid: Color = PaletteTokens.Yellow.Yellow600
    override val BackgroundWarningSolidPressed: Color = PaletteTokens.Yellow.Yellow700

    // Info
    override val BackgroundInfoPrimary: Color = PaletteTokens.Blue.Blue100
    override val BackgroundInfoSecondary: Color = PaletteTokens.Blue.Blue200
    override val BackgroundInfoSolid: Color = PaletteTokens.Blue.Blue600
    override val BackgroundInfoSolidPressed: Color = PaletteTokens.Blue.Blue700

    // Success
    override val BackgroundSuccessPrimary: Color = PaletteTokens.Emerald.Emerald100
    override val BackgroundSuccessSecondary: Color = PaletteTokens.Emerald.Emerald200
    override val BackgroundSuccessSolid: Color = PaletteTokens.Emerald.Emerald600
    override val BackgroundSuccessSolidPressed: Color = PaletteTokens.Emerald.Emerald700

    override val BackgroundModal: Color = PaletteTokens.Base.White
    override val BackgroundDisabled: Color = PaletteTokens.Slate.Slate100
    override val BackgroundDisabledAlternative: Color = PaletteTokens.Slate.Slate100
        .copy(alpha = .6f)

    /**
     * Borders.
     */

    override val BorderPrimary: Color = PaletteTokens.Slate.Slate300

    // Brand
    override val BorderBrandPrimary: Color = PaletteTokens.Amber.Amber500
    override val BorderBrandPrimaryDimmed: Color = PaletteTokens.Amber.Amber500
        .copy(alpha = .3f)

    // Semantics
    override val BorderErrorPrimary: Color = PaletteTokens.Red.Red500
    override val BorderErrorSubtle: Color = PaletteTokens.Red.Red300
    override val BorderWarningSubtle: Color = PaletteTokens.Yellow.Yellow300
    override val BorderInfoSubtle: Color = PaletteTokens.Blue.Blue300
    override val BorderSuccessSubtle: Color = PaletteTokens.Emerald.Emerald300

    /**
     * Foregrounds.
     */

    override val ForegroundPrimary: Color = PaletteTokens.Slate.Slate500
    override val ForegroundPrimaryAlternative: Color = PaletteTokens.Base.White
    override val ForegroundSecondary: Color = PaletteTokens.Slate.Slate600

    // Brand
    override val ForegroundBrandPrimary: Color = PaletteTokens.Amber.Amber500

    // Semantics
    override val ForegroundErrorPrimary: Color = PaletteTokens.Red.Red600
    override val ForegroundWarningPrimary: Color = PaletteTokens.Yellow.Yellow600
    override val ForegroundInfoPrimary: Color = PaletteTokens.Blue.Blue600
    override val ForegroundSuccessPrimary: Color = PaletteTokens.Emerald.Emerald600

    override val ForegroundDisabled: Color = PaletteTokens.Slate.Slate50

    /**
     * Texts.
     */

    override val TextPrimary: Color = PaletteTokens.Slate.Slate800
    override val TextSecondary: Color = PaletteTokens.Slate.Slate500
    override val TextSecondaryAlternative: Color = PaletteTokens.Slate.Slate50

    // Brand
    override val TextBrandOnBrand: Color = PaletteTokens.Amber.Amber700

    // Semantics
    override val TextErrorPrimary: Color = PaletteTokens.Red.Red600
    override val TextErrorSecondary: Color = PaletteTokens.Red.Red500
    override val TextWarningPrimary: Color = PaletteTokens.Yellow.Yellow600
    override val TextWarningSecondary: Color = PaletteTokens.Yellow.Yellow500
    override val TextInfoPrimary: Color = PaletteTokens.Blue.Blue600
    override val TextInfoSecondary: Color = PaletteTokens.Blue.Blue500
    override val TextSuccessPrimary: Color = PaletteTokens.Emerald.Emerald600
    override val TextSuccessSecondary: Color = PaletteTokens.Emerald.Emerald500

    override val TextDisabled: Color = PaletteTokens.Slate.Slate400
}
