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
    BackgroundSuccessPrimary,
    BackgroundSuccessSecondary,
    BackgroundModal,
    BackgroundDimmed,
    BackgroundDisabled,

    BorderPrimary,
    BorderBrandPrimary,
    BorderBrandPrimaryDimmed,
    BorderSuccessPrimary,

    ForegroundPrimary,
    ForegroundPrimaryAlternative,
    ForegroundBrandPrimary,
    ForegroundSuccessPrimary,
    ForegroundDisabled,

    TextPrimary,
    TextSecondary,
    TextSecondaryAlternative,
    TextBrandOnBrand,
    TextSuccessPrimary,
    TextSuccessSecondary,
    TextDisabled,
}

@Composable
internal fun color(token: ColorKeyToken): Color =
    LocalColorScheme.current[token]
