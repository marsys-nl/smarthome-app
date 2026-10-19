package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.i18n.TranslationKey

data class EntityNaming(
    val key: TranslationKey,
    val qualifier: TranslationKey? = null,
)
