package network.marsys.smarthome.shared.feature.onboarding

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface OnboardingScreens : NavKey {
    @Serializable
    data object Initial : OnboardingScreens

    @Serializable
    data object Entities : OnboardingScreens

    @Serializable
    data object Scenes : OnboardingScreens

    @Serializable
    data object Configuration : OnboardingScreens
}
