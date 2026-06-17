package network.marsys.smarthome.shared.modal.entity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.Light
import network.marsys.smarthome.shared.library.core.coroutines.SuspendingActionStateMutator
import network.marsys.smarthome.shared.library.core.coroutines.suspendingActionStateMutator
import kotlin.time.Duration.Companion.milliseconds

internal typealias EntityDetailModalStateHolder =
    SuspendingActionStateMutator<EntityDetailModalAction, EntityDetailModalState>

class EntityDetailModalViewModel(
    private val identifier: EntityIdentifier,
    coroutineScope: CoroutineScope,
) : ViewModel(viewModelScope = coroutineScope),
    EntityDetailModalStateHolder by coroutineScope.suspendingActionStateMutator(
        state = MutableEntityDetailModalState(),
        producer = { state, actions ->
            launchEntityMutations(
                identifier = identifier,
                state = state,
            )
        },
    )

private fun CoroutineScope.launchEntityMutations(
    identifier: EntityIdentifier,
    state: MutableEntityDetailModalState,
) {
    println("[EntityDetailModalViewModel] Launching entity mutations for identifier: $identifier")

    launch {
        delay((100..1600).random().milliseconds)

        state.isLoading = false
        state.entity = Light(
            identifier = identifier,
            state = Light.State.Unknown,
        )
    }
}

private class MutableEntityDetailModalState : EntityDetailModalState {
    override var isLoading: Boolean by mutableStateOf(true)
    override var entity: Entity<*>? by mutableStateOf(null)
}
