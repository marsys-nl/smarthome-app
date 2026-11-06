@file:Suppress("StringLiteralDuplication")

package network.marsys.smarthome.shared.data.demo

import network.marsys.smarthome.domain.TranslationKey
import network.marsys.smarthome.domain.capability.Brightness
import network.marsys.smarthome.domain.capability.Capability.Companion.optional
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.ChildLock
import network.marsys.smarthome.domain.capability.MeasureTemperature
import network.marsys.smarthome.domain.capability.Movement
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.capability.Opened
import network.marsys.smarthome.domain.capability.Position
import network.marsys.smarthome.domain.capability.ScheduledMode
import network.marsys.smarthome.domain.capability.TargetTemperature
import network.marsys.smarthome.domain.capability.ThermostatMode
import network.marsys.smarthome.domain.capability.WindowDetection
import network.marsys.smarthome.domain.entity.Blind
import network.marsys.smarthome.domain.entity.Camera
import network.marsys.smarthome.domain.entity.Cover
import network.marsys.smarthome.domain.entity.Curtain
import network.marsys.smarthome.domain.entity.Entity
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.EntityNaming
import network.marsys.smarthome.domain.entity.Fan
import network.marsys.smarthome.domain.entity.Light
import network.marsys.smarthome.domain.entity.Shutter
import network.marsys.smarthome.domain.entity.SmartLock
import network.marsys.smarthome.domain.entity.SmartPlug
import network.marsys.smarthome.domain.entity.Speaker
import network.marsys.smarthome.domain.entity.Thermostat
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.domain.unit.percent
import network.marsys.smarthome.domain.zone.Zone
import network.marsys.smarthome.domain.zone.ZoneIdentifier
import network.marsys.smarthome.domain.zone.ZoneNaming
import network.marsys.smarthome.shared.library.i18n.TranslationCatalog
import network.marsys.smarthome.shared.library.i18n.memory.InMemoryTranslationCatalog

object DemoData {
    val zones = listOf(
        Zone(
            identifier = ZoneIdentifier("zone.bathroom"),
            naming = ZoneNaming(
                key = TranslationKey("bathroom"),
            ),
        ),
        Zone(
            identifier = ZoneIdentifier("zone.bedroom"),
            naming = ZoneNaming(
                key = TranslationKey("bedroom"),
            ),
        ),
        Zone(
            identifier = ZoneIdentifier("zone.garage"),
            naming = ZoneNaming(
                key = TranslationKey("garage"),
            ),
        ),
        Zone(
            identifier = ZoneIdentifier("zone.hallway"),
            naming = ZoneNaming(
                key = TranslationKey("hallway"),
            ),
        ),
        Zone(
            identifier = ZoneIdentifier("zone.kitchen"),
            naming = ZoneNaming(
                key = TranslationKey("kitchen"),
            ),
        ),
        Zone(
            identifier = ZoneIdentifier("zone.living-room"),
            naming = ZoneNaming(
                key = TranslationKey("living-room"),
            ),
        ),
        Zone(
            identifier = ZoneIdentifier("zone.office"),
            naming = ZoneNaming(
                key = TranslationKey("office"),
            ),
        ),
        Zone(
            identifier = ZoneIdentifier("zone.nursery"),
            naming = ZoneNaming(
                key = TranslationKey("nursery"),
            ),
        ),
    )

    val entities: List<Entity> = listOf(
        // Lights
        Light(
            identifier = EntityIdentifier("light.bedroom-lamp"),
            naming = EntityNaming(
                key = TranslationKey("light.bedroom"),
            ),
            state = Light.State.Known(
                onOff = required(OnOff(current = true)),
                brightness = optional(Brightness(80.percent)),
            ),
            zone = zone("zone.bedroom"),
        ),
        Light(
            identifier = EntityIdentifier("light.kitchen-light"),
            naming = EntityNaming(
                key = TranslationKey("light.kitchen"),
                qualifier = TranslationKey("spots"),
            ),
            state = Light.State.Known(
                onOff = required(OnOff(current = true)),
                brightness = optional(null),
            ),
            zone = zone("zone.kitchen"),
        ),
        Light(
            identifier = EntityIdentifier("light.living-room.ceiling"),
            naming = EntityNaming(
                key = TranslationKey("light.living-room"),
                qualifier = TranslationKey("ceiling"),
            ),
            state = Light.State.Known(
                onOff = required(OnOff(current = true)),
                brightness = optional(null),
            ),
            zone = zone("zone.living-room"),
        ),
        Light(
            identifier = EntityIdentifier("light.hallway.ceiling"),
            naming = EntityNaming(
                key = TranslationKey("light.hallway"),
                qualifier = TranslationKey("ceiling"),
            ),
            state = Light.State.Known(
                onOff = required(OnOff(current = true)),
                brightness = optional(null),
            ),
            zone = zone("zone.hallway"),
        ),
        // Thermostats
        Thermostat(
            identifier = EntityIdentifier("thermostat.office"),
            naming = EntityNaming(
                key = TranslationKey("thermostat.office"),
            ),
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
            naming = EntityNaming(
                key = TranslationKey("thermostat.main-bedroom"),
            ),
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
        Thermostat(
            identifier = EntityIdentifier("thermostat.nursery"),
            naming = EntityNaming(
                key = TranslationKey("thermostat.nursery"),
            ),
            state = Thermostat.State.Known(
                onOff = required(OnOff(current = true)),
                mode = required(
                    value = ThermostatMode(
                        current = ThermostatMode.Mode.Heat,
                        supported = setOf(
                            ThermostatMode.Mode.Heat,
                        ),
                    ),
                ),
                temperatures = Thermostat.Temperatures(
                    current = required(MeasureTemperature(current = 19.celsius)),
                    target = required(TargetTemperature(current = 21.celsius)),
                ),
                childLock = optional(ChildLock(current = true)),
                windowDetection = optional(WindowDetection(current = true)),
                scheduledMode = optional(ScheduledMode(current = false)),
            ),
            zone = zone("zone.nursery"),
        ),
        // Plugs
        SmartPlug(
            identifier = EntityIdentifier("plug.office-plug"),
            naming = EntityNaming(
                key = TranslationKey("plug.office"),
            ),
            state = SmartPlug.State.Known(
                onOff = required(OnOff(current = true)),
            ),
            zone = zone("zone.office"),
        ),
        SmartPlug(
            identifier = EntityIdentifier("plug.smart-tv"),
            naming = EntityNaming(
                key = TranslationKey("plug.tv"),
            ),
            state = SmartPlug.State.Known(
                onOff = required(OnOff(current = true)),
            ),
            zone = zone("zone.living-room"),
        ),
        // Covers
        Blind(
            identifier = EntityIdentifier("blind.living-room"),
            naming = EntityNaming(
                key = TranslationKey("blind.living-room"),
            ),
            state = Blind.State.Known(
                control = Cover.Control.WithPosition(
                    position = required(Position(current = 0.percent)),
                    movement = optional(Movement(current = Movement.Direction.Idle)),
                ),
            ),
            zone = zone("zone.living-room"),
        ),
        Blind(
            identifier = EntityIdentifier("blind.office"),
            naming = EntityNaming(
                key = TranslationKey("blind.office"),
            ),
            state = Blind.State.Known(
                control = Cover.Control.WithoutPosition(
                    opened = required(Opened(current = false)),
                ),
            ),
            zone = zone("zone.office"),
        ),
        Shutter(
            identifier = EntityIdentifier("shutter.main-bedroom"),
            naming = EntityNaming(
                key = TranslationKey("shutter.main-bedroom"),
            ),
            state = Shutter.State.Known(
                control = Cover.Control.WithPosition(
                    position = required(Position(current = 50.percent)),
                ),
            ),
            zone = zone("zone.bedroom"),
        ),
        Curtain(
            identifier = EntityIdentifier("curtain.kitchen"),
            naming = EntityNaming(
                key = TranslationKey("curtain.kitchen"),
            ),
            state = Curtain.State.Known(
                control = Cover.Control.WithoutPosition(
                    opened = required(Opened(current = true)),
                    movement = optional(Movement(current = Movement.Direction.Idle)),
                ),
            ),
            zone = zone("zone.kitchen"),
        ),
        // Fans
        Fan(
            identifier = EntityIdentifier("fan.bedroom"),
            naming = EntityNaming(
                key = TranslationKey("fan.bedroom"),
            ),
            state = Fan.State.Known(
                onOff = required(OnOff(current = true)),
            ),
            zone = zone("zone.bedroom"),
        ),
        // Speakers
        Speaker(
            identifier = EntityIdentifier("speaker.kitchen"),
            naming = EntityNaming(
                key = TranslationKey("speaker.kitchen"),
            ),
            state = Speaker.State.Unknown,
            zone = zone("zone.kitchen"),
        ),
        // Cameras
        Camera(
            identifier = EntityIdentifier("camera.front-door"),
            naming = EntityNaming(
                key = TranslationKey("camera.front-door"),
            ),
            state = Camera.State.Known(
                onOff = required(OnOff(current = true)),
            ),
            zone = zone("zone.hallway"),
        ),
        // Locks
        SmartLock(
            identifier = EntityIdentifier("lock.front-door"),
            naming = EntityNaming(
                key = TranslationKey("lock.front-door"),
            ),
            state = SmartLock.State.Known(
                onOff = required(OnOff(current = true)),
            ),
            zone = zone("zone.hallway"),
        ),
    )

    val translations: TranslationCatalog = InMemoryTranslationCatalog(
        translations = DemoEntityTranslations + ZoneTranslations,
    )

    fun zone(identifier: ZoneIdentifier): Zone =
        zones.first { it.identifier == identifier }

    fun zone(identifier: String): ZoneIdentifier? =
        zones.firstOrNull { it.identifier.value == identifier }
            ?.identifier
}

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
