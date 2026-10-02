package network.marsys.smarthome.shared.library.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.Url
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import network.marsys.smarthome.shared.library.core.SmartHomeConfig
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.model.ConnectionConfiguration
import org.koin.core.qualifier.named
import org.koin.dsl.module

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

            install(SmartHomeConnection) {
                applicationConfigurationRepository = get()
            }
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

private class SmartHomeConnectionConfiguration {
    var applicationConfigurationRepository: ApplicationConfigurationRepository? = null
}

private val SmartHomeConnection = createClientPlugin(
    name = "SmartHomeConnection",
    createConfiguration = ::SmartHomeConnectionConfiguration,
) {
    val repository = requireNotNull(pluginConfig.applicationConfigurationRepository) {
        "Application configuration repository is not provided."
    }

    onRequest { request, _ ->
        when (val configuration = repository.connection.first()) {
            ConnectionConfiguration.Unconfigured ->
                error("Application isn't configured yet. Please configure the application first.")

            ConnectionConfiguration.Demo ->
                error("Backend is not available in demo mode. Please configure the application first.")

            is ConnectionConfiguration.Backend ->
                request.applyDefaultConfiguration(configuration = configuration)
        }
    }
}

private fun HttpRequestBuilder.applyDefaultConfiguration(
    configuration: ConnectionConfiguration.Backend,
) {
    val url = Url(configuration.uri)

    url {
        protocol = url.protocol
        host = url.host
        port = url.port

        encodedPath = url.encodedPath.trimEnd('/') + '/' +
            encodedPath.trimStart('/')
    }

    configuration.apiKey?.let { apiKey ->
        headers.append("X-API-Key", apiKey)
    }
}
