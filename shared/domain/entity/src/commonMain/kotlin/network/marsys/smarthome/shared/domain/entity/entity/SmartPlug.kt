package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier

data class SmartPlug(
    override val identifier: EntityIdentifier,
    override val label: String,
    override val description: String,
) : Entity
