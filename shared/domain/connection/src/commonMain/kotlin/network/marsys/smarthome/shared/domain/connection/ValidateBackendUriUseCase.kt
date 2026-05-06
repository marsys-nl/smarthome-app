package network.marsys.smarthome.shared.domain.connection

import network.marsys.smarthome.shared.library.core.Result

fun interface ValidateBackendUriUseCase {
    suspend operator fun invoke(uri: String, apiKey: String?): Result<Unit, Reason>

    sealed interface Reason {
        data class InvalidUri(val message: String?) : Reason
        data object Unreachable : Reason
        data object InvalidBackend : Reason
        data object Unauthenticated : Reason
    }
}
