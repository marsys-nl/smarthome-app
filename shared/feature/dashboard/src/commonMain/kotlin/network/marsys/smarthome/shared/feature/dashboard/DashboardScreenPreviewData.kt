package network.marsys.smarthome.shared.feature.dashboard

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.domain.unit.percent
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.Light
import network.marsys.smarthome.shared.domain.entity.entity.SmartPlug
import network.marsys.smarthome.shared.domain.entity.entity.Thermostat

object DashboardScreenPreviewData {
    val entities: List<Entity<*>> = listOf(
        // Lights
        Light(
            identifier = EntityIdentifier("light.bedroom-lamp"),
            state = Light.State.Known(
                isOn = true,
                brightness = 80.percent,
            ),
        ),
        Light(
            identifier = EntityIdentifier("light.kitchen-light"),
            state = Light.State.Known(
                isOn = true,
            ),
        ),
        Light(
            identifier = EntityIdentifier("light.ceiling-light"),
            state = Light.State.Known(
                isOn = false,
            ),
        ),
        // Thermostats
        Thermostat(
            identifier = EntityIdentifier("thermostat.office"),
            state = Thermostat.State.Known(
                mode = Thermostat.ThermostatMode.Off,
                temperatures = Thermostat.Temperatures(
                    current = 18.celsius,
                    target = 22.celsius,
                ),
            ),
        ),
        Thermostat(
            identifier = EntityIdentifier("thermostat.main-bedroom"),
            state = Thermostat.State.Known(
                mode = Thermostat.ThermostatMode.Auto,
                temperatures = Thermostat.Temperatures(
                    current = 18.celsius,
                    target = 22.celsius,
                ),
            ),
        ),
        // Plugs
        SmartPlug(
            identifier = EntityIdentifier("plug.office-plug"),
            state = SmartPlug.State.Known(
                isOn = true,
            ),
        ),
        SmartPlug(
            identifier = EntityIdentifier("plug.smart-tv"),
            state = SmartPlug.State.Known(
                isOn = true,
            ),
        ),
    )
}
