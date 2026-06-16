package network.marsys.smarthome.shared.modal.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.percent
import network.marsys.smarthome.shared.domain.entity.entity.Light
import network.marsys.smarthome.shared.domain.entity.entity.Thermostat

object EntityDetailModalPreviewData {
    val loading = EntityDetailModalState.Loading

    val loaded = mapOf(
        Light::class to EntityDetailModalState.Loaded(
            entity = Light(
                identifier = EntityIdentifier("light.kitchen-light"),
                state = Light.State.Known(
                    isOn = true,
                    brightness = 80.percent,
                ),
            ),
        ),

        Thermostat::class to EntityDetailModalState.Loaded(
            entity = Thermostat(
                identifier = EntityIdentifier("thermostat.living-room"),
                state = Thermostat.State.Unknown,
            ),
        ),
    )
}
