package network.marsys.smarthome.shared.application.hub.ports.outbound

import kotlinx.coroutines.flow.Flow
import network.marsys.smarthome.shared.application.hub.SmartHomeEvent

interface SmartHomeEventSource {
    fun events(): Flow<SmartHomeEvent>
}

sealed class SmartHomeEventSourceException(
    cause: Throwable?,
) : Exception(cause) {
    class Unreachable(cause: Throwable?) : SmartHomeEventSourceException(cause)
    class Unauthorized(cause: Throwable?) : SmartHomeEventSourceException(cause)
    class Unknown(cause: Throwable?) : SmartHomeEventSourceException(cause)
}
