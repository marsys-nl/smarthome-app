package network.marsys.smarthome.shared.domain.entity

import kotlinx.coroutines.flow.Flow
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.zone.Zone

interface EntityRepository {
    val zones: Flow<Collection<Zone>>

    val entities: Flow<Collection<Entity<*>>>

    fun entity(identifier: EntityIdentifier): Flow<Entity<*>?>

    suspend fun execute(action: Entity.Action)
}
