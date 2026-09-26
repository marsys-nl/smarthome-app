package network.marsys.smarthome.shared.domain.authentication

import network.marsys.smarthome.shared.library.core.Result

fun interface AuthenticateUseCase {
    suspend operator fun invoke(
        issuer: String,
        clientIdentifier: String,
    ): Result<Unit, Reason>

    sealed interface Reason {
        data object Cancelled : Reason
        data object Failed : Reason
        data object Unavailable : Reason
    }
}
