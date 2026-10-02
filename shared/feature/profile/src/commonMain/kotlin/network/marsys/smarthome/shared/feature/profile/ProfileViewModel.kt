package network.marsys.smarthome.shared.feature.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import network.marsys.smarthome.shared.library.core.coroutines.SuspendingActionStateEffectMutator
import network.marsys.smarthome.shared.library.core.coroutines.handle
import network.marsys.smarthome.shared.library.core.coroutines.suspendingActionStateEffectMutator
import network.marsys.smarthome.shared.library.navigation.NavigationDestination
import network.marsys.smarthome.shared.library.resources.SmartHomeRes
import network.marsys.smarthome.shared.library.resources.demo_user
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.model.ConnectionConfiguration
import org.jetbrains.compose.resources.getString

internal typealias ProfileStateHolder =
    SuspendingActionStateEffectMutator<ProfileScreenAction, ProfileScreenState, ProfileScreenEffect>

class ProfileViewModel(
    private val applicationConfigurationRepository: ApplicationConfigurationRepository,
    coroutineScope: CoroutineScope,
) : ViewModel(viewModelScope = coroutineScope),
    ProfileStateHolder by coroutineScope.suspendingActionStateEffectMutator(
        state = MutableProfileScreenState(),
        producer = { state, actions, emitter ->
            launchConfigurationMutations(
                state = state,
                applicationConfigurationRepository = applicationConfigurationRepository,
            )

            actions.handle(
                scope = this,
                keySelector = ProfileScreenAction::key,
            ) {
                when (val action = type()) {
                    ProfileScreenAction.ChangeAppAppearance ->
                        action.flow.collect {
                            emitter.emit(
                                effect = ProfileScreenEffect.Navigate(
                                    target = NavigationDestination.ChangeAppAppearanceModal,
                                ),
                            )
                        }

                    ProfileScreenAction.ConfirmLogout ->
                        Unit

                    ProfileScreenAction.ConfirmResetOnboarding ->
                        applicationConfigurationRepository.setConnectionConfiguration(
                            configuration = ConnectionConfiguration.Unconfigured,
                        )

                    ProfileScreenAction.Logout -> action.flow.collect {
                        emitter.emit(
                            effect = ProfileScreenEffect.DisplayConfirmLogoutDialog,
                        )
                    }

                    ProfileScreenAction.ResetOnboarding ->
                        action.flow.collect {
                            emitter.emit(
                                effect = ProfileScreenEffect.DisplayConfirmResetOnboardingDialog,
                            )
                        }
                }
            }
        },
    )

context(scope: CoroutineScope)
private fun launchConfigurationMutations(
    state: MutableProfileScreenState,
    applicationConfigurationRepository: ApplicationConfigurationRepository,
) {
    scope.launch {
        applicationConfigurationRepository.connection.collect {
            applyConnectedBackendMutations(
                state = state,
                configuration = it,
            )

            applyUserMutations(
                state = state,
                demoMode = it is ConnectionConfiguration.Demo,
            )
        }
    }
}

private fun applyConnectedBackendMutations(
    state: MutableProfileScreenState,
    configuration: ConnectionConfiguration,
) {
    state.connectedBackend = (configuration as? ConnectionConfiguration.Backend)?.uri
}

private suspend fun applyUserMutations(
    state: MutableProfileScreenState,
    demoMode: Boolean,
) {
    state.user = when (demoMode) {
        true -> getString(SmartHomeRes.string.demo_user)
        else -> "Niels"
    }

    state.email = when (demoMode) {
        true -> "demo.user@example.com"
        else -> "niels.marsman@example.com"
    }
}

private class MutableProfileScreenState : ProfileScreenState {
    override var user: String by mutableStateOf("")
    override var email: String by mutableStateOf("")
    override var connectedBackend: String? by mutableStateOf(null)
}
