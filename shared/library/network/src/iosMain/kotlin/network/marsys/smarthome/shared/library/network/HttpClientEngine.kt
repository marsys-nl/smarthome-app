package network.marsys.smarthome.shared.library.network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

internal actual fun httpClientEngine(): HttpClientEngineFactory<*> = Darwin
