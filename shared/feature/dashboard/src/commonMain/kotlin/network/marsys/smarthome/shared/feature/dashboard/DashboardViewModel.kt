package network.marsys.smarthome.shared.feature.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.EntityRepository
import network.marsys.smarthome.shared.domain.entity.area.Area
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.library.core.coroutines.SuspendingActionStateEffectMutator
import network.marsys.smarthome.shared.library.core.coroutines.handle
import network.marsys.smarthome.shared.library.core.coroutines.suspendingActionStateEffectMutator
import network.marsys.smarthome.shared.library.navigation.NavigationDestination
import network.marsys.smarthome.shared.library.resources.SmartHomeRes
import network.marsys.smarthome.shared.library.resources.demo_user
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import org.jetbrains.compose.resources.getString

internal typealias DashboardStateHolder =
    SuspendingActionStateEffectMutator<DashboardScreenAction, DashboardScreenState, DashboardScreenEffect>

class DashboardViewModel(
    private val applicationConfigurationRepository: ApplicationConfigurationRepository,
    private val entityRepository: EntityRepository,
    coroutineScope: CoroutineScope,
) : ViewModel(viewModelScope = coroutineScope),
    DashboardStateHolder by coroutineScope.suspendingActionStateEffectMutator(
        state = MutableDashboardScreenState(),
        producer = { state, actions, emitter ->
            launchAreaMutations(
                state = state,
                entityRepository = entityRepository,
            )

            launchEntityMutations(
                state = state,
                entityRepository = entityRepository,
            )

            launchUserNameMutations(
                state = state,
                applicationConfigurationRepository = applicationConfigurationRepository,
            )

            actions.handle(
                scope = this,
                keySelector = DashboardScreenAction::key,
            ) {
                when (val action = type()) {
                    DashboardScreenAction.ChangeAppAppearance -> action.flow.collect {
                        emitter.emit(
                            effect = DashboardScreenEffect.Navigate(
                                target = NavigationDestination.ChangeAppAppearanceModal,
                            ),
                        )
                    }

                    DashboardScreenAction.NavigateToAreas -> action.flow.collect {
                        emitter.emit(
                            effect = DashboardScreenEffect.Navigate(
                                target = NavigationDestination.Areas,
                            ),
                        )
                    }

                    is DashboardScreenAction.OpenEntityDetailModal -> action.flow.collect {
                        emitter.emit(
                            effect = DashboardScreenEffect.Navigate(
                                target = NavigationDestination.EntityDetailModal(entity = action.entity),
                            ),
                        )
                    }

                    is DashboardScreenAction.OpenAreaScreen -> action.flow.collect {
                        emitter.emit(
                            effect = DashboardScreenEffect.Navigate(
//                                target = NavigationDestination.AreaScreen(area = action.area),
                                target = NavigationDestination.Areas,
                            ),
                        )
                    }

                    is DashboardScreenAction.ToggleEntityState -> action.flow.collect {
                        entityRepository.execute(
                            action = when (it.state) {
                                true -> OnOff.Toggle.On(identifier = action.entity)
                                false -> OnOff.Toggle.Off(identifier = action.entity)
                            },
                        )
                    }

                    DashboardScreenAction.ToggleGroupEntitiesByType -> action.flow.collect {
                        state.quickControlState.groupedEntitiesByType = !state.quickControlState.groupedEntitiesByType
                    }
                }
            }
        },
    )

context(scope: CoroutineScope)
private fun launchAreaMutations(
    state: MutableDashboardScreenState,
    entityRepository: EntityRepository,
) {
    scope.launch {
        val flow = combine(
            entityRepository.areas,
            entityRepository.entities,
        ) { areas, entities ->
            val current = state.areasState.areas

            areas.forEach { area ->
                if (!current.containsKey(area.identifier)) {
                    current[area.identifier] = MutableAreaState(area = area)
                }

                val stateEntities = current[area.identifier]?.entities as? SnapshotStateMap<EntityIdentifier, Entity<*>>
                    ?: return@forEach

                stateEntities.clear()
                stateEntities.putAll(
                    from = entities
                        .filter { entity -> entity.area?.identifier == area.identifier }
                        .associateBy { entity -> entity.identifier },
                )
            }

            val removed = current.keys - areas.map { it.identifier }.toSet()
            removed.forEach(current::remove)

            state.areasState.condition = when {
                areas.isEmpty() -> DashboardScreenState.Condition.Empty
                else -> DashboardScreenState.Condition.Success
            }
        }

        flow
            .catch { state.areasState.condition = DashboardScreenState.Condition.Error }
            .collect()
    }
}

@OptIn(FlowPreview::class)
context(scope: CoroutineScope)
private fun launchEntityMutations(
    state: MutableDashboardScreenState,
    entityRepository: EntityRepository,
) {
    scope.launch {
        entityRepository.entities
            .catch { state.quickControlState.condition = DashboardScreenState.Condition.Error }
            .collect { entities ->
                val current = state.quickControlState.entities

                entities.forEach {
                    if (current[it.identifier] != it) {
                        current[it.identifier] = it
                    }
                }

                val removed = current.keys - entities.map { it.identifier }.toSet()
                removed.forEach(current::remove)

                state.quickControlState.condition = when {
                    entities.isEmpty() -> DashboardScreenState.Condition.Empty
                    else -> DashboardScreenState.Condition.Success
                }
            }
    }
}

context(scope: CoroutineScope)
private fun launchUserNameMutations(
    state: MutableDashboardScreenState,
    applicationConfigurationRepository: ApplicationConfigurationRepository,
) {
    scope.launch {
        applicationConfigurationRepository.isDemoMode.collect {
            state.user = when (it) {
                true -> getString(SmartHomeRes.string.demo_user)
                else -> "Niels"
            }
        }
    }
}

private class MutableDashboardScreenState(
    areasState: MutableAreasState = MutableAreasState(),
    quickControlState: MutableQuickControlState = MutableQuickControlState(),
) : DashboardScreenState {
    override val areasState: MutableAreasState by mutableStateOf(areasState)
    override val quickControlState: MutableQuickControlState by mutableStateOf(quickControlState)
    override var user: String by mutableStateOf("")
}

private class MutableAreasState : DashboardScreenState.AreasState {
    override var condition: DashboardScreenState.Condition by mutableStateOf(DashboardScreenState.Condition.Loading)
    override var areas: SnapshotStateMap<EntityIdentifier, DashboardScreenState.AreaState> = mutableStateMapOf()
}

private class MutableAreaState(
    area: Area,
) : DashboardScreenState.AreaState {
    override var area: Area by mutableStateOf(area)
    override var entities: SnapshotStateMap<EntityIdentifier, Entity<*>> = mutableStateMapOf()
}

private class MutableQuickControlState : DashboardScreenState.QuickControlState {
    override var condition: DashboardScreenState.Condition by mutableStateOf(DashboardScreenState.Condition.Loading)
    override var entities: SnapshotStateMap<EntityIdentifier, Entity<*>> = mutableStateMapOf()
    override var groupedEntitiesByType: Boolean by mutableStateOf(false)
}
