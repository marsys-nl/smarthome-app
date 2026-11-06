package network.marsys.smarthome.shared.data.entity.ports.outbound

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import network.marsys.smarthome.domain.entity.Entity
import network.marsys.smarthome.domain.entity.EntityIdentifier

interface EntityDataSource {
    val entities: StateFlow<Collection<Entity>>

    fun entity(identifier: EntityIdentifier): Flow<Entity?>

    suspend fun execute(action: Entity.Action)
}
