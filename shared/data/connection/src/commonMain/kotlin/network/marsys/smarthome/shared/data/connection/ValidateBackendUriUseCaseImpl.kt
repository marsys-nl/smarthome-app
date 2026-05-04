package network.marsys.smarthome.shared.data.connection

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.Url
import io.ktor.http.isSuccess
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import network.marsys.smarthome.shared.domain.connection.ValidateBackendUriUseCase
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.Result.Companion.fail
import network.marsys.smarthome.shared.library.core.Result.Companion.succeed
import network.marsys.smarthome.shared.library.core.onFailure

internal class ValidateBackendUriUseCaseImpl(
    private val client: HttpClient,
) : ValidateBackendUriUseCase {
    override suspend fun invoke(uri: String): Result<Unit, ValidateBackendUriUseCase.Reason> = try {
        val url = validateUri(uri)
            .onFailure { return it }

        with(client.get("${url}${HEALTH_ENDPOINT}")) {
            when (status.isSuccess()) {
                true -> succeed(with = Unit)
                false -> fail(with = ValidateBackendUriUseCase.Reason.InvalidBackend)
            }
        }
    } catch (_: SerializationException) {
        fail(with = ValidateBackendUriUseCase.Reason.InvalidBackend)
    } catch (_: IOException) {
        fail(with = ValidateBackendUriUseCase.Reason.Unreachable)
    } catch (_: IllegalArgumentException) {
        fail(with = ValidateBackendUriUseCase.Reason.Unreachable)
    }

    private fun validateUri(uri: String): Result<Url, ValidateBackendUriUseCase.Reason> = try {
        require(uri.isNotBlank()) {
            "URI cannot be blank"
        }

        val url = Url(uri)

        require(url.protocol.name in validSchemes) {
            "Invalid scheme: ${url.protocol.name}. Valid schemes are: $validSchemes"
        }

        require(url.host.contains('.') || url.host == "localhost") {
            "Host must contain a dot or be 'localhost'"
        }

        succeed(with = url)
    } catch (exception: IllegalArgumentException) {
        fail(with = ValidateBackendUriUseCase.Reason.InvalidUri(exception.message ?: "Invalid uri"))
    }

    companion object {
        private const val HEALTH_ENDPOINT = "api/health"

        private val validSchemes = setOf("http", "https")
    }
}
