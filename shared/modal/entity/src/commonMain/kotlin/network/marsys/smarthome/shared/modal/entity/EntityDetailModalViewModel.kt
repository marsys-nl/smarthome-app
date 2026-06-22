package network.marsys.smarthome.shared.modal.entity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.EntityRepository
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.library.core.coroutines.SuspendingActionStateMutator
import network.marsys.smarthome.shared.library.core.coroutines.handle
import network.marsys.smarthome.shared.library.core.coroutines.suspendingActionStateMutator

internal typealias EntityDetailModalStateHolder =
    SuspendingActionStateMutator<EntityDetailModalAction, EntityDetailModalState>

class EntityDetailModalViewModel(
    private val identifier: EntityIdentifier,
    private val entityRepository: EntityRepository,
    coroutineScope: CoroutineScope,
) : ViewModel(viewModelScope = coroutineScope),
    EntityDetailModalStateHolder by coroutineScope.suspendingActionStateMutator(
        state = MutableEntityDetailModalState(),
        producer = { state, actions ->
            launchEntityMutations(
                identifier = identifier,
                state = state,
                entityRepository = entityRepository,
            )

            actions.handle(
                scope = this,
                keySelector = EntityDetailModalAction::key,
            ) {
                when (val action = type()) {
                    is EntityDetailModalAction.ToggleEntity -> action.flow.collect {
                        entityRepository.execute(
                            action = when (action.state) {
                                true -> Entity.Toggleable.Toggle.On(identifier = action.entity)
                                false -> Entity.Toggleable.Toggle.Off(identifier = action.entity)
                            },
                        )
                    }

                    is EntityDetailModalAction.AdjustBrightness -> action.flow.collect {
                        entityRepository.execute(
                            action = Entity.Dimmable.SetBrightness(
                                identifier = it.entity,
                                brightness = it.brightness,
                            ),
                        )
                    }
                }
            }
        },
    )

context(scope: CoroutineScope)
private fun launchEntityMutations(
    identifier: EntityIdentifier,
    state: MutableEntityDetailModalState,
    entityRepository: EntityRepository,
) {
    scope.launch {
        entityRepository.entity(identifier).collect { entity ->
            state.entity = entity
            state.isLoading = false
        }
    }
}

private class MutableEntityDetailModalState : EntityDetailModalState {
    override var isLoading: Boolean by mutableStateOf(true)
    override var entity: Entity<*>? by mutableStateOf(null)
}
