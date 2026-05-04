package network.marsys.smarthome.shared.data.connection

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url
import io.ktor.http.isSuccess
import io.ktor.serialization.ContentConvertException
import kotlinx.io.IOException
import network.marsys.smarthome.api.models.config.HealthResponse
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
            val response = body<HealthResponse>()

            when {
                status.isSuccess() && response.app == "SmartHomeBackend" ->
                    succeed(with = Unit)

                else -> fail(with = ValidateBackendUriUseCase.Reason.InvalidBackend)
            }
        }
    } catch (_: ContentConvertException) {
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
        fail(with = ValidateBackendUriUseCase.Reason.InvalidUri(exception.message))
    }

    companion object {
        private const val HEALTH_ENDPOINT = "api/health"

        private val validSchemes = setOf("http", "https")
    }
}
