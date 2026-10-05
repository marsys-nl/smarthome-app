package network.marsys.smarthome.shared.data.connection

import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.ContentConvertException
import kotlinx.io.IOException
import network.marsys.smarthome.shared.domain.connection.SynchronizeEntitiesUseCase
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.Result.Companion.fail

class SynchronizeEntitiesUseCaseImpl(
    private val client: HttpClient,
) : SynchronizeEntitiesUseCase {
    override suspend fun invoke(): Result<Unit, SynchronizeEntitiesUseCase.Reason> =
        try {
            val response = client.get("/api/entities") {
                timeout {
                    requestTimeoutMillis = 10_000
                }
            }

            when (response.status) {
                HttpStatusCode.OK -> Result.succeed(with = Unit)

                HttpStatusCode.Unauthorized ->
                    fail(with = SynchronizeEntitiesUseCase.Reason.Unauthenticated)

                else ->
                    fail(with = SynchronizeEntitiesUseCase.Reason.Unreachable)
            }
        } catch (_: NoTransformationFoundException) {
            fail(with = SynchronizeEntitiesUseCase.Reason.Unreachable)
        } catch (_: ContentConvertException) {
            fail(with = SynchronizeEntitiesUseCase.Reason.Unreachable)
        } catch (_: IOException) {
            fail(with = SynchronizeEntitiesUseCase.Reason.Unreachable)
        } catch (_: IllegalArgumentException) {
            fail(with = SynchronizeEntitiesUseCase.Reason.Unreachable)
        }
}
