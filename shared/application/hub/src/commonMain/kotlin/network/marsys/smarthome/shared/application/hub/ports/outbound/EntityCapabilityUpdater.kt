package network.marsys.smarthome.shared.application.hub.ports.outbound

import network.marsys.smarthome.domain.capability.Capability
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.shared.library.core.Result

fun interface EntityCapabilityUpdater {
    suspend fun update(identifier: EntityIdentifier, capability: Capability<*, *>): Result<Unit, Reason>

    sealed interface Reason {
        data object NotSynchronized : Reason
        data class EntityNotFound(val entity: EntityIdentifier) : Reason
        data class EntityStateUnknown(val entity: EntityIdentifier) : Reason
        data class UnsupportedCapability(
            val entity: EntityIdentifier,
            val capability: Capability<*, *>,
        ) : Reason
    }
}
