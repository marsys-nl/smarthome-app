package network.marsys.smarthome.shared.library.network

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.Url
import io.ktor.http.encodedPath
import kotlinx.coroutines.flow.first
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.model.ConnectionConfiguration

internal val SmartHomeConnection = createClientPlugin(
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

internal class SmartHomeConnectionConfiguration {
    var applicationConfigurationRepository: ApplicationConfigurationRepository? = null
}
