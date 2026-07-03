package network.marsys.smarthome.shared.data.entity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.domain.unit.percent
import network.marsys.smarthome.shared.domain.entity.EntityRepository
import network.marsys.smarthome.shared.domain.entity.capability.Brightness
import network.marsys.smarthome.shared.domain.entity.capability.Capability.Companion.optional
import network.marsys.smarthome.shared.domain.entity.capability.Capability.Companion.required
import network.marsys.smarthome.shared.domain.entity.capability.ChildLock
import network.marsys.smarthome.shared.domain.entity.capability.MeasureTemperature
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import network.marsys.smarthome.shared.domain.entity.capability.Position
import network.marsys.smarthome.shared.domain.entity.capability.ScheduledMode
import network.marsys.smarthome.shared.domain.entity.capability.TargetTemperature
import network.marsys.smarthome.shared.domain.entity.capability.ThermostatMode
import network.marsys.smarthome.shared.domain.entity.capability.WindowDetection
import network.marsys.smarthome.shared.domain.entity.capability.WritableCapability
import network.marsys.smarthome.shared.domain.entity.entity.Blind
import network.marsys.smarthome.shared.domain.entity.entity.Camera
import network.marsys.smarthome.shared.domain.entity.entity.Curtain
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.Fan
import network.marsys.smarthome.shared.domain.entity.entity.Light
import network.marsys.smarthome.shared.domain.entity.entity.Lock
import network.marsys.smarthome.shared.domain.entity.entity.Shutter
import network.marsys.smarthome.shared.domain.entity.entity.SmartPlug
import network.marsys.smarthome.shared.domain.entity.entity.Speaker
import network.marsys.smarthome.shared.domain.entity.entity.Thermostat
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

class DemoEntityRepository(
    seed: List<Entity<*>> = DemoEntities,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) : EntityRepository {
    private val state = MutableStateFlow(seed.associateBy { it.identifier })

    override val entities: Flow<Collection<Entity<*>>> =
        state
            .map { it.values }

    init {
        initializeObservingSimulations()
    }

    override fun entity(identifier: EntityIdentifier): Flow<Entity<*>?> =
        state
            .map { it[identifier] }
            .distinctUntilChanged()

    override suspend fun execute(action: Entity.Action) {
        val entity = state.value[action.identifier] ?: return

        val capability: WritableCapability<*> = when (action) {
            is Brightness.SetBrightness -> Brightness(current = action.brightness)
            is ChildLock.Toggle.On -> ChildLock(current = true)
            is ChildLock.Toggle.Off -> ChildLock(current = false)
            is OnOff.Toggle.On -> OnOff(current = true)
            is OnOff.Toggle.Off -> OnOff(current = false)
            is ScheduledMode.Toggle.On -> ScheduledMode(current = true)
            is ScheduledMode.Toggle.Off -> ScheduledMode(current = false)
            is TargetTemperature.SetTargetTemperature -> TargetTemperature(current = action.targetTemperature)
            is WindowDetection.Toggle.On -> WindowDetection(current = true)
            is WindowDetection.Toggle.Off -> WindowDetection(current = false)
            else -> return
        }

        val updated = entity.update { it.with(capability) }
        state.update {
            it + (action.identifier to updated)
        }
    }

    /*
     * Simulations
     */

    private fun initializeObservingSimulations() {
        applyThermostatSimulations()
    }

    private fun applyThermostatSimulations() {
        scope.launch {
            while (true) {
                delay(SIMULATION_TICK)

                state.value.values.filterIsInstance<Thermostat>()
                    .forEach { thermostat ->
                        val thermostatState = thermostat.state as? Thermostat.State.Known ?: return@forEach

                        val onOff = thermostatState.onOff.value
                        val measured = thermostatState.temperatures.current.value
                        val target = thermostatState.temperatures.target.value
                        val mode = thermostatState.mode.value

                        if (onOff.current && measured.current != target.current) {
                            val step = randomizer.nextDouble(0.05, 0.15).celsius

                            val adjusted = when {
                                measured.current < target.current && mode.supports(ThermostatMode.Mode.Heat) ->
                                    minOf(measured.current + step, target.current)

                                measured.current > target.current && mode.supports(ThermostatMode.Mode.Cool) ->
                                    maxOf(measured.current - step, target.current)

                                else -> measured.current
                            }

                            val updated = thermostat.update {
                                it.with(
                                    capability = MeasureTemperature(
                                        current = adjusted.coerceIn(target.range),
                                    ),
                                )
                            }

                            state.update {
                                it + (thermostat.identifier to updated)
                            }
                        }
                    }
            }
        }
    }

    companion object {
        private val SIMULATION_TICK = 2.seconds

        private val randomizer = Random(seed = Clock.System.now().epochSeconds)
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
    Thermostat(
        identifier = EntityIdentifier("thermostat.nursery"),
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
    // Covers
    Blind(
        identifier = EntityIdentifier("blind.living-room"),
        state = Blind.State.Known(
            position = required(Position(current = 0.percent)),
        ),
    ),
    Shutter(
        identifier = EntityIdentifier("shutter.main-bedroom"),
        state = Shutter.State.Known(
            position = required(Position(current = 0.percent)),
        ),
    ),
    Curtain(
        identifier = EntityIdentifier("curtain.kitchen"),
        state = Curtain.State.Known(
            position = required(Position(current = 0.percent)),
        ),
    ),
    // Fans
    Fan(
        identifier = EntityIdentifier("fan.bedroom"),
        state = Fan.State.Known(
            onOff = required(OnOff(current = true)),
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
            onOff = required(OnOff(current = true)),
        ),
    ),
    // Locks
    Lock(
        identifier = EntityIdentifier("lock.front-door"),
        state = Lock.State.Known(
            onOff = required(OnOff(current = true)),
        ),
    ),
)
