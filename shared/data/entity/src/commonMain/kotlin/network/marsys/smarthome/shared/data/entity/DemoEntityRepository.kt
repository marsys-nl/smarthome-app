package network.marsys.smarthome.shared.data.entity

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.domain.unit.percent
import network.marsys.smarthome.shared.domain.entity.EntityRepository
import network.marsys.smarthome.shared.domain.entity.capability.Brightness
import network.marsys.smarthome.shared.domain.entity.capability.Capability.Companion.optional
import network.marsys.smarthome.shared.domain.entity.capability.Capability.Companion.required
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import network.marsys.smarthome.shared.domain.entity.entity.Blind
import network.marsys.smarthome.shared.domain.entity.entity.Camera
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.Fan
import network.marsys.smarthome.shared.domain.entity.entity.Light
import network.marsys.smarthome.shared.domain.entity.entity.Lock
import network.marsys.smarthome.shared.domain.entity.entity.SmartPlug
import network.marsys.smarthome.shared.domain.entity.entity.Speaker
import network.marsys.smarthome.shared.domain.entity.entity.Thermostat

class DemoEntityRepository(
    seed: List<Entity<*>> = DemoEntities,
) : EntityRepository {
    private val state = MutableStateFlow(seed.associateBy { it.identifier })

    override val entities: Flow<Collection<Entity<*>>> =
        state
            .map { it.values }

    override fun entity(identifier: EntityIdentifier): Flow<Entity<*>?> =
        state
            .map { it[identifier] }
            .distinctUntilChanged()

    override suspend fun execute(action: Entity.Action) = when (action) {
        is Entity.Toggleable.Toggle -> toggle(action)
    }

    private fun toggle(action: Entity.Toggleable.Toggle) {
        val entity = state.value[action.identifier] as? Entity.Toggleable
            ?: return

        state.update {
            it + (action.identifier to entity.toggle())
        }
    }
}

val DemoEntities: List<Entity<*>> = listOf(
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
            onOff = required(OnOff(current = true)),
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
