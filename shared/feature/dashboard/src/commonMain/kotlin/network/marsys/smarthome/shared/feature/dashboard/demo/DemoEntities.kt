package network.marsys.smarthome.shared.feature.dashboard.demo

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.domain.unit.percent
import network.marsys.smarthome.shared.domain.entity.entity.Blind
import network.marsys.smarthome.shared.domain.entity.entity.Camera
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.Fan
import network.marsys.smarthome.shared.domain.entity.entity.Light
import network.marsys.smarthome.shared.domain.entity.entity.Lock
import network.marsys.smarthome.shared.domain.entity.entity.SmartPlug
import network.marsys.smarthome.shared.domain.entity.entity.Speaker
import network.marsys.smarthome.shared.domain.entity.entity.Thermostat

val DemoEntityTranslations = mapOf(
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
    "plug.office-plug" to mapOf(
        "en" to "Office",
        "nl" to "Kantoor",
    ),
    "plug.smart-tv" to mapOf(
        "en" to "Smart TV",
    ),
    "blind.living-room" to mapOf(
        "en" to "Living room",
        "nl" to "Woonkamer",
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

internal val DemoEntities: List<Entity<*>> = listOf(
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
    // Blinds
    Blind(
        identifier = EntityIdentifier("blind.living-room"),
        state = Blind.State.Known(
            position = 0.percent,
        ),
    ),
    // Fans
    Fan(
        identifier = EntityIdentifier("fan.bedroom"),
        state = Fan.State.Known(
            isOn = true,
        ),
    ),
    // Speakers
    Speaker(
        identifier = EntityIdentifier("speaker.kitchen"),
        state = Speaker.State.Unknown,
    ),
    // Cameras
    Camera(
        identifier = EntityIdentifier("camera.front-door"),
        state = Camera.State.Known(
            isOn = true,
        ),
    ),
    // Locks
    Lock(
        identifier = EntityIdentifier("lock.front-door"),
        state = Lock.State.Known(
            isOn = true,
        ),
    ),
)
