package network.marsys.smarthome.shared.modal.entity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.EntityRepository
import network.marsys.smarthome.shared.domain.entity.capability.Brightness
import network.marsys.smarthome.shared.domain.entity.capability.ChildLock
import network.marsys.smarthome.shared.domain.entity.capability.Movement
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import network.marsys.smarthome.shared.domain.entity.capability.ScheduledMode
import network.marsys.smarthome.shared.domain.entity.capability.TargetTemperature
import network.marsys.smarthome.shared.domain.entity.capability.WindowDetection
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
                    is EntityDetailModalAction.AdjustBrightness -> action.flow.collect {
                        entityRepository.execute(
                            action = Brightness.SetBrightness(
                                identifier = it.entity,
                                brightness = it.brightness,
                            ),
                        )
                    }

                    is EntityDetailModalAction.AdjustTargetTemperature -> action.flow.collect {
                        entityRepository.execute(
                            action = TargetTemperature.SetTargetTemperature(
                                identifier = it.entity,
                                targetTemperature = it.temperature,
                            ),
                        )
                    }

                    is EntityDetailModalAction.MoveCover.Open -> action.flow.collect {
                        entityRepository.execute(
                            action = Movement.Move.Open(
                                identifier = it.entity,
                            ),
                        )
                    }

                    is EntityDetailModalAction.MoveCover.Close -> action.flow.collect {
                        entityRepository.execute(
                            action = Movement.Move.Close(
                                identifier = it.entity,
                            ),
                        )
                    }

                    is EntityDetailModalAction.MoveCover.Stop -> action.flow.collect {
                        entityRepository.execute(
                            action = Movement.Move.Stop(
                                identifier = it.entity,
                            ),
                        )
                    }

                    is EntityDetailModalAction.ToggleChildLock -> action.flow.collect {
                        entityRepository.execute(
                            action = when (it.state) {
                                true -> ChildLock.Toggle.On(identifier = action.entity)
                                false -> ChildLock.Toggle.Off(identifier = action.entity)
                            },
                        )
                    }

                    is EntityDetailModalAction.ToggleEntity -> action.flow.collect {
                        entityRepository.execute(
                            action = when (it.state) {
                                true -> OnOff.Toggle.On(identifier = action.entity)
                                false -> OnOff.Toggle.Off(identifier = action.entity)
                            },
                        )
                    }

                    is EntityDetailModalAction.ToggleScheduledMode -> action.flow.collect {
                        entityRepository.execute(
                            action = when (it.state) {
                                true -> ScheduledMode.Toggle.On(identifier = action.entity)
                                false -> ScheduledMode.Toggle.Off(identifier = action.entity)
                            },
                        )
                    }

                    is EntityDetailModalAction.ToggleWindowDetection -> action.flow.collect {
                        entityRepository.execute(
                            action = when (it.state) {
                                true -> WindowDetection.Toggle.On(identifier = action.entity)
                                false -> WindowDetection.Toggle.Off(identifier = action.entity)
                            },
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
