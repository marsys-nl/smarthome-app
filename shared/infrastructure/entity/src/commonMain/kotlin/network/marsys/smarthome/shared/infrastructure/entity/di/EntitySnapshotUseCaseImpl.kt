package network.marsys.smarthome.shared.infrastructure.entity.di

import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.ContentConvertException
import kotlinx.io.IOException
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.EntitySnapshotUseCase
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.Light
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.Result.Companion.fail
import network.marsys.smarthome.shared.library.core.Result.Companion.succeed

class EntitySnapshotUseCaseImpl(
    private val client: HttpClient,
) : EntitySnapshotUseCase {
    override suspend fun invoke(): Result<List<Entity<*>>, EntitySnapshotUseCase.Reason> =
        try {
            val response = client.get("/api/entities") {
                timeout {
                    requestTimeoutMillis = 10_000
                }
            }

            when (response.status) {
                HttpStatusCode.OK ->
                    succeed(
                        with = response.body<List<EntityResponse>>()
                            .map {
                                Light(
                                    identifier = EntityIdentifier(value = it.identifier),
                                    state = Light.State.Unknown,
                                )
                            },
                    )

                HttpStatusCode.Unauthorized ->
                    fail(with = EntitySnapshotUseCase.Reason.Unauthenticated)

                else ->
                    fail(with = EntitySnapshotUseCase.Reason.Unreachable)
            }
        } catch (_: NoTransformationFoundException) {
            fail(with = EntitySnapshotUseCase.Reason.InvalidData)
        } catch (_: ContentConvertException) {
            fail(with = EntitySnapshotUseCase.Reason.InvalidData)
        } catch (_: IOException) {
            fail(with = EntitySnapshotUseCase.Reason.Unreachable)
        } catch (_: IllegalArgumentException) {
            fail(with = EntitySnapshotUseCase.Reason.Unreachable)
        }
}
