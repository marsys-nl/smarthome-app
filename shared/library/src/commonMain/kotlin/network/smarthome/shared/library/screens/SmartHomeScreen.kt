package network.smarthome.shared.library.screens

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import network.marsys.smarthome.domain.EntityIdentifier

@Serializable
internal sealed interface SmartHomeScreen : NavKey {
    @Serializable
    data object Dashboard : SmartHomeScreen

    @Serializable
    data object Zones : SmartHomeScreen

    @Serializable
    data class Zone(
        val zone: @Contextual EntityIdentifier,
    ) : SmartHomeScreen

    @Serializable
    data object Scenes : SmartHomeScreen

    @Serializable
    data object Profile : SmartHomeScreen

    sealed interface Modal : SmartHomeScreen

    @Serializable
    data object AppAppearance : Modal

    @Serializable
    data class EntityDetails(
        val entity: @Contextual EntityIdentifier,
    ) : Modal
}
