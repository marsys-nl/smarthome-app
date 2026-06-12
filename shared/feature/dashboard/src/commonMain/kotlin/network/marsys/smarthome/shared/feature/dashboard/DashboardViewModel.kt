package network.marsys.smarthome.shared.feature.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.feature.dashboard.demo.DemoEntities
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
    coroutineScope: CoroutineScope,
) : ViewModel(viewModelScope = coroutineScope),
    DashboardStateHolder by coroutineScope.suspendingActionStateEffectMutator(
        state = MutableDashboardScreenState(),
        producer = { state, actions, emitter ->
            launchEntityMutations(
                state = state,
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

                    is DashboardScreenAction.ToggleEntityState -> action.flow.collect {
                        val entity = state.quickControlState.entities[action.entity] as? Entity.Toggleable
                            ?: return@collect

                        state.quickControlState.entities[action.entity] = entity.toggle()
                    }

                    is DashboardScreenAction.OpenEntityDetailModal -> action.flow.collect {
                        emitter.emit(
                            effect = DashboardScreenEffect.Navigate(
                                target = NavigationDestination.EntityDetailModal(entity = action.entity),
                            ),
                        )
                    }

                    DashboardScreenAction.ToggleGroupEntitiesByType -> action.flow.collect {
                        state.quickControlState.groupedEntitiesByType = !state.quickControlState.groupedEntitiesByType
                    }
                }
            }
        },
    )

private fun CoroutineScope.launchEntityMutations(
    state: MutableDashboardScreenState,
) {
    state.quickControlState.entities
        .putAll(DemoEntities.associateBy { it.identifier })

    launch {
        // No-op for now; this will listen to SSE updates for entities.
    }
}

private fun CoroutineScope.launchUserNameMutations(
    state: MutableDashboardScreenState,
    applicationConfigurationRepository: ApplicationConfigurationRepository,
) {
    launch {
        applicationConfigurationRepository.isDemoMode.collect {
            state.user = when (it) {
                true -> getString(SmartHomeRes.string.demo_user)
                else -> "Niels"
            }
        }
    }
}

private class MutableDashboardScreenState(
    quickControlState: MutableQuickControlState = MutableQuickControlState(),
) : DashboardScreenState {
    override val quickControlState: MutableQuickControlState by mutableStateOf(quickControlState)
    override var user: String by mutableStateOf("")
}

private class MutableQuickControlState : DashboardScreenState.QuickControlState {
    override var entities: SnapshotStateMap<EntityIdentifier, Entity<*>> = mutableStateMapOf()
    override var groupedEntitiesByType: Boolean by mutableStateOf(false)
}
