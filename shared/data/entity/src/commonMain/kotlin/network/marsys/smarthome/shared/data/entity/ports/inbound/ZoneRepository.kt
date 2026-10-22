package network.marsys.smarthome.shared.data.entity.ports.inbound

import kotlinx.coroutines.flow.Flow
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.zone.Zone

interface ZoneRepository {
    val zones: Flow<Collection<Zone>>

    fun zone(identifier: EntityIdentifier): Flow<Zone?>
}
