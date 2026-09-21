package network.marsys.smarthome.shared.library.design.domain.preview

import androidx.compose.runtime.Composable
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection

@Composable
fun SmartHomeTheme(
    theme: ThemeSelection,
    content: @Composable () -> Unit,
) = SmartHomeTheme(
    theme = theme,
    translations = DemoPreviewData.translations,
    content = content,
)
