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
import network.marsys.smarthome.shared.library.store.OnboardingRepository

class SmartHomeStoreRepository(
    private val dataStore: DataStore<Preferences>,
) : AppearancePreferencesRepository, ApplicationConfigurationRepository, OnboardingRepository {
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

    override val backendUri: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[Keys.backendUri]
        }

    override val isDemoMode: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[Keys.isDemoMode] ?: false
        }

    override val isOnboardingFinished: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[Keys.isOnboardingFinished] ?: false
        }

    override suspend fun setTheme(theme: ThemeSelection) {
        dataStore.edit { preferences ->
            preferences[Keys.theme] = theme.name
        }
    }

    override suspend fun setBackendUri(uri: String) {
        dataStore.edit { preferences ->
            preferences[Keys.backendUri] = uri
        }
    }

    override suspend fun setDemoMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.isDemoMode] = enabled
        }
    }

    override suspend fun finishOnboarding() {
        dataStore.edit { preferences ->
            preferences[Keys.isOnboardingFinished] = true
        }
    }

    private companion object Keys {
        private val backendUri = stringPreferencesKey("config.backend_uri")
        private val isDemoMode = booleanPreferencesKey("config.is_demo_mode")
        private val isOnboardingFinished = booleanPreferencesKey("onboarding.is_finished")
        private val theme = stringPreferencesKey("appearance.theme")
    }
}
