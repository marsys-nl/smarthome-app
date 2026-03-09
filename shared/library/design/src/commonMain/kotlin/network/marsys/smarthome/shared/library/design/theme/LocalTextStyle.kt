package network.marsys.smarthome.shared.library.design.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

val LocalTextStyle = staticCompositionLocalOf<TextStyle> {
    error("No text style provided")
}
