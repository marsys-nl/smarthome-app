package network.marsys.smarthome.shared.data.connection

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
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
    override suspend fun invoke(uri: String, apiKey: String?): Result<Unit, ValidateBackendUriUseCase.Reason> = try {
        val url = validateUri(uri)
            .onFailure { return it }

        val response = client.get("${url}${HEALTH_ENDPOINT}") {
            timeout {
                requestTimeoutMillis = 10_000
            }

            if (!apiKey.isNullOrBlank()) {
                header(API_KEY_HEADER, apiKey)
            }
        }

        when (response.status) {
            HttpStatusCode.OK if response.body<HealthResponse>().app == "SmartHomeBackend" ->
                succeed(with = Unit)

            HttpStatusCode.Unauthorized ->
                fail(with = ValidateBackendUriUseCase.Reason.Unauthenticated)

            else ->
                fail(with = ValidateBackendUriUseCase.Reason.InvalidBackend)
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

        val url = Url(
            urlString = uri.trim()
                .let { trimmed ->
                    if (!trimmed.contains("://")) "https://$trimmed" else trimmed
                }
                .trimEnd('/'),
        )

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
        private const val API_KEY_HEADER = "X-Api-Key"
        private const val HEALTH_ENDPOINT = "/api/health"

        private val validSchemes = setOf("http", "https")
    }
}
