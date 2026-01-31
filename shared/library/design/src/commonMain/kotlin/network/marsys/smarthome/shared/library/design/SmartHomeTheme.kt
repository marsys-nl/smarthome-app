package network.marsys.smarthome.shared.library.design

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.ThemeComposable
import com.composeunstyled.theme.ThemeProperty
import com.composeunstyled.theme.ThemeToken
import network.marsys.smarthome.shared.library.design.theme.SmartHomeDefaultTheme

val colors = ThemeProperty<Color>("colors")
val background = ThemeToken<Color>("background")
val textOnBackground = ThemeToken<Color>("textOnBackground")

@Composable
fun SmartHomeTheme(
    theme: ThemeComposable = SmartHomeDefaultTheme,
    content: @Composable () -> Unit,
) {
    theme.invoke(content)
}
