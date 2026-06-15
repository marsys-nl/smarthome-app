package network.marsys.smarthome.shared.library.i18n

import androidx.compose.runtime.compositionLocalOf

interface TranslationCache {
    operator fun get(key: String, language: String): String?
}

internal data object EmptyTranslationCache : TranslationCache {
    override fun get(key: String, language: String): String? = null
}

val LocalTranslationCache = compositionLocalOf<TranslationCache> {
    EmptyTranslationCache
}
