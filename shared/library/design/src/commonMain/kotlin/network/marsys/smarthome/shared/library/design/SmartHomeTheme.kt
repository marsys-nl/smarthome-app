package network.marsys.smarthome.shared.library.design

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken

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
        values = arrayOf(
            LocalColorScheme provides scheme,
            LocalContentColor provides scheme[ColorKeyToken.TextPrimary],
        ),
        content = content,
    )
}

@Composable
fun SmartHomeComponentPreview(
    scheme: ColorScheme,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    background: @Composable () -> Color = { LocalColorScheme.current[ColorKeyToken.BackgroundSecondary] },
    content: @Composable () -> Unit,
) {
    SmartHomeTheme(
        scheme = scheme,
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

object SmartHomeTheme {
    val colors: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current
}
