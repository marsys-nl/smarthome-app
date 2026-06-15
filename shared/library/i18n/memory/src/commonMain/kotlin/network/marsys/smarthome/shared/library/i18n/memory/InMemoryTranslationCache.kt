package network.marsys.smarthome.shared.library.i18n.memory

import network.marsys.smarthome.shared.library.i18n.TranslationCache

class InMemoryTranslationCache(
    private val translations: Map<String, Map<String, String>>,
    private val fallback: String = "en",
) : TranslationCache {
    override fun get(key: String, language: String): String? {
        val translation = translations[key] ?: return null
        return translation[language] ?: translation[fallback]
    }
}
