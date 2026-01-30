package network.marsys.smarthome.shared.library.design.theme

import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.buildTheme
import network.marsys.smarthome.shared.library.design.background
import network.marsys.smarthome.shared.library.design.colors
import network.marsys.smarthome.shared.library.design.textOnBackground

val SmartHomeDefaultTheme = buildTheme {
    properties[colors] = mapOf(
        background to Color(0xFFF9FAFC),
        textOnBackground to Color(0xFF020616),
    )

    defaultContentColor = properties[colors][textOnBackground]
}
