@file:Suppress("StringLiteralDuplication")

package network.marsys.smarthome.shared.library.design.domain.preview

import network.marsys.smarthome.domain.identifiers.EntityIdentifier
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
import network.marsys.smarthome.shared.library.i18n.TranslationCache
import network.marsys.smarthome.shared.library.i18n.memory.InMemoryTranslationCache

object DemoPreviewData {
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

    val translations: TranslationCache = InMemoryTranslationCache(
        translations = DemoEntityTranslations + ZoneTranslations,
        fallback = "en",
    )
}

private fun zone(identifier: String): Zone? =
    DemoPreviewData.zones.firstOrNull { it.identifier.value == identifier }

private val ZoneTranslations = mapOf(
    "zone.bathroom" to mapOf(
        "en" to "Bathroom",
        "nl" to "Badkamer",
    ),
    "zone.bedroom" to mapOf(
        "en" to "Bedroom",
        "nl" to "Slaapkamer",
    ),
    "zone.garage" to mapOf(
        "en" to "Garage",
        "nl" to "Garage",
    ),
    "zone.hallway" to mapOf(
        "en" to "Hallway",
        "nl" to "Hal",
    ),
    "zone.kitchen" to mapOf(
        "en" to "Kitchen",
        "nl" to "Keuken",
    ),
    "zone.living-room" to mapOf(
        "en" to "Living room",
        "nl" to "Woonkamer",
    ),
    "zone.nursery" to mapOf(
        "en" to "Nursery",
        "nl" to "Babykamer",
    ),
    "zone.office" to mapOf(
        "en" to "Office",
        "nl" to "Kantoor",
    ),
)

private val DemoEntityTranslations = mapOf(
    "light.bedroom-lamp" to mapOf(
        "en" to "Bedroom",
        "nl" to "Slaapkamer",
    ),
    "light.kitchen-light" to mapOf(
        "en" to "Kitchen",
        "nl" to "Keuken",
    ),
    "light.ceiling-light" to mapOf(
        "en" to "Ceiling hallway",
        "nl" to "Plafond hal",
    ),
    "thermostat.office" to mapOf(
        "en" to "Office",
        "nl" to "Kantoor",
    ),
    "thermostat.main-bedroom" to mapOf(
        "en" to "Main bedroom",
        "nl" to "Hoofdslaapkamer",
    ),
    "thermostat.nursery" to mapOf(
        "en" to "Nursery",
        "nl" to "Babykamer",
    ),
    "plug.office-plug" to mapOf(
        "en" to "Office",
        "nl" to "Kantoor",
    ),
    "plug.smart-tv" to mapOf(
        "en" to "Smart TV",
    ),
    "shutter.main-bedroom" to mapOf(
        "en" to "Main bedroom",
        "nl" to "Hoofdslaapkamer",
    ),
    "blind.living-room" to mapOf(
        "en" to "Living room",
        "nl" to "Woonkamer",
    ),
    "blind.office" to mapOf(
        "en" to "Office",
        "nl" to "Kantoor",
    ),
    "curtain.kitchen" to mapOf(
        "en" to "Kitchen",
        "nl" to "Keuken",
    ),
    "fan.bedroom" to mapOf(
        "en" to "Bedroom",
        "nl" to "Slaapkamer",
    ),
    "speaker.kitchen" to mapOf(
        "en" to "Kitchen",
        "nl" to "Keuken",
    ),
    "camera.front-door" to mapOf(
        "en" to "Front door",
        "nl" to "Voordeur",
    ),
    "lock.front-door" to mapOf(
        "en" to "Front door",
        "nl" to "Voordeur",
    ),
)
