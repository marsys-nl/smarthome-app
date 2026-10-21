package network.marsys.smarthome.shared.data.entity.ports.outbound

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity

interface EntityDataSource {
    val entities: Flow<Collection<Entity<*>>>

    fun entity(identifier: EntityIdentifier): StateFlow<Entity<*>?>

    suspend fun execute(action: Entity.Action)
}
