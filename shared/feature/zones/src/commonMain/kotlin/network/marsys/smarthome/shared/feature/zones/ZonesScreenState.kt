package network.marsys.smarthome.shared.feature.zones

import androidx.compose.runtime.Stable
import network.marsys.smarthome.domain.entity.Entity
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.zone.Zone
import network.marsys.smarthome.domain.zone.ZoneIdentifier
import network.marsys.smarthome.shared.library.navigation.NavigationDestination

@Stable
interface ZonesScreenState {
    val condition: Condition
    val zones: Map<ZoneIdentifier, ZoneState>

    @Stable
    interface ZoneState {
        val zone: Zone
        val entities: Map<EntityIdentifier, Entity>
    }

    sealed interface Condition {
        data object Error : Condition
        data object Empty : Condition
        data object Loading : Condition
        data object Success : Condition
    }
}

sealed class ZonesScreenAction(val key: String) {
    data class OpenZoneScreen(
        val zone: ZoneIdentifier,
    ) : ZonesScreenAction("OpenZoneScreen[$zone]")

    data object RetryZones : ZonesScreenAction("RetryZones")
}

sealed interface ZonesScreenEffect {
    data class Navigate(val target: NavigationDestination) : ZonesScreenEffect
}
