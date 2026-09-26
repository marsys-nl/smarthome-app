package network.marsys.smarthome.shared.domain.connection

import network.marsys.smarthome.shared.library.core.Result

fun interface DownloadConfigurationUseCase {
    suspend operator fun invoke(): Result<Response, Reason>

    sealed interface Reason {
        data object Unreachable : Reason
        data object InvalidConfiguration : Reason
        data object Unauthenticated : Reason
    }

    data class Response(
        val authUri: String,
    )
}
