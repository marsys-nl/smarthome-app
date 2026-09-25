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
import network.marsys.smarthome.api.models.config.HealthResponse
import network.marsys.smarthome.shared.domain.connection.CheckSystemHealthUseCase
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.Result.Companion.fail
import network.marsys.smarthome.shared.library.core.Result.Companion.succeed
import network.marsys.smarthome.shared.library.core.onFailure
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository

internal class CheckSystemHealthUseCaseImpl(
    private val applicationConfigurationRepository: ApplicationConfigurationRepository,
    private val client: HttpClient,
) : CheckSystemHealthUseCase {
    override suspend fun invoke(): Result<Unit, CheckSystemHealthUseCase.Reason> = try {
        val apiKey = applicationConfigurationRepository.apiKey.first()
        val url = formatUri(uri = applicationConfigurationRepository.backendUri.first())
            .onFailure {
                return fail(with = CheckSystemHealthUseCase.Reason.Unreachable)
            }

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
                fail(with = CheckSystemHealthUseCase.Reason.Unauthenticated)

            else ->
                fail(with = CheckSystemHealthUseCase.Reason.Unhealthy)
        }
    } catch (_: NoTransformationFoundException) {
        fail(with = CheckSystemHealthUseCase.Reason.Unreachable)
    } catch (_: ContentConvertException) {
        fail(with = CheckSystemHealthUseCase.Reason.Unreachable)
    } catch (e: IOException) {
        println(e)
        fail(with = CheckSystemHealthUseCase.Reason.Unreachable)
    } catch (_: IllegalArgumentException) {
        fail(with = CheckSystemHealthUseCase.Reason.Unreachable)
    }

    companion object {
        private const val API_KEY_HEADER = "X-Api-Key"
        private const val HEALTH_ENDPOINT = "/api/health"
    }
}
