package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme

enum class ColorKeyToken {
    BackgroundPrimary,
    BackgroundSecondary,
    BackgroundTertiary,
    BackgroundBrandPrimary,
    BackgroundBrandSecondary,
    BackgroundDisabled,

    BorderPrimary,

    ForegroundPrimaryAlternative,
    ForegroundDisabled,

    TextPrimary,
    TextDisabled,
}

@Composable
internal fun color(token: ColorKeyToken): Color =
    LocalColorScheme.current[token]
