package network.marsys.smarthome.shared.infrastructure.hub

import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.SSEClientException
import io.ktor.client.plugins.sse.sse
import io.ktor.http.HttpStatusCode
import io.ktor.sse.ServerSentEvent
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import network.marsys.smarthome.api.mappers.PlatformEventDomainMapper
import network.marsys.smarthome.api.models.platform.PlatformEventMessage
import network.marsys.smarthome.shared.application.hub.Scope
import network.marsys.smarthome.shared.application.hub.SmartHomeEvent
import network.marsys.smarthome.shared.application.hub.ports.outbound.SmartHomeEventSource
import network.marsys.smarthome.shared.application.hub.ports.outbound.SmartHomeEventSourceException

internal class ServerSentEventSmartHomeEventSource(
    private val client: HttpClient,
    private val json: Json = Json { ignoreUnknownKeys = true },
) : SmartHomeEventSource {
    override fun events(): Flow<SmartHomeEvent> = channelFlow {
        try {
            client.sse(urlString = "/api/events") {
                incoming.collect { event ->
                    handle(event = event)
                }
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: SSEClientException) {
            throw exception.map()
        } catch (exception: IOException) {
            throw SmartHomeEventSourceException.Unreachable(exception)
        } catch (@Suppress("TooGenericExceptionCaught") exception: Exception) {
            throw SmartHomeEventSourceException.Unknown(exception)
        }
    }

    private suspend fun ProducerScope<SmartHomeEvent>.handle(event: ServerSentEvent) {
        val parsed = try {
            parse(event = event)
        } catch (_: SerializationException) {
            null
        } catch (_: IllegalArgumentException) {
            null
        }

        parsed?.let { event ->
            send(element = event)
        }
    }

    private fun parse(event: ServerSentEvent): SmartHomeEvent? {
        val data = event.data
            ?: return null

        println("Event data received: $data")

        return when (event.event) {
            "sync-required" ->
                json.decodeFromString<SmartHomeEventMessage.SyncRequired>(string = data)
                    .let { message ->
                        SmartHomeEvent.SyncRequired(
                            scope = when (message.scope) {
                                SmartHomeEventMessage.SyncRequired.Scope.All ->
                                    Scope.All
                            },
                        )
                    }

            "platform-event" ->
                json.decodeFromString<PlatformEventMessage>(string = data)
                    .let { message ->
                        SmartHomeEvent.Platform(
                            event = PlatformEventDomainMapper.invoke(source = message),
                        )
                    }

            else -> null
        }
    }

    private fun SSEClientException.map() =
        when {
            response?.status == HttpStatusCode.Unauthorized || response?.status == HttpStatusCode.Forbidden ->
                SmartHomeEventSourceException.Unauthorized(this)

            cause is IOException || response == null ->
                SmartHomeEventSourceException.Unreachable(this)

            else ->
                SmartHomeEventSourceException.Unknown(this)
        }
}
