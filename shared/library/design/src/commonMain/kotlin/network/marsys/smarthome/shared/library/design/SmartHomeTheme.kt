package network.marsys.smarthome.shared.library.design

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.component.Modal
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.LocalTextStyle
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken

@Composable
fun SmartHomeTheme(
    theme: ThemeSelection,
    content: @Composable () -> Unit,
) {
    val darkMode = when (theme) {
        ThemeSelection.LightMode -> false
        ThemeSelection.DarkMode -> true
        ThemeSelection.SystemDefault -> isSystemInDarkTheme()
    }

    val scheme = when (darkMode) {
        true -> ColorScheme.darkColorScheme
        else -> ColorScheme.lightColorScheme
    }

    CompositionLocalProvider(
        values = arrayOf(
            LocalThemeSelection provides theme,
        ),
    ) {
        SmartHomeTheme(
            scheme = scheme,
            content = content,
        )
    }
}

@Composable
private fun SmartHomeTheme(
    scheme: ColorScheme,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalColorScheme provides scheme,
        LocalContentColor provides scheme[ColorKeyToken.TextPrimary],
        LocalTextStyle provides TextStyle.Default,
    ) {
        Box(
            modifier = Modifier
                .background(LocalColorScheme.current[ColorKeyToken.BackgroundPrimary]),
        ) {
            content.invoke()
        }
    }
}

@Composable
fun SmartHomeComponentPreview(
    theme: ThemeSelection,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    background: @Composable () -> Color = { LocalColorScheme.current[ColorKeyToken.BackgroundPrimary] },
    content: @Composable () -> Unit,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        Column(
            modifier = modifier
                .background(background.invoke())
                .padding(contentPadding),
        ) {
            content.invoke()
        }
    }
}

@Composable
fun SmartHomeModalPreview(
    theme: ThemeSelection,
    modifier: Modifier = Modifier,
    background: @Composable () -> Color = { LocalColorScheme.current[ColorKeyToken.BackgroundPrimary] },
    content: @Composable () -> Unit,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(background.invoke()),
        ) {
            Modal(
                onDismissRequest = {},
            ) {
                content.invoke()
            }
        }
    }
}

private val LocalThemeSelection = compositionLocalOf<ThemeSelection> {
    error("No selected theme provided")
}

object SmartHomeTheme {
    val colors: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val current: ThemeSelection
        @Composable
        @ReadOnlyComposable
        get() = LocalThemeSelection.current
}
