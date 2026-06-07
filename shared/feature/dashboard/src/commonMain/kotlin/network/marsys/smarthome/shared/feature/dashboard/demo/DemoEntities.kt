package network.marsys.smarthome.shared.feature.dashboard.demo

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Blind
import network.marsys.smarthome.shared.domain.entity.entity.Camera
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.Fan
import network.marsys.smarthome.shared.domain.entity.entity.Light
import network.marsys.smarthome.shared.domain.entity.entity.Lock
import network.marsys.smarthome.shared.domain.entity.entity.SmartPlug
import network.marsys.smarthome.shared.domain.entity.entity.Speaker
import network.marsys.smarthome.shared.domain.entity.entity.Thermostat

internal val DemoEntities: List<Entity> = listOf(
    // Lights
    Light(
        identifier = EntityIdentifier("light.bedroom-lamp"),
        label = "Bedroom lamp",
        description = "69% brightness",
    ),
    Light(
        identifier = EntityIdentifier("light.kitchen-light"),
        label = "Kitchen light",
        description = "55% brightness",
    ),
    Light(
        identifier = EntityIdentifier("light.ceiling-light"),
        label = "Ceiling light",
        description = "Off",
    ),
    // Thermostats
    Thermostat(
        identifier = EntityIdentifier("thermostat.office"),
        label = "Office",
        description = "18°C",
    ),
    Thermostat(
        identifier = EntityIdentifier("thermostat.main-bedroom"),
        label = "Main bedroom",
        description = "18°C • Heating",
    ),
    // Plugs
    SmartPlug(
        identifier = EntityIdentifier("plug.office-plug"),
        label = "Office plug",
        description = "On",
    ),
    SmartPlug(
        identifier = EntityIdentifier("plug.smart-tv"),
        label = "Smart TV",
        description = "On",
    ),
    // Blinds
    Blind(
        identifier = EntityIdentifier("blind.living-room"),
        label = "Living room blind",
        description = "Closed",
    ),
    // Fans
    Fan(
        identifier = EntityIdentifier("fan.bedroom"),
        label = "Bedroom fan",
        description = "Medium",
    ),
    // Speakers
    Speaker(
        identifier = EntityIdentifier("speaker.kitchen"),
        label = "Kitchen speaker",
        description = "Paused",
    ),
    // Cameras
    Camera(
        identifier = EntityIdentifier("camera.front-door"),
        label = "Front door camera",
        description = "Streaming",
    ),
    // Locks
    Lock(
        identifier = EntityIdentifier("lock.front-door"),
        label = "Front door lock",
        description = "Locked",
    ),
)
