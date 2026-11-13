package network.marsys.smarthome.shared.application.hub

import network.marsys.smarthome.domain.platform.PlatformEvent

sealed interface SmartHomeEvent {
    data class SyncRequired(
        val scope: Scope,
    ) : SmartHomeEvent

    data class Platform(
        val event: PlatformEvent,
    ) : SmartHomeEvent
}

sealed interface Scope {
    data object All : Scope
}
