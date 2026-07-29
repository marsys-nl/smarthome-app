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
import network.marsys.smarthome.shared.domain.entity.zone.Zone

object DashboardScreenEntityData {
    val zones = listOf(
        Zone(
            identifier = EntityIdentifier("zone.bathroom"),
            icon = Zone.Icon.Bathroom,
        ),
        Zone(
            identifier = EntityIdentifier("zone.bedroom"),
            icon = Zone.Icon.Bedroom,
        ),
        Zone(
            identifier = EntityIdentifier("zone.garage"),
            icon = Zone.Icon.Garage,
        ),
        Zone(
            identifier = EntityIdentifier("zone.hallway"),
            icon = Zone.Icon.Other,
        ),
        Zone(
            identifier = EntityIdentifier("zone.kitchen"),
            icon = Zone.Icon.Kitchen,
        ),
        Zone(
            identifier = EntityIdentifier("zone.living-room"),
            icon = Zone.Icon.LivingRoom,
        ),
        Zone(
            identifier = EntityIdentifier("zone.office"),
            icon = Zone.Icon.Office,
        ),
        Zone(
            identifier = EntityIdentifier("zone.nursery"),
            icon = Zone.Icon.Bedroom,
        ),
    )

    val entities: List<Entity<*>> = listOf(
        // Lights
        Light(
            identifier = EntityIdentifier("light.bedroom-lamp"),
            state = Light.State.Known(
                onOff = required(OnOff(current = true)),
                brightness = optional(Brightness(80.percent)),
            ),
            zone = zone("zone.bedroom"),
        ),
        Light(
            identifier = EntityIdentifier("light.kitchen-light"),
            state = Light.State.Known(
                onOff = required(OnOff(current = true)),
            ),
            zone = zone("zone.kitchen"),
        ),
        Light(
            identifier = EntityIdentifier("light.ceiling-light"),
            state = Light.State.Known(
                onOff = required(OnOff(current = false)),
            ),
            zone = zone("zone.hallway"),
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
            zone = zone("zone.office"),
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
            zone = zone("zone.bedroom"),
        ),
        // Plugs
        SmartPlug(
            identifier = EntityIdentifier("plug.office-plug"),
            state = SmartPlug.State.Known(
                onOff = required(OnOff(current = true)),
            ),
            zone = zone("zone.office"),
        ),
        SmartPlug(
            identifier = EntityIdentifier("plug.smart-tv"),
            state = SmartPlug.State.Known(
                onOff = required(OnOff(current = true)),
            ),
            zone = zone("zone.living-room"),
        ),
    )
}

private fun zone(identifier: String): Zone? =
    DashboardScreenEntityData.zones.firstOrNull { it.identifier.value == identifier }
