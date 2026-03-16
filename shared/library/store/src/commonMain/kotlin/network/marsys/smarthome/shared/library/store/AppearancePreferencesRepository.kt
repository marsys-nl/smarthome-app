package network.marsys.smarthome.shared.library.store

import kotlinx.coroutines.flow.Flow
import network.marsys.smarthome.shared.library.design.ThemeSelection

/**
 * Stores user appearance preferences.
 */
interface AppearancePreferencesRepository {
    /**
     * Which theme to use.
     */
    val theme: Flow<ThemeSelection>

    /**
     * Sets the theme to use.
     */
    suspend fun setTheme(theme: ThemeSelection)
}
