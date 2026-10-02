package network.marsys.smarthome.shared.library.store.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.store.AppearancePreferencesRepository
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.model.ConnectionConfiguration

class SmartHomeStoreRepository(
    private val dataStore: DataStore<Preferences>,
) : AppearancePreferencesRepository, ApplicationConfigurationRepository {
    override val theme: Flow<ThemeSelection> =
        dataStore.data.map { preferences ->
            try {
                val theme = preferences[Keys.theme]
                    ?: return@map ThemeSelection.SystemDefault

                ThemeSelection.valueOf(theme)
            } catch (_: IllegalArgumentException) {
                ThemeSelection.SystemDefault
            }
        }

    override val connection: Flow<ConnectionConfiguration> =
        dataStore.data.map { preferences ->
            val apiKey = preferences[Keys.apiKey]
            val uri = preferences[Keys.backendUri]
            val isDemoMode = preferences[Keys.isDemoMode] ?: false

            when {
                isDemoMode ->
                    ConnectionConfiguration.Demo

                uri != null ->
                    ConnectionConfiguration.Backend(
                        uri = uri,
                        apiKey = apiKey,
                    )

                else ->
                    ConnectionConfiguration.Unconfigured
            }
        }

    override suspend fun setTheme(theme: ThemeSelection) {
        dataStore.edit { preferences ->
            preferences[Keys.theme] = theme.name
        }
    }

    override suspend fun setConnectionConfiguration(configuration: ConnectionConfiguration) {
        dataStore.edit { preferences ->
            preferences[Keys.isDemoMode] = configuration is ConnectionConfiguration.Demo

            when (configuration) {
                is ConnectionConfiguration.Unconfigured, is ConnectionConfiguration.Demo -> {
                    preferences.remove(Keys.backendUri)
                    preferences.remove(Keys.apiKey)
                }

                is ConnectionConfiguration.Backend -> {
                    preferences[Keys.backendUri] = configuration.uri

                    when (val apiKey = configuration.apiKey) {
                        null -> preferences.remove(Keys.apiKey)
                        else -> preferences[Keys.apiKey] = apiKey
                    }
                }
            }
        }
    }

    private companion object Keys {
        private val apiKey = stringPreferencesKey("config.api_key")
        private val backendUri = stringPreferencesKey("config.backend_uri")
        private val isDemoMode = booleanPreferencesKey("config.is_demo_mode")
        private val theme = stringPreferencesKey("appearance.theme")
    }
}
