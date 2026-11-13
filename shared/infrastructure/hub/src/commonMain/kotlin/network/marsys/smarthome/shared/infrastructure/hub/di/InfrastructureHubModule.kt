package network.marsys.smarthome.shared.infrastructure.hub.di

import kotlinx.serialization.json.Json
import network.marsys.smarthome.shared.application.hub.di.applicationHubModule
import network.marsys.smarthome.shared.application.hub.ports.outbound.SmartHomeEventSource
import network.marsys.smarthome.shared.infrastructure.hub.ServerSentEventSmartHomeEventSource
import org.koin.core.qualifier.named
import org.koin.dsl.binds
import org.koin.dsl.module

val infrastructureHubModule = module {
    includes(
        applicationHubModule,
    )

    single {
        ServerSentEventSmartHomeEventSource(
            client = get(named("smarthomeHttpClient")),
            json = Json.Default,
        )
    } binds arrayOf(
        SmartHomeEventSource::class,
    )
}
