package network.smarthome.shared.library.screens

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface SmartHomeScreen : NavKey {
    @Serializable
    data object Dashboard : SmartHomeScreen

    @Serializable
    data object Rooms : SmartHomeScreen

    @Serializable
    data object Scenes : SmartHomeScreen

    @Serializable
    data object Profile : SmartHomeScreen

    sealed interface Modal : SmartHomeScreen

    @Serializable
    data object AppAppearance : Modal
}
