package network.marsys.smarthome.shared.data.entity.ports.inbound

import network.marsys.smarthome.shared.library.core.Result

fun interface EntitySynchronization {
    suspend fun synchronize(): Result<Unit, Reason>

    sealed interface Reason {
        data object Unreachable : Reason
        data object Unauthenticated : Reason
    }
}
