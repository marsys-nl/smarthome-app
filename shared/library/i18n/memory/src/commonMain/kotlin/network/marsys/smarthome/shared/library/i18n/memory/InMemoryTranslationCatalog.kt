package network.marsys.smarthome.shared.library.i18n.memory

import network.marsys.smarthome.shared.library.i18n.TranslationCatalog

class InMemoryTranslationCatalog(
    private val translations: Map<String, Map<String, String>>,
) : TranslationCatalog {
    override fun get(key: String, language: String): String? =
        translations[key]?.get(language)

    override fun get(key: String, category: String, language: String): String? =
        translations["$key.$category"]?.get(language)
            ?: translations["$key.other"]?.get(language)
}
