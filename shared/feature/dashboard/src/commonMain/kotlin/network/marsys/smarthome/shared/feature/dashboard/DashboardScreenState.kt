package network.marsys.smarthome.shared.feature.dashboard

import androidx.compose.runtime.Stable
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.zone.Zone
import network.marsys.smarthome.shared.library.navigation.NavigationDestination

@Stable
interface DashboardScreenState {
    val zonesState: ZonesState
    val quickControlState: QuickControlState
    val user: String

    @Stable
    interface QuickControlState {
        val condition: Condition
        val entities: Map<EntityIdentifier, Entity<*>>
        val groupedEntitiesByType: Boolean
    }

    @Stable
    interface ZonesState {
        val condition: Condition
        val zones: Map<EntityIdentifier, ZoneState>
    }

    @Stable
    interface ZoneState {
        val zone: Zone
        val entities: Map<EntityIdentifier, Entity<*>>
    }

    sealed interface Condition {
        data object Error : Condition
        data object Empty : Condition
        data object Loading : Condition
        data object Success : Condition
    }
}

sealed class DashboardScreenAction(val key: String) {
    data object ChangeAppAppearance : DashboardScreenAction("ChangeAppAppearance")

    data object NavigateToZones : DashboardScreenAction("NavigateToZones")

    data class OpenEntityDetailModal(
        val entity: EntityIdentifier,
    ) : DashboardScreenAction("EditEntity[$entity]")

    data class OpenZoneScreen(
        val zone: EntityIdentifier,
    ) : DashboardScreenAction("OpenZoneScreen[$zone]")

    data object RetryQuickControl : DashboardScreenAction("RetryQuickControl")

    data object RetryZones : DashboardScreenAction("RetryZones")

    data class ToggleEntityState(
        val entity: EntityIdentifier,
        val state: Boolean,
    ) : DashboardScreenAction("ToggleEntityState[$entity]")

    data object ToggleGroupEntitiesByType : DashboardScreenAction("ToggleGroupEntitiesByType")
}

sealed interface DashboardScreenEffect {
    data class Navigate(val target: NavigationDestination) : DashboardScreenEffect
}
