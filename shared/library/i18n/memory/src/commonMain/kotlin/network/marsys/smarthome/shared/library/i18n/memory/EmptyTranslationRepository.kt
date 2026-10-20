package network.marsys.smarthome.shared.library.i18n.memory

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import network.marsys.smarthome.shared.library.i18n.EmptyTranslationCatalog
import network.marsys.smarthome.shared.library.i18n.TranslationCatalog
import network.marsys.smarthome.shared.library.i18n.TranslationRepository

internal object EmptyTranslationRepository : TranslationRepository {
    override val catalog: StateFlow<TranslationCatalog> = MutableStateFlow(EmptyTranslationCatalog)
}
