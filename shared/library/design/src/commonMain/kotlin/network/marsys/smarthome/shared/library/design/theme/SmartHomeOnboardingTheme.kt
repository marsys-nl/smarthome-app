package network.marsys.smarthome.shared.library.design.theme

import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.buildTheme
import network.marsys.smarthome.shared.library.design.background
import network.marsys.smarthome.shared.library.design.colors
import network.marsys.smarthome.shared.library.design.textOnBackground

val SmartHomeOnboardingTheme = buildTheme {
    properties[colors] = mapOf(
        background to Color(0xFFF1BF42),
        textOnBackground to Color(0xFF411C07),
    )

    defaultContentColor = properties[colors][textOnBackground]
}
