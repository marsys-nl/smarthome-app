package network.marsys.smarthome.shared.domain.entity

import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.library.core.Result

fun interface EntitySnapshotUseCase {
    suspend operator fun invoke(): Result<List<Entity<*>>, Reason>

    sealed interface Reason {
        data object InvalidData : Reason
        data object Unreachable : Reason
        data object Unauthenticated : Reason
    }
}
