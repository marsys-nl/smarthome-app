package network.marsys.smarthome.shared.domain.entity

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.zone.Zone

interface EntityRepository {
    val entities: Flow<Collection<Entity<*>>>

    fun entity(identifier: EntityIdentifier): StateFlow<Entity<*>?>

    suspend fun execute(action: Entity.Action)
}
