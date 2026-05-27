package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.ui.graphics.Color

@Suppress("PropertyName", "RedundantSuppression")
internal object DarkColorSchemeTokens : ColorSchemeTokens {
    override val BackgroundPrimary: Color = PaletteTokens.Slate.Slate950
    override val BackgroundSecondary: Color = PaletteTokens.Slate.Slate800
        .copy(alpha = .6f)
    override val BackgroundSecondarySelected: Color = PaletteTokens.Amber.Amber500
        .copy(alpha = .1f)
    override val BackgroundTertiary: Color = PaletteTokens.Slate.Slate700
    override val BackgroundTertiaryDisabled: Color = PaletteTokens.Slate.Slate700
    override val BackgroundBrandPrimary: Color = PaletteTokens.Amber.Amber500
    override val BackgroundBrandSecondary: Color = PaletteTokens.Emerald.Emerald500
    override val BackgroundModal: Color = PaletteTokens.Slate.Slate900
    override val BackgroundDisabled: Color = PaletteTokens.Slate.Slate800
    override val BorderPrimary: Color = PaletteTokens.Slate.Slate700
    override val BorderBrandPrimary: Color = PaletteTokens.Amber.Amber500
    override val BorderBrandPrimaryDimmed: Color = PaletteTokens.Amber.Amber500
        .copy(alpha = .3f)
    override val ForegroundPrimary: Color = PaletteTokens.Slate.Slate400
    override val ForegroundPrimaryAlternative: Color = PaletteTokens.Base.White
    override val ForegroundBrandPrimary: Color = PaletteTokens.Amber.Amber500
    override val ForegroundDisabled: Color = PaletteTokens.Slate.Slate700
    override val TextPrimary: Color = PaletteTokens.Base.White
    override val TextSecondary: Color = PaletteTokens.Slate.Slate400
    override val TextSecondaryAlternative: Color = PaletteTokens.Slate.Slate400
    override val TextBrandOnBrand: Color = PaletteTokens.Amber.Amber300
    override val TextDisabled: Color = PaletteTokens.Slate.Slate600
}
