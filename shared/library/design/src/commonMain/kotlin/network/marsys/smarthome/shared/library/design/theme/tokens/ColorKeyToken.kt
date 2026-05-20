package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme

enum class ColorKeyToken {
    BackgroundPrimary,
    BackgroundSecondary,
    BackgroundSecondarySelected,
    BackgroundTertiary,
    BackgroundBrandPrimary,
    BackgroundBrandSecondary,
    BackgroundModal,
    BackgroundDisabled,

    BorderPrimary,
    BorderBrandPrimary,
    BorderBrandPrimaryDimmed,

    ForegroundPrimary,
    ForegroundPrimaryAlternative,
    ForegroundBrandPrimary,
    ForegroundDisabled,

    TextPrimary,
    TextSecondary,
    TextDisabled,
}

@Composable
internal fun color(token: ColorKeyToken): Color =
    LocalColorScheme.current[token]
