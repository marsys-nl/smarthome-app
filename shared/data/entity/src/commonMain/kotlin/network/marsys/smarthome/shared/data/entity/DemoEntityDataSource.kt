@file:Suppress("StringLiteralDuplication")

package network.marsys.smarthome.shared.data.entity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.domain.unit.percent
import network.marsys.smarthome.shared.data.demo.DemoData
import network.marsys.smarthome.shared.data.entity.ports.outbound.EntityDataSource
import network.marsys.smarthome.shared.domain.entity.capability.Brightness
import network.marsys.smarthome.shared.domain.entity.capability.Capability
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
import network.marsys.smarthome.shared.domain.entity.entity.Cover
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.Thermostat
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

@Suppress("TooManyFunctions")
class DemoEntityDataSource(
    seed: List<Entity<*>> = DemoData.entities,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) : EntityDataSource {
    private val simulations = mutableMapOf<EntityIdentifier, Entity.Action>()

    override val entities: StateFlow<Collection<Entity<*>>>
        field = MutableStateFlow(value = seed)

    init {
        initializeObservingSimulations()
    }

    override fun entity(identifier: EntityIdentifier): Flow<Entity<*>?> =
        entities
            .map { collection ->
                collection
                    .firstOrNull { entity -> entity.identifier == identifier }
            }
            .distinctUntilChanged()

    override suspend fun execute(action: Entity.Action) {
        val entity = entities.value.firstOrNull { it.identifier == action.identifier } ?: return

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

        entities.replace(
            replacement = entity.update {
                it.with(capability)
            },
        )
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

                entities.value.filterIsInstance<Thermostat>()
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

                            entities.replace(
                                replacement = thermostat.update {
                                    it.with(
                                        capability = MeasureTemperature(
                                            current = adjusted.coerceIn(target.range),
                                        ),
                                    )
                                },
                            )
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
                    val entity = entities.value.firstOrNull { it.identifier == action.identifier } ?: return@forEach

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

        entities.replace(
            replacement = entity.update { state ->
                capabilities.fold(state) { accumulator, capability ->
                    accumulator.with(capability = capability)
                }
            },
        )

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

    /*
     * Helper methods
     */

    private fun MutableStateFlow<List<Entity<*>>>.replace(replacement: Entity<*>) = update { collection ->
        collection.map { item ->
            if (item.identifier == replacement.identifier) {
                replacement
            } else {
                item
            }
        }
    }

    private fun <T, R> Flow<T>.mapState(
        scope: CoroutineScope,
        initial: T,
        started: SharingStarted = SharingStarted.WhileSubscribed(DEFAULT_STOP_TIMEOUT_MILLIS),
        transform: (T) -> R,
    ): StateFlow<R> = map(transform)
        .distinctUntilChanged()
        .stateIn(scope = scope, started = started, initialValue = transform(initial))

    companion object {
        private const val DEFAULT_STOP_TIMEOUT_MILLIS = 5000L
        private val SIMULATION_TICK = 0.1.seconds

        private val randomizer = Random(seed = Clock.System.now().epochSeconds)
    }
}
