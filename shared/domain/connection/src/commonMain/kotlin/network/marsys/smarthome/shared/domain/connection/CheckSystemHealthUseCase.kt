package network.marsys.smarthome.shared.domain.connection

import network.marsys.smarthome.shared.library.core.Result

fun interface CheckSystemHealthUseCase {
    suspend operator fun invoke(): Result<Unit, Reason>

    sealed interface Reason {
        data object Unhealthy : Reason
        data object Unreachable : Reason
        data object Unauthenticated : Reason
    }
}
