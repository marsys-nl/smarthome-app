package network.marsys.smarthome.shared.library.network

import io.ktor.client.engine.HttpClientEngineFactory

internal expect fun httpClientEngine(): HttpClientEngineFactory<*>
