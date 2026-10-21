package network.marsys.smarthome.shared.domain.entity

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.zone.Zone

interface ZoneRepository {
    val zones: Flow<Collection<Zone>>

    fun zone(identifier: EntityIdentifier): Flow<Zone?>
}
