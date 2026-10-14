package network.marsys.smarthome.shared.library.i18n

import kotlinx.coroutines.flow.StateFlow

interface TranslationRepository {
    val catalog: StateFlow<TranslationCatalog>
}
