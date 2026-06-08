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

internal val DemoEntities: List<Entity<*>> = listOf(
    // Lights
    Light(
        identifier = EntityIdentifier("light.bedroom-lamp"),
        label = "Bedroom lamp",
        state = Light.State.Known(
            isOn = true,
            brightness = 80.percent,
        ),
    ),
    Light(
        identifier = EntityIdentifier("light.kitchen-light"),
        label = "Kitchen light",
        state = Light.State.Known(
            isOn = true,
        ),
    ),
    Light(
        identifier = EntityIdentifier("light.ceiling-light"),
        label = "Ceiling light",
        state = Light.State.Known(
            isOn = false,
        ),
    ),
    // Thermostats
    Thermostat(
        identifier = EntityIdentifier("thermostat.office"),
        label = "Office",
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
        label = "Main bedroom",
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
        label = "Office plug",
        state = SmartPlug.State.Known(
            isOn = true,
        ),
    ),
    SmartPlug(
        identifier = EntityIdentifier("plug.smart-tv"),
        label = "Smart TV",
        state = SmartPlug.State.Known(
            isOn = true,
        ),
    ),
    // Blinds
    Blind(
        identifier = EntityIdentifier("blind.living-room"),
        label = "Living room blind",
        state = Blind.State.Known(
            position = 0.percent,
        ),
    ),
    // Fans
    Fan(
        identifier = EntityIdentifier("fan.bedroom"),
        label = "Bedroom fan",
        state = Fan.State.Known(
            isOn = true,
        ),
    ),
    // Speakers
    Speaker(
        identifier = EntityIdentifier("speaker.kitchen"),
        label = "Kitchen speaker",
        state = Speaker.State.Known(
            isOn = true,
        ),
    ),
    // Cameras
    Camera(
        identifier = EntityIdentifier("camera.front-door"),
        label = "Front door camera",
        state = Camera.State.Known(
            isOn = true,
        ),
    ),
    // Locks
    Lock(
        identifier = EntityIdentifier("lock.front-door"),
        label = "Front door lock",
        state = Lock.State.Known(
            isOn = true,
        ),
    ),
)
