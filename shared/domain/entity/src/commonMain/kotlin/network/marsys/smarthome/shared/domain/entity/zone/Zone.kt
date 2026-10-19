package network.marsys.smarthome.shared.domain.entity.zone

import network.marsys.smarthome.domain.i18n.TranslationKey
import network.marsys.smarthome.domain.identifiers.EntityIdentifier

data class Zone(
    val identifier: EntityIdentifier,
    val naming: TranslationKey,
    val icon: Icon,
) {
    sealed interface Icon {
        data object Bathroom : Icon
        data object Bedroom : Icon
        data object Garage : Icon
        data object Kitchen : Icon
        data object LivingRoom : Icon
        data object Office : Icon
        data object Other : Icon
    }
}
