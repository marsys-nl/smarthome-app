package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme

enum class GradientKeyToken {
    BrandPrimaryToSecondary,
    BrandPrimaryToSecondaryPressed,
    DimmedPrimaryToSecondary,
}

@Composable
internal fun gradient(token: GradientKeyToken): Brush =
    LocalColorScheme.current[token]
