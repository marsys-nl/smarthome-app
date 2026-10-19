package network.marsys.smarthome.shared.feature.zone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.EntityRepository
import network.marsys.smarthome.shared.domain.entity.ZoneRepository
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.zone.Zone
import network.marsys.smarthome.shared.library.core.coroutines.EffectEmitter
import network.marsys.smarthome.shared.library.core.coroutines.SuspendingActionStateEffectMutator
import network.marsys.smarthome.shared.library.core.coroutines.handle
import network.marsys.smarthome.shared.library.core.coroutines.suspendingActionStateEffectMutator
import network.marsys.smarthome.shared.library.navigation.NavigationDestination

internal typealias ZoneStateHolder =
    SuspendingActionStateEffectMutator<ZoneScreenAction, ZoneScreenState, ZoneScreenEffect>

class ZoneViewModel(
    private val identifier: EntityIdentifier,
    private val entityRepository: EntityRepository,
    private val zoneRepository: ZoneRepository,
    coroutineScope: CoroutineScope,
) : ViewModel(viewModelScope = coroutineScope),
    ZoneStateHolder by coroutineScope.suspendingActionStateEffectMutator(
        state = MutableZoneScreenState(),
        producer = { state, actions, emitter ->
            var zoneMutationJob: Job?

            zoneMutationJob = launchZoneMutations(
                identifier = identifier,
                entityRepository = entityRepository,
                zoneRepository = zoneRepository,
                emitter = emitter,
                state = state,
            )

            actions.handle(
                scope = this,
                keySelector = ZoneScreenAction::key,
            ) {
                when (val action = type()) {
                    is ZoneScreenAction.NavigateToZones -> action.flow.collect {
                        emitter.emit(
                            effect = ZoneScreenEffect.Navigate(
                                target = NavigationDestination.Zones,
                            ),
                        )
                    }

                    is ZoneScreenAction.OpenEntityDetailModal -> action.flow.collect {
                        emitter.emit(
                            effect = ZoneScreenEffect.Navigate(
                                target = NavigationDestination.EntityDetailModal(entity = action.entity),
                            ),
                        )
                    }

                    is ZoneScreenAction.RetryLoadingEntities -> action.flow.collect {
                        zoneMutationJob?.cancel()
                        zoneMutationJob = launchZoneMutations(
                            identifier = identifier,
                            entityRepository = entityRepository,
                            zoneRepository = zoneRepository,
                            emitter = emitter,
                            state = state,
                        )
                    }

                    is ZoneScreenAction.ToggleEntityState -> action.flow.collect {
                        entityRepository.execute(
                            action = when (it.state) {
                                true -> OnOff.Toggle.On(identifier = action.entity)
                                false -> OnOff.Toggle.Off(identifier = action.entity)
                            },
                        )
                    }
                }
            }
        },
    )

context(scope: CoroutineScope)
private fun launchZoneMutations(
    identifier: EntityIdentifier,
    entityRepository: EntityRepository,
    zoneRepository: ZoneRepository,
    emitter: EffectEmitter<ZoneScreenEffect>,
    state: MutableZoneScreenState,
) = scope.launch {
    if (state.condition != ZoneScreenState.Condition.Resolving) {
        state.condition = ZoneScreenState.Condition.Loading
    }

    zoneRepository.zone(identifier = identifier)
        .collect { zone ->
            if (zone == null) {
                emitter.emit(
                    effect = ZoneScreenEffect.Navigate(
                        target = NavigationDestination.Zones,
                    ),
                )
            } else {
                state.zone = zone

                launchEntityMutations(
                    identifier = identifier,
                    state = state,
                    entityRepository = entityRepository,
                )
            }
        }
}

context(scope: CoroutineScope)
private fun launchEntityMutations(
    identifier: EntityIdentifier,
    state: MutableZoneScreenState,
    entityRepository: EntityRepository,
) = scope.launch {
    state.condition = ZoneScreenState.Condition.Loading

    entityRepository.entities
        .catch { state.condition = ZoneScreenState.Condition.Error }
        .collect { entities ->
            val current = state.entities

            entities
                .filter { it.zone?.identifier == identifier }
                .forEach {
                    if (current[it.identifier] != it) {
                        current[it.identifier] = it
                    }
                }

            val removed = current.keys - entities.map { it.identifier }.toSet()
            removed.forEach(current::remove)

            state.condition = when {
                entities.isEmpty() -> ZoneScreenState.Condition.Empty
                else -> ZoneScreenState.Condition.Success
            }
        }
}

private class MutableZoneScreenState : ZoneScreenState {
    override var condition: ZoneScreenState.Condition by mutableStateOf(ZoneScreenState.Condition.Resolving)
    override var zone: Zone? by mutableStateOf(null)
    override var entities: SnapshotStateMap<EntityIdentifier, Entity<*>> = mutableStateMapOf()
}
