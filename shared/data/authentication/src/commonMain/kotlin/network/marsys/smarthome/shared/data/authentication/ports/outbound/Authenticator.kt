package network.marsys.smarthome.shared.data.authentication.ports.outbound

import network.marsys.smarthome.shared.library.core.Result

interface Authenticator {
    suspend fun authenticate(
        issuer: String,
        clientIdentifier: String,
    ): Result<Unit, Reason>

    suspend fun invalidate()

    sealed interface Reason {
        data object Cancelled : Reason
        data object Failed : Reason
        data object Unavailable : Reason
    }
}
