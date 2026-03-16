package network.marsys.smarthome.shared.library.store

import kotlinx.coroutines.flow.Flow

/**
 * Persisted configuration required for the application to connect and operate.
 */
interface ApplicationConfigurationRepository {
    /**
     * The backend URI to connect to.
     * Null until the user completed the backend configuration
     */
    val backendUri: Flow<String?>

    /**
     * Whether the demo mode is enabled.
     * True when the user chose to skip the backend configuration and enter demo mode.
     * Mutually exclusive with [backendUri] in practice, but stored independently.
     */
    val isDemoMode: Flow<Boolean>

    /**
     * Stores the [backendUri] to connect to.
     */
    suspend fun setBackendUri(uri: String)

    /**
     * Stores whether the demo mode is enabled.
     */
    suspend fun setDemoMode(enabled: Boolean)
}
