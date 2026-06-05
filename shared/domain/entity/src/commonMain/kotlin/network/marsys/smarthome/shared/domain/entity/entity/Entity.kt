package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier

interface Entity {
    val identifier: EntityIdentifier
    val label: String
    val description: String
}
