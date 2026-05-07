package network.smarthome.shared.library

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface SmartHomeNavigationFlow : NavKey {
    @Serializable
    data object Onboarding : SmartHomeNavigationFlow

    @Serializable
    data object Main : SmartHomeNavigationFlow
}
