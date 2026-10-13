package network.marsys.smarthome.shared.library.i18n

import androidx.compose.runtime.compositionLocalOf

interface TranslationSource {
    operator fun get(key: String, language: String): String?
}

data object EmptyTranslationSource : TranslationSource {
    override fun get(key: String, language: String): String? = null
}

val LocalTranslationSource = compositionLocalOf<TranslationSource> {
    EmptyTranslationSource
}
