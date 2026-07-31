package network.marsys.smarthome.shared.feature.zones

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.EntityRepository
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.zone.Zone
import network.marsys.smarthome.shared.library.core.coroutines.SuspendingActionStateEffectMutator
import network.marsys.smarthome.shared.library.core.coroutines.handle
import network.marsys.smarthome.shared.library.core.coroutines.suspendingActionStateEffectMutator
import network.marsys.smarthome.shared.library.navigation.NavigationDestination
import kotlin.collections.set

internal typealias ZonesStateHolder =
    SuspendingActionStateEffectMutator<ZonesScreenAction, ZonesScreenState, ZonesScreenEffect>

class ZonesViewModel(
    private val entityRepository: EntityRepository,
    coroutineScope: CoroutineScope,
) : ViewModel(viewModelScope = coroutineScope),
    ZonesStateHolder by coroutineScope.suspendingActionStateEffectMutator(
        state = MutableZonesScreenState(),
        producer = { state, actions, emitter ->
            var zoneMutationJob: Job?

            zoneMutationJob = launchZoneMutations(
                state = state,
                entityRepository = entityRepository,
            )

            actions.handle(
                scope = this,
                keySelector = ZonesScreenAction::key,
            ) {
                when (val action = type()) {
                    is ZonesScreenAction.OpenZoneScreen -> action.flow.collect {
                        emitter.emit(
                            effect = ZonesScreenEffect.Navigate(
                                target = NavigationDestination.Zone(
                                    zone = action.zone,
                                ),
                            ),
                        )
                    }

                    is ZonesScreenAction.RetryZones -> action.flow.collect {
                        zoneMutationJob?.cancel()
                        zoneMutationJob = launchZoneMutations(
                            state = state,
                            entityRepository = entityRepository,
                        )
                    }
                }
            }
        },
    )

context(scope: CoroutineScope)
private fun launchZoneMutations(
    state: MutableZonesScreenState,
    entityRepository: EntityRepository,
) = scope.launch {
    state.condition = ZonesScreenState.Condition.Loading

    val flow = combine(
        entityRepository.zones,
        entityRepository.entities,
    ) { zones, entities ->
        val current = state.zones

        zones.forEach { zone ->
            if (!current.containsKey(zone.identifier)) {
                current[zone.identifier] = MutableZoneState(zone = zone)
            }

            val stateEntities = current[zone.identifier]
                ?.entities as? SnapshotStateMap<EntityIdentifier, Entity<*>>
                ?: return@forEach

            stateEntities.clear()
            stateEntities.putAll(
                from = entities
                    .filter { entity -> entity.zone?.identifier == zone.identifier }
                    .associateBy { entity -> entity.identifier },
            )
        }

        val removed = current.keys - zones.map { it.identifier }.toSet()
        removed.forEach(current::remove)

        state.condition = when {
            zones.isEmpty() -> ZonesScreenState.Condition.Empty
            else -> ZonesScreenState.Condition.Success
        }
    }

    flow
        .catch {
            println(it)
            state.condition = ZonesScreenState.Condition.Error
        }
        .collect()
}

private class MutableZonesScreenState : ZonesScreenState {
    override var condition: ZonesScreenState.Condition by mutableStateOf(ZonesScreenState.Condition.Loading)
    override var zones: SnapshotStateMap<EntityIdentifier, ZonesScreenState.ZoneState> = mutableStateMapOf()
}

private class MutableZoneState(
    zone: Zone,
) : ZonesScreenState.ZoneState {
    override var zone: Zone by mutableStateOf(zone)
    override var entities: SnapshotStateMap<EntityIdentifier, Entity<*>> = mutableStateMapOf()
}
