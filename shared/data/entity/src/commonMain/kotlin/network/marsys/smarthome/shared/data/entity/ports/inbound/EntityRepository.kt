package network.marsys.smarthome.shared.data.entity.ports.inbound

import kotlinx.coroutines.flow.Flow
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.library.core.Result

interface EntityRepository {
    val entities: Flow<Collection<Entity<*>>>

    fun entity(identifier: EntityIdentifier): Flow<Entity<*>?>

    suspend fun execute(action: Entity.Action)

    suspend fun synchronize(): Result<Unit, EntitySynchronization.Reason>
}
