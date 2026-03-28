package network.smarthome.shared.library.screens

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface SmartHomeScreens : NavKey {
    @Serializable
    data object Onboarding : SmartHomeScreens

    @Serializable
    data object Dashboard : SmartHomeScreens
}
