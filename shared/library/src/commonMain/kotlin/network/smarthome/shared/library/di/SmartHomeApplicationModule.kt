package network.smarthome.shared.library.di

import network.marsys.smarthome.shared.library.store.AppearancePreferencesRepository
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.OnboardingRepository
import network.marsys.smarthome.shared.library.store.datastore.SmartHomeStoreRepository
import org.koin.dsl.binds
import org.koin.dsl.module

internal val smartHomeApplicationModule = module {
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
