package network.marsys.smarthome.shared.feature.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.feature.dashboard.entity.Action
import network.marsys.smarthome.shared.feature.dashboard.entity.Effect
import network.marsys.smarthome.shared.feature.dashboard.entity.State
import network.marsys.smarthome.shared.library.core.coroutines.SuspendingActionStateEffectMutator
import network.marsys.smarthome.shared.library.core.coroutines.handle
import network.marsys.smarthome.shared.library.core.coroutines.suspendingActionStateEffectMutator
import network.marsys.smarthome.shared.library.resources.SmartHomeRes
import network.marsys.smarthome.shared.library.resources.demo_user
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import org.jetbrains.compose.resources.getString

internal typealias DashboardStateHolder = SuspendingActionStateEffectMutator<Action, State, Effect>

class DashboardViewModel(
    private val applicationConfigurationRepository: ApplicationConfigurationRepository,
    coroutineScope: CoroutineScope,
) : ViewModel(viewModelScope = coroutineScope),
    DashboardStateHolder by coroutineScope.suspendingActionStateEffectMutator(
        state = MutableState(),
        producer = { state, actions, emitter ->
            launchUserNameMutations(state, applicationConfigurationRepository)

            actions.handle(
                scope = this,
                keySelector = Action::key,
            ) {
                when (val action = type()) {
                    Action.ChangeAppAppearance -> action.flow.collect {
                        emitter.emit(Effect.OpenAppearanceModal)
                    }

                    Action.ToggleGroupEntitiesByType -> action.flow.collect {
                        state.quickControlState.groupedEntitiesByType = !state.quickControlState.groupedEntitiesByType
                    }
                }
            }
        },
    )

private fun CoroutineScope.launchUserNameMutations(
    state: MutableState,
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

private class MutableState(
    quickControlState: MutableQuickControlState = MutableQuickControlState(),
) : State {
    override val quickControlState: MutableQuickControlState by mutableStateOf(quickControlState)
    override var user: String by mutableStateOf("")
}

private class MutableQuickControlState : State.QuickControlState {
    override var entities: MutableMap<EntityIdentifier, Entity<*>> by mutableStateOf(mutableMapOf())
    override var groupedEntitiesByType: Boolean by mutableStateOf(false)
}
