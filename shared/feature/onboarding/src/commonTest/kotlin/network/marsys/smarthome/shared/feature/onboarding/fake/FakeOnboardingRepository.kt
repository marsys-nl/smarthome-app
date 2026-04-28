package network.marsys.smarthome.shared.feature.onboarding.fake

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import network.marsys.smarthome.shared.library.store.OnboardingRepository

class FakeOnboardingRepository(
    override val isOnboardingFinished: MutableStateFlow<Boolean> =
        MutableStateFlow(value = false),
) : OnboardingRepository {
    override suspend fun finishOnboarding() {
        isOnboardingFinished.update { true }
    }

    override suspend fun resetOnboarding() {
        isOnboardingFinished.update { false }
    }
}
