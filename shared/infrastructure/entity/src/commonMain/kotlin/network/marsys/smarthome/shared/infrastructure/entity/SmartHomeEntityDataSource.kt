package network.marsys.smarthome.shared.infrastructure.entity

import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.ContentConvertException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.data.entity.ports.inbound.EntitySynchronization
import network.marsys.smarthome.shared.data.entity.ports.outbound.EntityDataSource
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.Result.Companion.fail

internal class SmartHomeEntityDataSource(
    private val client: HttpClient,
) : EntityDataSource, EntitySynchronization {
    override val entities: StateFlow<Collection<Entity<*>>>
        field = MutableStateFlow(emptyList())

    override fun entity(identifier: EntityIdentifier): Flow<Entity<*>?> =
        entities.map { collection ->
            collection.find { entity -> entity.identifier == identifier }
        }

    override suspend fun execute(action: Entity.Action) {
        // No-op for now.
    }

    override suspend fun synchronize(): Result<Unit, EntitySynchronization.Reason> =
        try {
            val response = client.get("/api/entities") {
                timeout {
                    requestTimeoutMillis = 10_000
                }
            }

            when (response.status) {
                HttpStatusCode.OK -> response.body<Collection<EntityResponse>>().let { response ->
                    println(response)

                    Result.succeed(with = Unit)
                }

                HttpStatusCode.Unauthorized ->
                    fail(with = EntitySynchronization.Reason.Unauthenticated)

                else ->
                    fail(with = EntitySynchronization.Reason.Unreachable)
            }
        } catch (_: NoTransformationFoundException) {
            fail(with = EntitySynchronization.Reason.Unreachable)
        } catch (_: ContentConvertException) {
            fail(with = EntitySynchronization.Reason.Unreachable)
        } catch (_: IOException) {
            fail(with = EntitySynchronization.Reason.Unreachable)
        } catch (_: IllegalArgumentException) {
            fail(with = EntitySynchronization.Reason.Unreachable)
        }
}
