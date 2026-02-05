package network.marsys.smarthome.shared.library.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme

@Composable
fun SmartHomeTheme(
    darkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val scheme = when (darkMode) {
        true -> ColorScheme.darkColorScheme
        else -> ColorScheme.lightColorScheme
    }

    SmartHomeTheme(
        scheme = scheme,
        content = content,
    )
}

@Composable
fun SmartHomeTheme(
    scheme: ColorScheme,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        values = arrayOf(LocalColorScheme provides scheme),
        content = content,
    )
}

object SmartHomeTheme {
    val colors: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current
}
