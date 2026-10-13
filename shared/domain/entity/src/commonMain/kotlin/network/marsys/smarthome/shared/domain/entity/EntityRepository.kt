package network.marsys.smarthome.shared.domain.entity

import kotlinx.coroutines.flow.Flow
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.zone.Zone
import network.marsys.smarthome.shared.library.core.Result

interface EntityRepository {
    val zones: Flow<Collection<Zone>>

    val entities: Flow<Collection<Entity<*>>>

    fun entity(identifier: EntityIdentifier): Flow<Entity<*>?>

    suspend fun execute(action: Entity.Action)

    suspend fun synchronize(): Result<Unit, Reason>

    sealed interface Reason {
        data object InvalidData : Reason
        data object Unreachable : Reason
        data object Unauthenticated : Reason
    }
}
