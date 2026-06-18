package network.smarthome.shared.library.di

import network.marsys.smarthome.shared.library.i18n.TranslationCache
import network.marsys.smarthome.shared.library.i18n.memory.InMemoryTranslationCache
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

    single<TranslationCache> {
        InMemoryTranslationCache(
            translations = DemoEntityTranslations,
        )
    }
}

private val DemoEntityTranslations = mapOf(
    "light.bedroom-lamp" to mapOf(
        "en" to "Bedroom",
        "nl" to "Slaapkamer",
    ),
    "light.kitchen-light" to mapOf(
        "en" to "Kitchen",
        "nl" to "Keuken",
    ),
    "light.ceiling-light" to mapOf(
        "en" to "Ceiling hallway",
        "nl" to "Plafond hal",
    ),
    "thermostat.office" to mapOf(
        "en" to "Office",
        "nl" to "Kantoor",
    ),
    "thermostat.main-bedroom" to mapOf(
        "en" to "Main bedroom",
        "nl" to "Hoofdslaapkamer",
    ),
    "plug.office-plug" to mapOf(
        "en" to "Office",
        "nl" to "Kantoor",
    ),
    "plug.smart-tv" to mapOf(
        "en" to "Smart TV",
    ),
    "blind.living-room" to mapOf(
        "en" to "Living room",
        "nl" to "Woonkamer",
    ),
    "fan.bedroom" to mapOf(
        "en" to "Bedroom",
        "nl" to "Slaapkamer",
    ),
    "speaker.kitchen" to mapOf(
        "en" to "Kitchen",
        "nl" to "Keuken",
    ),
    "camera.front-door" to mapOf(
        "en" to "Front door",
        "nl" to "Voordeur",
    ),
    "lock.front-door" to mapOf(
        "en" to "Front door",
        "nl" to "Voordeur",
    ),
)
