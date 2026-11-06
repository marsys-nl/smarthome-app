package network.marsys.smarthome.shared.data.entity.ports.inbound

import kotlinx.coroutines.flow.Flow
import network.marsys.smarthome.domain.zone.Zone
import network.marsys.smarthome.domain.zone.ZoneIdentifier

interface ZoneRepository {
    val zones: Flow<Collection<Zone>>

    fun zone(identifier: ZoneIdentifier): Flow<Zone?>
}
