package network.marsys.smarthome.shared.domain.entity

import network.marsys.smarthome.domain.zone.Zone

val Zone.icon: ZoneIcon
    get() = when (naming.key.value) {
        "bathroom" -> ZoneIcon.Bathroom
        "bedroom" -> ZoneIcon.Bedroom
        "garage" -> ZoneIcon.Garage
        "kitchen" -> ZoneIcon.Kitchen
        "living-room" -> ZoneIcon.LivingRoom
        "office" -> ZoneIcon.Office
        else -> ZoneIcon.Other
    }

sealed interface ZoneIcon {
    data object Bathroom : ZoneIcon
    data object Bedroom : ZoneIcon
    data object Garage : ZoneIcon
    data object Kitchen : ZoneIcon
    data object LivingRoom : ZoneIcon
    data object Office : ZoneIcon
    data object Other : ZoneIcon
}
