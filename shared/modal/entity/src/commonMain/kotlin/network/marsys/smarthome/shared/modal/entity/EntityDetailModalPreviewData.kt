package network.marsys.smarthome.shared.modal.entity

import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.domain.unit.percent
import network.marsys.smarthome.shared.domain.entity.capability.Brightness
import network.marsys.smarthome.shared.domain.entity.capability.Capability.Companion.optional
import network.marsys.smarthome.shared.domain.entity.capability.Capability.Companion.required
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.Light
import network.marsys.smarthome.shared.domain.entity.entity.Thermostat

object EntityDetailModalPreviewData {
    val loading = object : EntityDetailModalState {
        override val isLoading: Boolean = true
        override val entity: Entity<*>? = null
    }

    val loaded = mapOf(
        Light::class to object : EntityDetailModalState {
            override val isLoading: Boolean = true
            override val entity: Entity<*> = Light(
                identifier = EntityIdentifier("light.kitchen-light"),
                state = Light.State.Known(
                    onOff = required(OnOff(current = true)),
                    brightness = optional(Brightness(80.percent)),
                ),
            )
        },

        Thermostat::class to object : EntityDetailModalState {
            override val isLoading: Boolean = true
            override val entity: Entity<*> = Thermostat(
                identifier = EntityIdentifier("thermostat.living-room"),
                state = Thermostat.State.Unknown,
            )
        },
    )
}
