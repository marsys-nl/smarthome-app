package network.smarthome.shared.library

import androidx.compose.ui.window.ComposeUIViewController
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import network.marsys.smarthome.shared.library.store.AppearancePreferencesRepository
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.OnboardingRepository
import network.marsys.smarthome.shared.library.store.datastore.SmartHomeStoreRepository
import network.marsys.smarthome.shared.library.store.datastore.createDataStore
import org.koin.dsl.binds
import org.koin.dsl.module

val applicationModule = module {
    single<DataStore<Preferences>> {
        createDataStore()
    }

    single {
        SmartHomeStoreRepository(
            dataStore = get(),
        )
    } binds arrayOf(
        AppearancePreferencesRepository::class,
        ApplicationConfigurationRepository::class,
        OnboardingRepository::class,
    )
}

@Suppress("unused")
fun mainViewController() = ComposeUIViewController {
    SmartHomeApp(
        applicationModules = arrayOf(applicationModule),
    )
}
