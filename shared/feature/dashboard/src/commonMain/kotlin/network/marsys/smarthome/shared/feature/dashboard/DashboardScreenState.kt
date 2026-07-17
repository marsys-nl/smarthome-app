package network.marsys.smarthome.shared.feature.dashboard

import androidx.compose.runtime.Stable
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.library.navigation.NavigationDestination

@Stable
interface DashboardScreenState {
    val quickControlState: QuickControlState
    val user: String

    @Stable
    interface QuickControlState {
        val condition: Condition
        val entities: Map<EntityIdentifier, Entity<*>>
        val groupedEntitiesByType: Boolean
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

    data class ToggleEntityState(
        val entity: EntityIdentifier,
        val state: Boolean,
    ) : DashboardScreenAction("ToggleEntityState[$entity]")

    data class OpenEntityDetailModal(
        val entity: EntityIdentifier,
    ) : DashboardScreenAction("EditEntity[$entity]")

    data object ToggleGroupEntitiesByType : DashboardScreenAction("ToggleGroupEntitiesByType")
}

sealed interface DashboardScreenEffect {
    data class Navigate(val target: NavigationDestination) : DashboardScreenEffect
}
