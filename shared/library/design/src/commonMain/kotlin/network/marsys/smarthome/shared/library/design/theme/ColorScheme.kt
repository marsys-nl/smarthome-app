package network.marsys.smarthome.shared.library.design.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import network.marsys.smarthome.shared.library.design.theme.tokens.DarkColorSchemeTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.InvertedPrimaryColorSchemeTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.LightColorSchemeTokens

@Stable
@ConsistentCopyVisibility
data class ColorScheme internal constructor(
    val background: Brush,
    val container: Brush,
    val containerSubtle: Brush,
    val contentOnContainer: Color,
    val contentOnContainerEmphasized: Color,
    val contentOnContainerSubtle: Color,
    val contentOnPrimary: Color,
    val primary: Color,
) {
    companion object {
        val lightColorScheme = lightColorScheme()
        val darkColorScheme = darkColorScheme()
        val invertedPrimaryColorScheme = invertedPrimaryColorScheme()
    }
}

val LocalColorScheme = staticCompositionLocalOf<ColorScheme> {
    error("No color scheme provided")
}

internal fun darkColorScheme(
    background: Brush = DarkColorSchemeTokens.Background,
    container: Brush = DarkColorSchemeTokens.Container,
    containerSubtle: Brush = DarkColorSchemeTokens.ContainerSubtle,
    contentOnContainer: Color = DarkColorSchemeTokens.ContentOnContainer,
    contentOnContainerEmphasized: Color = DarkColorSchemeTokens.ContentOnContainerEmphasized,
    contentOnContainerSubtle: Color = DarkColorSchemeTokens.ContentOnContainerSubtle,
    contentOnPrimary: Color = DarkColorSchemeTokens.ContentOnPrimary,
    primary: Color = DarkColorSchemeTokens.Primary,
) = ColorScheme(
    background = background,
    container = container,
    containerSubtle = containerSubtle,
    contentOnContainer = contentOnContainer,
    contentOnContainerEmphasized = contentOnContainerEmphasized,
    contentOnContainerSubtle = contentOnContainerSubtle,
    contentOnPrimary = contentOnPrimary,
    primary = primary,
)

internal fun invertedPrimaryColorScheme(
    background: Brush = InvertedPrimaryColorSchemeTokens.Background,
    container: Brush = InvertedPrimaryColorSchemeTokens.Container,
    containerSubtle: Brush = InvertedPrimaryColorSchemeTokens.ContainerSubtle,
    contentOnContainer: Color = InvertedPrimaryColorSchemeTokens.ContentOnContainer,
    contentOnContainerEmphasized: Color = InvertedPrimaryColorSchemeTokens.ContentOnContainerEmphasized,
    contentOnContainerSubtle: Color = InvertedPrimaryColorSchemeTokens.ContentOnContainerSubtle,
    contentOnPrimary: Color = InvertedPrimaryColorSchemeTokens.ContentOnPrimary,
    primary: Color = InvertedPrimaryColorSchemeTokens.Primary,
) = ColorScheme(
    background = background,
    container = container,
    containerSubtle = containerSubtle,
    contentOnContainer = contentOnContainer,
    contentOnContainerEmphasized = contentOnContainerEmphasized,
    contentOnContainerSubtle = contentOnContainerSubtle,
    contentOnPrimary = contentOnPrimary,
    primary = primary,
)

internal fun lightColorScheme(
    background: Brush = LightColorSchemeTokens.Background,
    container: Brush = LightColorSchemeTokens.Container,
    containerSubtle: Brush = LightColorSchemeTokens.ContainerSubtle,
    contentOnContainer: Color = LightColorSchemeTokens.ContentOnContainer,
    contentOnContainerEmphasized: Color = LightColorSchemeTokens.ContentOnContainerEmphasized,
    contentOnContainerSubtle: Color = LightColorSchemeTokens.ContentOnContainerSubtle,
    contentOnPrimary: Color = LightColorSchemeTokens.ContentOnPrimary,
    primary: Color = LightColorSchemeTokens.Primary,
) = ColorScheme(
    background = background,
    container = container,
    containerSubtle = containerSubtle,
    contentOnContainer = contentOnContainer,
    contentOnContainerEmphasized = contentOnContainerEmphasized,
    contentOnContainerSubtle = contentOnContainerSubtle,
    contentOnPrimary = contentOnPrimary,
    primary = primary,
)
