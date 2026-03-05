package network.marsys.smarthome.shared.feature.onboarding

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface OnboardingScreens : NavKey {
    @Serializable
    data object Initial : OnboardingScreens

    @Serializable
    data object Entities : OnboardingScreens

    @Serializable
    data object Appearance : OnboardingScreens

    @Serializable
    data object Configuration : OnboardingScreens

    companion object {
        const val SCREEN_COUNT = 4

        @Suppress("MagicNumber")
        internal fun indexOf(screen: OnboardingScreens) = when (screen) {
            Initial -> 1
            Entities -> 2
            Appearance -> 3
            Configuration -> 4
        }
    }
}
