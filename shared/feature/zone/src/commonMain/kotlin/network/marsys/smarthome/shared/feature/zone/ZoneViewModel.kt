package network.marsys.smarthome.shared.feature.zone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.EntityRepository
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.library.core.coroutines.SuspendingActionStateEffectMutator
import network.marsys.smarthome.shared.library.core.coroutines.suspendingActionStateEffectMutator

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
            // Implement the logic to handle actions and update the state here
        },
    )

private class MutableZoneScreenState(
    identifier: EntityIdentifier,
) : ZoneScreenState {
    override var condition: ZoneScreenState.Condition by mutableStateOf(ZoneScreenState.Condition.Loading)
    override var zone: EntityIdentifier by mutableStateOf(identifier)
    override var entities: SnapshotStateMap<EntityIdentifier, Entity<*>> = mutableStateMapOf()
}
