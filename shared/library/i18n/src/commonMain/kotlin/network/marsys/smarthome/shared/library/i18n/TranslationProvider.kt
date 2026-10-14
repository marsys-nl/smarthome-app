package network.marsys.smarthome.shared.library.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TranslationProvider(
    repository: TranslationRepository,
    content: @Composable () -> Unit,
) {
    val catalog = repository.catalog
        .collectAsStateWithLifecycle()

    CompositionLocalProvider(
        LocalTranslationCatalog provides catalog.value,
        content = content,
    )
}
