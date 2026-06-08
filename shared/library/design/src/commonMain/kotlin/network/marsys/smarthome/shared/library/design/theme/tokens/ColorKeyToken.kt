package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme

enum class ColorKeyToken {
    BackgroundPrimary,
    BackgroundSecondary,
    BackgroundSecondarySelected,
    BackgroundTertiary,
    BackgroundTertiaryDisabled,
    BackgroundBrandPrimary,
    BackgroundBrandSecondary,
    BackgroundError,
    BackgroundModal,
    BackgroundDimmed,
    BackgroundDisabled,

    BorderPrimary,
    BorderBrandPrimary,
    BorderBrandPrimaryDimmed,

    ForegroundPrimary,
    ForegroundPrimaryAlternative,
    ForegroundBrandPrimary,
    ForegroundError,
    ForegroundDisabled,

    TextPrimary,
    TextSecondary,
    TextSecondaryAlternative,
    TextBrandOnBrand,
    TextError,
    TextDisabled,
}

@Composable
internal fun color(token: ColorKeyToken): Color =
    LocalColorScheme.current[token]
