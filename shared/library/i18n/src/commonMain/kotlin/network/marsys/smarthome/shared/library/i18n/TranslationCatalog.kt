package network.marsys.smarthome.shared.library.i18n

import androidx.compose.runtime.compositionLocalOf

interface TranslationCatalog {
    operator fun get(key: String, language: String): String?
    operator fun get(key: String, category: String, language: String): String?
}

data object EmptyTranslationCatalog : TranslationCatalog {
    override fun get(key: String, language: String): String? = null
    override fun get(key: String, category: String, language: String): String? = null
}

val LocalTranslationCatalog = compositionLocalOf<TranslationCatalog> {
    EmptyTranslationCatalog
}
