package network.marsys.smarthome.shared.modal.entity

import network.marsys.smarthome.domain.TranslationKey
import network.marsys.smarthome.domain.capability.Brightness
import network.marsys.smarthome.domain.capability.Capability.Companion.optional
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.entity.Entity
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.EntityNaming
import network.marsys.smarthome.domain.entity.Light
import network.marsys.smarthome.domain.entity.Thermostat
import network.marsys.smarthome.domain.unit.percent

object EntityDetailModalPreviewData {
    val loading = object : EntityDetailModalState {
        override val isLoading: Boolean = true
        override val entity: Entity? = null
    }

    val loaded = mapOf(
        Light to object : EntityDetailModalState {
            override val isLoading: Boolean = false
            override val entity: Entity = Light(
                identifier = EntityIdentifier("light.kitchen-light"),
                naming = EntityNaming(
                    key = TranslationKey("light.kitchen"),
                    qualifier = TranslationKey("spots"),
                ),
                state = Light.State.Known(
                    onOff = required(OnOff(current = true)),
                    brightness = optional(Brightness(80.percent)),
                ),
            )
        },

        Thermostat to object : EntityDetailModalState {
            override val isLoading: Boolean = false
            override val entity: Entity = Thermostat(
                identifier = EntityIdentifier("thermostat.living-room"),
                naming = EntityNaming(
                    key = TranslationKey("thermostat.living-room"),
                ),
                state = Thermostat.State.Unknown,
            )
        },
    )
}
