package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.ui.graphics.Color

@Suppress("PropertyName", "RedundantSuppression")
internal object LightColorSchemeTokens : ColorSchemeTokens {
    override val BackgroundPrimary: Color = PaletteTokens.Slate.Slate50
    override val BackgroundSecondary: Color = PaletteTokens.Base.White
    override val BackgroundSecondarySelected: Color = PaletteTokens.Amber.Amber50
    override val BackgroundTertiary: Color = PaletteTokens.Slate.Slate200
    override val BackgroundTertiaryDisabled: Color = PaletteTokens.Slate.Slate100
    override val BackgroundBrandPrimary: Color = PaletteTokens.Amber.Amber500
    override val BackgroundBrandSecondary: Color = PaletteTokens.Emerald.Emerald500
    override val BackgroundSuccessPrimary: Color = PaletteTokens.Emerald.Emerald50
    override val BackgroundSuccessSecondary: Color = PaletteTokens.Emerald.Emerald100
    override val BackgroundModal: Color = PaletteTokens.Base.White
    override val BackgroundDisabled: Color = PaletteTokens.Slate.Slate100
    override val BorderPrimary: Color = PaletteTokens.Slate.Slate300
    override val BorderBrandPrimary: Color = PaletteTokens.Amber.Amber500
    override val BorderBrandPrimaryDimmed: Color = PaletteTokens.Amber.Amber500
        .copy(alpha = .3f)
    override val BorderSuccessPrimary: Color = PaletteTokens.Emerald.Emerald200
    override val ForegroundPrimary: Color = PaletteTokens.Slate.Slate500
    override val ForegroundPrimaryAlternative: Color = PaletteTokens.Base.White
    override val ForegroundBrandPrimary: Color = PaletteTokens.Amber.Amber500
    override val ForegroundSuccessPrimary: Color = PaletteTokens.Emerald.Emerald600
    override val ForegroundDisabled: Color = PaletteTokens.Slate.Slate50
    override val TextPrimary: Color = PaletteTokens.Slate.Slate800
    override val TextSecondary: Color = PaletteTokens.Slate.Slate500
    override val TextSecondaryAlternative: Color = PaletteTokens.Slate.Slate50
    override val TextBrandOnBrand: Color = PaletteTokens.Amber.Amber700
    override val TextSuccessPrimary: Color = PaletteTokens.Emerald.Emerald600
    override val TextSuccessSecondary: Color = PaletteTokens.Emerald.Emerald700
    override val TextDisabled: Color = PaletteTokens.Slate.Slate400
}
