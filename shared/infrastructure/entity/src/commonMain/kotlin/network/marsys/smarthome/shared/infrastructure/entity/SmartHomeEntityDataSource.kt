package network.marsys.smarthome.shared.infrastructure.entity

import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.ContentConvertException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.io.IOException
import network.marsys.smarthome.api.mappers.EntityCollectionDomainMapper
import network.marsys.smarthome.api.mappers.entity.action.EntityActionApiMapper
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.domain.capability.Capability
import network.marsys.smarthome.domain.capability.with
import network.marsys.smarthome.domain.entity.Entity
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.shared.application.hub.ports.outbound.EntityCapabilityUpdater
import network.marsys.smarthome.shared.data.entity.ports.inbound.EntitySynchronization
import network.marsys.smarthome.shared.data.entity.ports.outbound.EntityDataSource
import network.marsys.smarthome.shared.domain.entity.update
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.Result.Companion.fail
import network.marsys.smarthome.shared.library.core.Result.Companion.succeed

internal class SmartHomeEntityDataSource(
    private val client: HttpClient,
) : EntityDataSource, EntitySynchronization, EntityCapabilityUpdater {
    override val entities: StateFlow<Collection<Entity>>
        field = MutableStateFlow(emptyList())

    override fun entity(identifier: EntityIdentifier): Flow<Entity?> =
        entities.map { collection ->
            collection.find { entity -> entity.identifier == identifier }
        }

    override suspend fun execute(action: Entity.Action) {
        try {
            client.post("/api/entities/actions") {
                contentType(ContentType.Application.Json)
                setBody(EntityActionApiMapper.invoke(source = action))
            }
        } catch (_: Exception) {
            // No-op for now
        }
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
                    handle(response)
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

    override suspend fun update(
        identifier: EntityIdentifier,
        capability: Capability<*, *>,
    ): Result<Unit, EntityCapabilityUpdater.Reason> {
        val entity = entities.value
            .find { it.identifier == identifier }

        return when {
            entity == null ->
                fail(with = EntityCapabilityUpdater.Reason.EntityNotFound(entity = identifier))

            entity.state !is Entity.State.Known ->
                fail(with = EntityCapabilityUpdater.Reason.EntityStateUnknown(entity = identifier))

            entity.state.find(query = capability.type with capability.context) == null ->
                fail(
                    with = EntityCapabilityUpdater.Reason.UnsupportedCapability(
                        entity = identifier,
                        capability = capability,
                    ),
                )

            else -> succeed(with = Unit).also {
                entities.update { current ->
                    current.map { entity ->
                        when {
                            entity.identifier == identifier -> entity.update { state ->
                                state.updateWith(capability = capability)
                            }

                            else -> entity
                        }
                    }
                }
            }
        }
    }

    private fun handle(collection: Collection<EntityResponse>) {
        val mapped = EntityCollectionDomainMapper
            .invoke(source = collection)

        entities.update { current ->
            (mapped + current)
                .distinctBy { it.identifier }
        }
    }
}
