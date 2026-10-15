package network.marsys.smarthome.shared.library.design

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import network.marsys.smarthome.shared.library.design.component.Modal
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.LocalTextStyle
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.i18n.EmptyTranslationCatalog
import network.marsys.smarthome.shared.library.i18n.LocalDescriptorStrings
import network.marsys.smarthome.shared.library.i18n.TranslationCatalog
import network.marsys.smarthome.shared.library.i18n.TranslationProvider
import network.marsys.smarthome.shared.library.i18n.TranslationRepository
import network.marsys.smarthome.shared.library.i18n.rememberDescriptorStrings

@Composable
fun SmartHomeTheme(
    theme: ThemeSelection,
    translations: TranslationCatalog = EmptyTranslationCatalog,
    content: @Composable () -> Unit,
) {
    val descriptorStrings = rememberDescriptorStrings()

    val darkMode = when (theme) {
        ThemeSelection.LightMode -> false
        ThemeSelection.DarkMode -> true
        ThemeSelection.SystemDefault -> isSystemInDarkTheme()
    }

    val scheme = when (darkMode) {
        true -> ColorScheme.darkColorScheme
        else -> ColorScheme.lightColorScheme
    }

    TranslationProvider(
        repository = object : TranslationRepository {
            override val catalog: StateFlow<TranslationCatalog>
                get() = MutableStateFlow(translations)
        },
    ) {
        CompositionLocalProvider(
            values = arrayOf(
                LocalDarkMode provides darkMode,
                LocalDescriptorStrings provides descriptorStrings,
                LocalIndication provides object : Indication {},
                LocalThemeSelection provides theme,
            ),
        ) {
            SmartHomeTheme(
                scheme = scheme,
                content = content,
            )
        }
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
    translations: TranslationCatalog = EmptyTranslationCatalog,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    background: @Composable () -> Color = { LocalColorScheme.current[ColorKeyToken.BackgroundPrimary] },
    content: @Composable () -> Unit,
) {
    SmartHomeTheme(
        theme = theme,
        translations = translations,
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
    translations: TranslationCatalog = EmptyTranslationCatalog,
    background: @Composable () -> Color = { LocalColorScheme.current[ColorKeyToken.BackgroundPrimary] },
    content: @Composable () -> Unit,
) {
    SmartHomeTheme(
        theme = theme,
        translations = translations,
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

val LocalDarkMode = compositionLocalOf<Boolean> {
    error("No dark mode value provided")
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
