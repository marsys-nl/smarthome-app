package network.marsys.smarthome.shared.library.navigation

import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.zone.ZoneIdentifier

sealed interface NavigationDestination {
    sealed interface MainNavigationDestination : NavigationDestination

    data object Zones : MainNavigationDestination

    data class Zone(val zone: ZoneIdentifier) : MainNavigationDestination

    data object ChangeAppAppearanceModal : NavigationDestination

    data class EntityDetailModal(
        val entity: EntityIdentifier,
    ) : NavigationDestination
}
