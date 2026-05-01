package network.marsys.smarthome.shared.data.connection

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlinx.io.IOException
import network.marsys.smarthome.shared.domain.connection.ValidateBackendUriUseCase

internal class ValidateBackendUriUseCaseImpl(
    private val client: HttpClient,
) : ValidateBackendUriUseCase {
    override suspend fun invoke(uri: String): Boolean =
        try {
            if (uri.isBlank()) {
                return false
            }

            with(client.get(uri)) {
                status.isSuccess()
            }
        } catch (_: IOException) {
            false
        } catch (_: IllegalArgumentException) {
            false
        }
}
