package network.marsys.smarthome.shared.library.design.theme

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class ColorSchemePreviewParameterProvider : PreviewParameterProvider<ColorScheme> {
    private val schemes = sequenceOf(
        "Light color scheme" to ColorScheme.lightColorScheme,
        "Dark color scheme" to ColorScheme.darkColorScheme,
        "Primary background color scheme" to ColorScheme.invertedPrimaryColorScheme,
    )

    override val values: Sequence<ColorScheme> = schemes
        .map(Pair<String, ColorScheme>::second)

    override fun getDisplayName(index: Int): String? = schemes
        .map(Pair<String, *>::first)
        .filterIndexed { i, _ -> i == index }
        .firstOrNull() ?: super.getDisplayName(index)
}
