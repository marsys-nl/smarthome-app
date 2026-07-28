@file:Suppress("StringLiteralDuplication")

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
import network.marsys.smarthome.shared.domain.entity.area.Area
import network.marsys.smarthome.shared.domain.entity.capability.Brightness
import network.marsys.smarthome.shared.domain.entity.capability.Capability
import network.marsys.smarthome.shared.domain.entity.capability.Capability.Companion.optional
import network.marsys.smarthome.shared.domain.entity.capability.Capability.Companion.required
import network.marsys.smarthome.shared.domain.entity.capability.ChildLock
import network.marsys.smarthome.shared.domain.entity.capability.MeasureTemperature
import network.marsys.smarthome.shared.domain.entity.capability.Movement
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import network.marsys.smarthome.shared.domain.entity.capability.Opened
import network.marsys.smarthome.shared.domain.entity.capability.Position
import network.marsys.smarthome.shared.domain.entity.capability.ScheduledMode
import network.marsys.smarthome.shared.domain.entity.capability.TargetTemperature
import network.marsys.smarthome.shared.domain.entity.capability.ThermostatMode
import network.marsys.smarthome.shared.domain.entity.capability.WindowDetection
import network.marsys.smarthome.shared.domain.entity.capability.WritableCapability
import network.marsys.smarthome.shared.domain.entity.entity.Blind
import network.marsys.smarthome.shared.domain.entity.entity.Camera
import network.marsys.smarthome.shared.domain.entity.entity.Cover
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

@Suppress("TooManyFunctions")
class DemoEntityRepository(
    seed: List<Entity<*>> = DemoEntities,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) : EntityRepository {
    private val simulations = mutableMapOf<EntityIdentifier, Entity.Action>()
    private val state = MutableStateFlow(seed.associateBy { it.identifier })

    override val areas: Flow<Collection<Area>> =
        state
            .map { entityMap ->
                entityMap.values
                    .mapNotNull { it.area }
                    .distinct()
            }
            .distinctUntilChanged()

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
            is ChildLock.Toggle -> executeChildLockCapabilityAction(action)
            is Movement.Move -> executeMovementCapabilityAction(action)
            is OnOff.Toggle -> executeOnOffCapabilityAction(action)
            is ScheduledMode.Toggle -> executeScheduledModeCapabilityAction(action)
            is TargetTemperature.SetTargetTemperature -> TargetTemperature(current = action.targetTemperature)
            is WindowDetection.Toggle -> executeWindowDetectionCapabilityAction(action)
            else -> return
        }

        val updated = entity.update { it.with(capability) }
        state.update {
            it + (action.identifier to updated)
        }
    }

    private fun executeChildLockCapabilityAction(action: ChildLock.Toggle) = when (action) {
        is ChildLock.Toggle.On -> ChildLock(current = true)
        is ChildLock.Toggle.Off -> ChildLock(current = false)
    }

    private fun executeMovementCapabilityAction(action: Movement.Move) = when (action) {
        is Movement.Move.Close -> Movement(current = Movement.Direction.Closing)
        is Movement.Move.Open -> Movement(current = Movement.Direction.Opening)
        is Movement.Move.Stop -> Movement(current = Movement.Direction.Idle)
    }.also {
        setupCoverSimulation(action)
    }

    private fun executeOnOffCapabilityAction(action: OnOff.Toggle) = when (action) {
        is OnOff.Toggle.On -> OnOff(current = true)
        is OnOff.Toggle.Off -> OnOff(current = false)
    }

    private fun executeScheduledModeCapabilityAction(action: ScheduledMode.Toggle) = when (action) {
        is ScheduledMode.Toggle.On -> ScheduledMode(current = true)
        is ScheduledMode.Toggle.Off -> ScheduledMode(current = false)
    }

    private fun executeWindowDetectionCapabilityAction(action: WindowDetection.Toggle) = when (action) {
        is WindowDetection.Toggle.On -> WindowDetection(current = true)
        is WindowDetection.Toggle.Off -> WindowDetection(current = false)
    }

    private fun setupCoverSimulation(action: Movement.Move) {
        when (action) {
            is Movement.Move.Stop -> simulations.remove(action.identifier)
            else -> simulations[action.identifier] = action
        }
    }

    /*
     * Simulations
     */

    private fun initializeObservingSimulations() {
        applyThermostatSimulations()
        applyRecurringSimulations()
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
                            val step = randomizer.nextDouble(0.005, 0.02).celsius

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

    private fun applyRecurringSimulations() {
        scope.launch {
            while (true) {
                delay(SIMULATION_TICK)

                simulations.forEach { (_, action) ->
                    val entity = state.value[action.identifier] ?: return@forEach

                    when (entity) {
                        is Cover<*> if action is Movement.Move -> applyCoverSimulation(entity, action)
                    }
                }
            }
        }
    }

    private suspend fun applyCoverSimulation(entity: Cover<*>, action: Movement.Move) {
        val coverState = entity.state as? Cover.State ?: return run {
            simulations.remove(entity.identifier)
        }

        val movement = coverState.control.movement as? Capability.Available<Movement> ?: return run {
            simulations.remove(entity.identifier)
        }

        val capabilities: List<Capability<*>> = when (val control = coverState.control) {
            is Cover.Control.WithPosition -> determineUpdatedCapabilitiesWithPosition(
                movement = movement.value,
                position = control.position.value,
                action = action,
            )

            is Cover.Control.WithoutPosition -> determineUpdatedCapabilitiesWithoutPosition(
                movement = movement.value,
                opened = control.opened.value,
                action = action,
            )
        }

        val updated = entity.update { state ->
            capabilities.fold(state) { accumulator, capability ->
                accumulator.with(capability = capability)
            }
        }

        state.update {
            it + (entity.identifier to updated)
        }

        capabilities.filterIsInstance<Movement>()
            .firstOrNull { it.current == Movement.Direction.Idle }
            ?.let {
                simulations.remove(entity.identifier)
            }
    }

    private fun determineUpdatedCapabilitiesWithPosition(
        movement: Movement,
        position: Position,
        action: Movement.Move,
    ): List<Capability<*>> {
        val updatedPosition = when (action) {
            is Movement.Move.Open -> position.copy(
                current = (position.current + 1.percent)
                    .coerceAtMost(100.percent),
            )

            is Movement.Move.Close -> position.copy(
                current = (position.current - 1.percent)
                    .coerceAtLeast(0.percent),
            )

            is Movement.Move.Stop -> position
        }

        val updatedMovement = when {
            updatedPosition.current == 0.percent || updatedPosition.current == 100.percent ->
                Movement(current = Movement.Direction.Idle)

            else -> movement
        }

        return listOf(updatedPosition, updatedMovement)
    }

    @Suppress("FunctionNameMaxLength")
    private suspend fun determineUpdatedCapabilitiesWithoutPosition(
        movement: Movement,
        opened: Opened,
        action: Movement.Move,
    ): List<Capability<*>> = listOf(
        movement.copy(
            current = Movement.Direction.Idle,
        ),
        opened.copy(
            current = when (action) {
                is Movement.Move.Open -> true
                is Movement.Move.Close -> false
                is Movement.Move.Stop -> opened.current
            },
        ),
    ).also {
        delay(1.seconds)
    }

    companion object {
        private val SIMULATION_TICK = 0.1.seconds

        private val randomizer = Random(seed = Clock.System.now().epochSeconds)
    }
}

val DemoAreas = listOf(
    Area(
        identifier = EntityIdentifier("area.bathroom"),
        icon = Area.Icon.Bathroom,
    ),
    Area(
        identifier = EntityIdentifier("area.bedroom"),
        icon = Area.Icon.Bedroom,
    ),
    Area(
        identifier = EntityIdentifier("area.garage"),
        icon = Area.Icon.Garage,
    ),
    Area(
        identifier = EntityIdentifier("area.hallway"),
        icon = Area.Icon.Other,
    ),
    Area(
        identifier = EntityIdentifier("area.kitchen"),
        icon = Area.Icon.Kitchen,
    ),
    Area(
        identifier = EntityIdentifier("area.living-room"),
        icon = Area.Icon.LivingRoom,
    ),
    Area(
        identifier = EntityIdentifier("area.office"),
        icon = Area.Icon.Office,
    ),
    Area(
        identifier = EntityIdentifier("area.nursery"),
        icon = Area.Icon.Bedroom,
    ),
)

val DemoEntities: List<Entity<*>> = listOf(
    // Lights
    Light(
        identifier = EntityIdentifier("light.bedroom-lamp"),
        state = Light.State.Known(
            onOff = required(OnOff(current = true)),
            brightness = optional(Brightness(80.percent)),
        ),
        area = area("area.bedroom"),
    ),
    Light(
        identifier = EntityIdentifier("light.kitchen-light"),
        state = Light.State.Known(
            onOff = required(OnOff(current = true)),
        ),
        area = area("area.kitchen"),
    ),
    Light(
        identifier = EntityIdentifier("light.ceiling-light"),
        state = Light.State.Known(
            onOff = required(OnOff(current = true)),
        ),
        area = area("area.hallway"),
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
        area = area("area.office"),
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
        area = area("area.bedroom"),
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
        area = area("area.nursery"),
    ),
    // Plugs
    SmartPlug(
        identifier = EntityIdentifier("plug.office-plug"),
        state = SmartPlug.State.Known(
            onOff = required(OnOff(current = true)),
        ),
        area = area("area.office"),
    ),
    SmartPlug(
        identifier = EntityIdentifier("plug.smart-tv"),
        state = SmartPlug.State.Known(
            onOff = required(OnOff(current = true)),
        ),
        area = area("area.living-room"),
    ),
    // Covers
    Blind(
        identifier = EntityIdentifier("blind.living-room"),
        state = Blind.State.Known(
            control = Cover.Control.WithPosition(
                position = required(Position(current = 0.percent)),
                movement = optional(Movement(current = Movement.Direction.Idle)),
            ),
        ),
        area = area("area.living-room"),
    ),
    Blind(
        identifier = EntityIdentifier("blind.office"),
        state = Blind.State.Known(
            control = Cover.Control.WithoutPosition(
                opened = required(Opened(current = false)),
            ),
        ),
        area = area("area.office"),
    ),
    Shutter(
        identifier = EntityIdentifier("shutter.main-bedroom"),
        state = Shutter.State.Known(
            control = Cover.Control.WithPosition(
                position = required(Position(current = 50.percent)),
            ),
        ),
        area = area("area.bedroom"),
    ),
    Curtain(
        identifier = EntityIdentifier("curtain.kitchen"),
        state = Curtain.State.Known(
            control = Cover.Control.WithoutPosition(
                opened = required(Opened(current = true)),
                movement = optional(Movement(current = Movement.Direction.Idle)),
            ),
        ),
        area = area("area.kitchen"),
    ),
    // Fans
    Fan(
        identifier = EntityIdentifier("fan.bedroom"),
        state = Fan.State.Known(
            onOff = required(OnOff(current = true)),
        ),
        area = area("area.bedroom"),
    ),
    // Speakers
    Speaker(
        identifier = EntityIdentifier("speaker.kitchen"),
        state = Speaker.State.Unknown,
        area = area("area.kitchen"),
    ),
    // Cameras
    Camera(
        identifier = EntityIdentifier("camera.front-door"),
        state = Camera.State.Known(
            onOff = required(OnOff(current = true)),
        ),
        area = area("area.hallway"),
    ),
    // Locks
    Lock(
        identifier = EntityIdentifier("lock.front-door"),
        state = Lock.State.Known(
            onOff = required(OnOff(current = true)),
        ),
        area = area("area.hallway"),
    ),
)

private fun area(identifier: String): Area? =
    DemoAreas.firstOrNull { it.identifier.value == identifier }
