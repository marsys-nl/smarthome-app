package network.marsys.smarthome.shared.library.store.model

sealed interface ConnectionConfiguration {
    data object Unconfigured : ConnectionConfiguration
    data object Demo : ConnectionConfiguration

    data class Backend(
        val uri: String,
        val apiKey: String?,
    ) : ConnectionConfiguration
}
