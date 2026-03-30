package network.marsys.smarthome.shared.library.store

import kotlinx.coroutines.flow.Flow

/**
 * Tracks the completion state of the onboarding flow.
 */
interface OnboardingRepository {
    /**
     * Whether the onboarding flow has been completed.
     */
    val isOnboardingFinished: Flow<Boolean>

    /**
     * Finishes the onboarding flow and sets the [isOnboardingFinished] flag to true.
     */
    suspend fun finishOnboarding()

    /**
     * Resets the onboarding flow and sets the [isOnboardingFinished] to false.
     */
    suspend fun resetOnboarding()
}
