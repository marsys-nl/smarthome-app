package network.marsys.smarthome.shared.library.design.theme

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import network.marsys.smarthome.shared.library.design.ThemeSelection

class ThemeSelectionPreviewParameterProvider : PreviewParameterProvider<ThemeSelection> {
    private val schemes = sequenceOf(
        "Light mode" to ThemeSelection.LightMode,
        "Dark mode" to ThemeSelection.DarkMode,
    )

    override val values: Sequence<ThemeSelection> = schemes
        .map(Pair<String, ThemeSelection>::second)

    override fun getDisplayName(index: Int): String? = schemes
        .map(Pair<String, *>::first)
        .filterIndexed { i, _ -> i == index }
        .firstOrNull() ?: super.getDisplayName(index)
}
