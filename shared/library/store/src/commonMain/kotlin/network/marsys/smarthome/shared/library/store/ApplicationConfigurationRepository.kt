package network.marsys.smarthome.shared.library.store

import kotlinx.coroutines.flow.Flow
import network.marsys.smarthome.shared.library.store.model.ConnectionConfiguration

/**
 * Persisted configuration required for the application to connect and operate.
 */
interface ApplicationConfigurationRepository {
    /**
     * The current application configuration, which is persisted across app restarts.
     */
    val connection: Flow<ConnectionConfiguration>

    /**
     * Stores the connection configuration.
     */
    suspend fun setConnectionConfiguration(configuration: ConnectionConfiguration)
}
