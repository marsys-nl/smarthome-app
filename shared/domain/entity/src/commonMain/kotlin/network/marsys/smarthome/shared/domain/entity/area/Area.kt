package network.marsys.smarthome.shared.domain.entity.area

import network.marsys.smarthome.domain.EntityIdentifier

data class Area(
    val identifier: EntityIdentifier,
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
