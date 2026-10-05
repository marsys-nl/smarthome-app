package network.marsys.smarthome.shared.library.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import network.marsys.smarthome.shared.library.core.SmartHomeConfig
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.ExperimentalOpenIdConnect

val networkModule = module {
    single(qualifier = named("genericHttpClient")) {
        HttpClient(engineFactory = httpClientEngine()) {
            installContentNegotiation()
            installLogging()
        }
    }

    single(qualifier = named("smarthomeHttpClient")) {
        HttpClient(engineFactory = httpClientEngine()) {
            installContentNegotiation()
            installLogging()
            installSmartHomePlugins()
        }
    }
}

private fun HttpClientConfig<*>.installContentNegotiation() {
    install(ContentNegotiation) {
        json(
            json = Json {
                ignoreUnknownKeys = true
            },
        )
    }
}

private fun HttpClientConfig<*>.installLogging() {
    if (SmartHomeConfig.DEBUG) {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }
}

@OptIn(ExperimentalOpenIdConnect::class)
context(scope: Scope)
private fun HttpClientConfig<*>.installSmartHomePlugins() {
    install(SmartHomeAuthentication) {
        store = scope.get()
        refreshHandler = scope.get()
        provider = scope.get()
    }

    install(SmartHomeConnection) {
        applicationConfigurationRepository = scope.get()
    }
}
