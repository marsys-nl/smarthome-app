package network.marsys.smarthome.shared.feature.zone

import androidx.compose.runtime.Stable
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.library.navigation.NavigationDestination

@Stable
internal interface ZoneScreenState {
    val condition: Condition
    val zone: EntityIdentifier
    val entities: Map<EntityIdentifier, Entity<*>>

    sealed interface Condition {
        data object Error : Condition
        data object Empty : Condition
        data object Loading : Condition
        data object Success : Condition
    }
}

internal sealed class ZoneScreenAction(val key: String) {
    data object NavigateToZones : ZoneScreenAction("NavigateToZones")

    data class OpenEntityDetailModal(
        val entity: EntityIdentifier,
    ) : ZoneScreenAction("EditEntity[$entity]")

    data object RetryLoadingEntities : ZoneScreenAction("RetryLoadingEntities")
}

internal sealed interface ZoneScreenEffect {
    data class Navigate(val target: NavigationDestination) : ZoneScreenEffect
}
