package network.marsys.smarthome.shared.infrastructure.hub

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import network.marsys.smarthome.api.models.platform.PlatformEventMessage

@Serializable
sealed interface SmartHomeEventMessage {
    @Serializable
    @SerialName("sync-required")
    data class SyncRequired(
        val scope: Scope,
    ) : SmartHomeEventMessage {
        @Serializable
        enum class Scope {
            @SerialName("all")
            All,
        }
    }

    @Serializable
    @SerialName("platform-event")
    data class Platform(
        val event: PlatformEventMessage,
    ) : SmartHomeEventMessage
}
