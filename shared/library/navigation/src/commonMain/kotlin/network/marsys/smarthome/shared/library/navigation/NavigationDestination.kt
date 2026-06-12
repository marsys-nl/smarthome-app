package network.marsys.smarthome.shared.library.navigation

import network.marsys.smarthome.domain.EntityIdentifier

sealed interface NavigationDestination {
    data object ChangeAppAppearanceModal : NavigationDestination

    data class EntityDetailModal(
        val entity: EntityIdentifier,
    ) : NavigationDestination
}
