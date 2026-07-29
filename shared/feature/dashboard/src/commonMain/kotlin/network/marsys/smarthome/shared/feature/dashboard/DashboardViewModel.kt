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
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.zone.Zone
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
            launchZoneMutations(
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

                    DashboardScreenAction.NavigateToZones -> action.flow.collect {
                        emitter.emit(
                            effect = DashboardScreenEffect.Navigate(
                                target = NavigationDestination.Zones,
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

                    is DashboardScreenAction.OpenZoneScreen -> action.flow.collect {
                        emitter.emit(
                            effect = DashboardScreenEffect.Navigate(
//                                target = NavigationDestination.ZoneScreen(zone = action.zone),
                                target = NavigationDestination.Zones,
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
private fun launchZoneMutations(
    state: MutableDashboardScreenState,
    entityRepository: EntityRepository,
) {
    scope.launch {
        val flow = combine(
            entityRepository.zones,
            entityRepository.entities,
        ) { zones, entities ->
            val current = state.zonesState.zones

            zones
                .take(MAX_ZONES)
                .forEach { zone ->
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

            state.zonesState.condition = when {
                zones.isEmpty() -> DashboardScreenState.Condition.Empty
                else -> DashboardScreenState.Condition.Success
            }
        }

        flow
            .catch { state.zonesState.condition = DashboardScreenState.Condition.Error }
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
    zonesState: MutableZonesState = MutableZonesState(),
    quickControlState: MutableQuickControlState = MutableQuickControlState(),
) : DashboardScreenState {
    override val zonesState: MutableZonesState by mutableStateOf(zonesState)
    override val quickControlState: MutableQuickControlState by mutableStateOf(quickControlState)
    override var user: String by mutableStateOf("")
}

private class MutableZonesState : DashboardScreenState.ZonesState {
    override var condition: DashboardScreenState.Condition by mutableStateOf(DashboardScreenState.Condition.Loading)
    override var zones: SnapshotStateMap<EntityIdentifier, DashboardScreenState.ZoneState> = mutableStateMapOf()
}

private class MutableZoneState(
    zone: Zone,
) : DashboardScreenState.ZoneState {
    override var zone: Zone by mutableStateOf(zone)
    override var entities: SnapshotStateMap<EntityIdentifier, Entity<*>> = mutableStateMapOf()
}

private class MutableQuickControlState : DashboardScreenState.QuickControlState {
    override var condition: DashboardScreenState.Condition by mutableStateOf(DashboardScreenState.Condition.Loading)
    override var entities: SnapshotStateMap<EntityIdentifier, Entity<*>> = mutableStateMapOf()
    override var groupedEntitiesByType: Boolean by mutableStateOf(false)
}

const val MAX_ZONES = 6
