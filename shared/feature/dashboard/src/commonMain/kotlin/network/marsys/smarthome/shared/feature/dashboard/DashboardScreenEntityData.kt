package network.marsys.smarthome.shared.feature.dashboard

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.domain.unit.percent
import network.marsys.smarthome.shared.domain.entity.capability.Brightness
import network.marsys.smarthome.shared.domain.entity.capability.Capability.Companion.optional
import network.marsys.smarthome.shared.domain.entity.capability.Capability.Companion.required
import network.marsys.smarthome.shared.domain.entity.capability.MeasureTemperature
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import network.marsys.smarthome.shared.domain.entity.capability.TargetTemperature
import network.marsys.smarthome.shared.domain.entity.capability.ThermostatMode
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.Light
import network.marsys.smarthome.shared.domain.entity.entity.SmartPlug
import network.marsys.smarthome.shared.domain.entity.entity.Thermostat

object DashboardScreenEntityData {
    val entities: List<Entity<*>> = listOf(
        // Lights
        Light(
            identifier = EntityIdentifier("light.bedroom-lamp"),
            state = Light.State.Known(
                onOff = required(OnOff(current = true)),
                brightness = optional(Brightness(80.percent)),
            ),
        ),
        Light(
            identifier = EntityIdentifier("light.kitchen-light"),
            state = Light.State.Known(
                onOff = required(OnOff(current = true)),
            ),
        ),
        Light(
            identifier = EntityIdentifier("light.ceiling-light"),
            state = Light.State.Known(
                onOff = required(OnOff(current = false)),
            ),
        ),
        // Thermostats
        Thermostat(
            identifier = EntityIdentifier("thermostat.office"),
            state = Thermostat.State.Known(
                onOff = required(OnOff(current = false)),
                mode = required(ThermostatMode(current = ThermostatMode.Mode.Heat)),
                temperatures = Thermostat.Temperatures(
                    current = required(MeasureTemperature(current = 18.celsius)),
                    target = required(TargetTemperature(current = 22.celsius)),
                ),
            ),
        ),
        Thermostat(
            identifier = EntityIdentifier("thermostat.main-bedroom"),
            state = Thermostat.State.Known(
                onOff = required(OnOff(current = true)),
                mode = required(ThermostatMode(current = ThermostatMode.Mode.Auto)),
                temperatures = Thermostat.Temperatures(
                    current = required(MeasureTemperature(current = 18.celsius)),
                    target = required(TargetTemperature(current = 22.celsius)),
                ),
            ),
        ),
        // Plugs
        SmartPlug(
            identifier = EntityIdentifier("plug.office-plug"),
            state = SmartPlug.State.Known(
                onOff = required(OnOff(current = true)),
            ),
        ),
        SmartPlug(
            identifier = EntityIdentifier("plug.smart-tv"),
            state = SmartPlug.State.Known(
                onOff = required(OnOff(current = true)),
            ),
        ),
    )
}
