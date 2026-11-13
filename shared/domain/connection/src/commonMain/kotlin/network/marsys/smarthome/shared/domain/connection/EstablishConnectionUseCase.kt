package network.marsys.smarthome.shared.domain.connection

import network.marsys.smarthome.shared.library.core.Result

fun interface EstablishConnectionUseCase {
    suspend operator fun invoke(): Result<Unit, Reason>

    sealed interface Reason {
        data object Unreachable : Reason
        data object Unauthorized : Reason
        data object Timeout : Reason
        data object Unknown : Reason
    }
}
