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
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.library.core.coroutines.SuspendingActionStateEffectMutator
import network.marsys.smarthome.shared.library.core.coroutines.handle
import network.marsys.smarthome.shared.library.core.coroutines.suspendingActionStateEffectMutator
import network.marsys.smarthome.shared.library.navigation.NavigationDestination
import kotlin.collections.set

internal typealias ZoneStateHolder =
    SuspendingActionStateEffectMutator<ZoneScreenAction, ZoneScreenState, ZoneScreenEffect>

class ZoneViewModel(
    private val identifier: EntityIdentifier,
    private val entityRepository: EntityRepository,
    coroutineScope: CoroutineScope,
) : ViewModel(viewModelScope = coroutineScope),
    ZoneStateHolder by coroutineScope.suspendingActionStateEffectMutator(
        state = MutableZoneScreenState(identifier),
        producer = { state, actions, emitter ->
            var entityMutationJob: Job?

            entityMutationJob = launchEntityMutations(
                identifier = identifier,
                state = state,
                entityRepository = entityRepository,
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
                        // Handle opening entity detail modal
                    }

                    is ZoneScreenAction.RetryLoadingEntities -> action.flow.collect {
                        entityMutationJob?.cancel()
                        entityMutationJob = launchEntityMutations(
                            identifier = identifier,
                            state = state,
                            entityRepository = entityRepository,
                        )
                    }
                }
            }
        },
    )

@OptIn(FlowPreview::class)
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

private class MutableZoneScreenState(
    identifier: EntityIdentifier,
) : ZoneScreenState {
    override var condition: ZoneScreenState.Condition by mutableStateOf(ZoneScreenState.Condition.Loading)
    override var zone: EntityIdentifier by mutableStateOf(identifier)
    override var entities: SnapshotStateMap<EntityIdentifier, Entity<*>> = mutableStateMapOf()
}
