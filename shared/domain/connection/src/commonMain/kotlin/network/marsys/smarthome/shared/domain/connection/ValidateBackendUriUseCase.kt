package network.marsys.smarthome.shared.domain.connection

fun interface ValidateBackendUriUseCase {
    suspend operator fun invoke(uri: String): Boolean
}
