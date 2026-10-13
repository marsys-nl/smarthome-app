package network.marsys.smarthome.shared.library.i18n.memory

import network.marsys.smarthome.shared.library.i18n.TranslationSource

class InMemoryTranslationSource(
    private val translations: Map<String, Map<String, String>>,
) : TranslationSource {
    override fun get(key: String, language: String): String? =
        translations[key]?.get(language)
}
