package network.marsys.smarthome.shared.library.i18n

import androidx.compose.runtime.compositionLocalOf

fun interface TranslationCatalog {
    operator fun get(key: String, language: String): String?
}

data object EmptyTranslationCatalog : TranslationCatalog {
    override fun get(key: String, language: String): String? = null
}

val LocalTranslationCatalog = compositionLocalOf<TranslationCatalog> {
    EmptyTranslationCatalog
}
