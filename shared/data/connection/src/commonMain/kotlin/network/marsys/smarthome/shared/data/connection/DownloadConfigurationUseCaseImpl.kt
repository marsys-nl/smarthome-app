package network.marsys.smarthome.shared.data.connection

import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.ContentConvertException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.io.IOException
import network.marsys.smarthome.api.models.config.ConfigurationResponse
import network.marsys.smarthome.shared.domain.connection.DownloadConfigurationUseCase
import network.marsys.smarthome.shared.domain.connection.DownloadConfigurationUseCase.Reason
import network.marsys.smarthome.shared.domain.connection.DownloadConfigurationUseCase.Response
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.Result.Companion.fail
import network.marsys.smarthome.shared.library.core.Result.Companion.succeed
import network.marsys.smarthome.shared.library.core.onFailure
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository

internal class DownloadConfigurationUseCaseImpl(
    private val applicationConfigurationRepository: ApplicationConfigurationRepository,
    private val client: HttpClient,
) : DownloadConfigurationUseCase {
    override suspend fun invoke(): Result<Response, Reason> =
        try {
            val apiKey = applicationConfigurationRepository.apiKey.first()
            val url = formatUri(uri = applicationConfigurationRepository.backendUri.first())
                .onFailure {
                    return fail(with = Reason.Unreachable)
                }

            val response = client.get("${url}${CONFIG_ENDPOINT}") {
                timeout {
                    requestTimeoutMillis = 10_000
                }

                if (!apiKey.isNullOrBlank()) {
                    header(API_KEY_HEADER, apiKey)
                }
            }

            when (response.status) {
                HttpStatusCode.OK ->
                    handle(response = response.body())

                HttpStatusCode.Unauthorized ->
                    fail(with = Reason.Unauthenticated)

                else ->
                    fail(with = Reason.InvalidConfiguration)
            }
        } catch (_: NoTransformationFoundException) {
            fail(with = Reason.InvalidConfiguration)
        } catch (_: ContentConvertException) {
            fail(with = Reason.InvalidConfiguration)
        } catch (_: IOException) {
            fail(with = Reason.Unreachable)
        } catch (_: IllegalArgumentException) {
            fail(with = Reason.Unreachable)
        }

    private fun handle(response: ConfigurationResponse): Result<Response, Reason> =
        succeed(with = Response(authUri = response.authUri))

    companion object {
        private const val API_KEY_HEADER = "X-Api-Key"
        private const val CONFIG_ENDPOINT = "/api/config"
    }
}
