package network.marsys.smarthome.shared.application.hub

import network.marsys.smarthome.domain.platform.PlatformEvent

fun interface SmartHomeEventHandler {
    suspend fun handle(event: SmartHomeEvent)

    abstract class For<T : Any>(
        private val select: (SmartHomeEvent) -> T?,
    ) : SmartHomeEventHandler {
        final override suspend fun handle(event: SmartHomeEvent) {
            select.invoke(event)?.let {
                on(event = it)
            }
        }

        @Suppress("FunctionNameMinLength")
        protected abstract suspend fun on(event: T)
    }
}

inline fun <reified E : SmartHomeEvent> event(): (SmartHomeEvent) -> E? =
    { it as? E }

inline fun <reified E : PlatformEvent> platform(): (SmartHomeEvent) -> E? =
    { (it as? SmartHomeEvent.Platform)?.event as? E }
