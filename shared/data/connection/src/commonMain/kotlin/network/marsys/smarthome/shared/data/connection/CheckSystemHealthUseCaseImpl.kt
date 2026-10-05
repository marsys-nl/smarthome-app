package network.marsys.smarthome.shared.data.connection

import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.ContentConvertException
import kotlinx.io.IOException
import network.marsys.smarthome.api.models.config.HealthResponse
import network.marsys.smarthome.shared.domain.connection.CheckSystemHealthUseCase
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.Result.Companion.fail
import network.marsys.smarthome.shared.library.core.Result.Companion.succeed
import network.marsys.smarthome.shared.library.network.SmartHomeInitializationRequestAttributeKey

internal class CheckSystemHealthUseCaseImpl(
    private val client: HttpClient,
) : CheckSystemHealthUseCase {
    override suspend fun invoke(): Result<Unit, CheckSystemHealthUseCase.Reason> = try {
        val response = client.get("/api/health") {
            attributes.put(SmartHomeInitializationRequestAttributeKey, true)

            timeout {
                requestTimeoutMillis = 10_000
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
    } catch (_: IOException) {
        fail(with = CheckSystemHealthUseCase.Reason.Unreachable)
    } catch (_: IllegalArgumentException) {
        fail(with = CheckSystemHealthUseCase.Reason.Unreachable)
    }
}
