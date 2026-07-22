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
import network.marsys.smarthome.shared.library.store.OnboardingRepository
import org.jetbrains.compose.resources.getString

internal typealias ProfileStateHolder =
    SuspendingActionStateEffectMutator<ProfileScreenAction, ProfileScreenState, ProfileScreenEffect>

class ProfileViewModel(
    private val applicationConfigurationRepository: ApplicationConfigurationRepository,
    private val onboardingRepository: OnboardingRepository,
    coroutineScope: CoroutineScope,
) : ViewModel(viewModelScope = coroutineScope),
    ProfileStateHolder by coroutineScope.suspendingActionStateEffectMutator(
        state = MutableProfileScreenState(),
        producer = { state, actions, emitter ->
            launchUserMutations(
                state = state,
                applicationConfigurationRepository = applicationConfigurationRepository,
            )

            launchConnectedBackendMutations(
                state = state,
                applicationConfigurationRepository = applicationConfigurationRepository,
            )

            actions.handle(
                scope = this,
                keySelector = ProfileScreenAction::key,
            ) {
                when (val action = type()) {
                    ProfileScreenAction.ChangeAppAppearance -> action.flow.collect {
                        emitter.emit(
                            effect = ProfileScreenEffect.Navigate(
                                target = NavigationDestination.ChangeAppAppearanceModal,
                            ),
                        )
                    }

                    ProfileScreenAction.ConfirmLogout ->
                        Unit // Do nothing yet, needs to be implemented in the future

                    ProfileScreenAction.ConfirmResetOnboarding ->
                        onboardingRepository.resetOnboarding()

                    ProfileScreenAction.Logout -> action.flow.collect {
                        emitter.emit(
                            effect = ProfileScreenEffect.DisplayConfirmLogoutDialog,
                        )
                    }

                    ProfileScreenAction.ResetOnboarding -> action.flow.collect {
                        emitter.emit(
                            effect = ProfileScreenEffect.DisplayConfirmResetOnboardingDialog,
                        )
                    }
                }
            }
        },
    )

context(scope: CoroutineScope)
private fun launchConnectedBackendMutations(
    state: MutableProfileScreenState,
    applicationConfigurationRepository: ApplicationConfigurationRepository,
) {
    scope.launch {
        applicationConfigurationRepository.backendUri.collect {
            state.connectedBackend = it
        }
    }
}

context(scope: CoroutineScope)
private fun launchUserMutations(
    state: MutableProfileScreenState,
    applicationConfigurationRepository: ApplicationConfigurationRepository,
) {
    scope.launch {
        applicationConfigurationRepository.isDemoMode.collect {
            state.user = when (it) {
                true -> getString(SmartHomeRes.string.demo_user)
                else -> "Niels"
            }

            state.email = when (it) {
                true -> "demo.user@example.com"
                else -> "niels.marsman@example.com"
            }
        }
    }
}

private class MutableProfileScreenState : ProfileScreenState {
    override var user: String by mutableStateOf("")
    override var email: String by mutableStateOf("")
    override var connectedBackend: String? by mutableStateOf(null)
}
