package network.smarthome.shared.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import network.marsys.smarthome.shared.data.connection.connectionDataModule
import network.marsys.smarthome.shared.feature.dashboard.DashboardViewModel
import network.marsys.smarthome.shared.feature.dashboard.demo.DemoEntityTranslations
import network.marsys.smarthome.shared.feature.onboarding.OnboardingViewModel
import network.marsys.smarthome.shared.library.core.coroutines.viewModelCoroutineScope
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.i18n.LocalTranslationCache
import network.marsys.smarthome.shared.library.i18n.TranslationCache
import network.marsys.smarthome.shared.library.i18n.memory.InMemoryTranslationCache
import network.marsys.smarthome.shared.library.network.networkModule
import network.marsys.smarthome.shared.library.store.AppearancePreferencesRepository
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.OnboardingRepository
import network.marsys.smarthome.shared.library.store.datastore.SmartHomeStoreRepository
import network.marsys.smarthome.shared.modal.entity.EntityDetailModalViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.binds
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

private val viewModelModule = module {
    viewModel {
        DashboardViewModel(
            applicationConfigurationRepository = get(),
            coroutineScope = viewModelCoroutineScope(),
        )
    }

    viewModel {
        OnboardingViewModel(
            appearancePreferencesRepository = get(),
            applicationConfigurationRepository = get(),
            onboardingRepository = get(),
            validateBackendUriUseCase = get(),
        )
    }

    viewModel {
        EntityDetailModalViewModel(
            identifier = it.get(),
            coroutineScope = viewModelCoroutineScope(),
        )
    }
}

private val smartHomeApplicationModule = module {
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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SmartHomeApp(
    applicationModule: Module,
) {
    KoinApplication(
        configuration = koinConfiguration {
            modules(
                applicationModule,
                connectionDataModule,
                networkModule,
                smartHomeApplicationModule,
                viewModelModule,
            )
        },
    ) {
        val translationCache = koinInject<TranslationCache>()

        val appearancePreferencesRepository = koinInject<AppearancePreferencesRepository>()
        val theme by appearancePreferencesRepository.theme
            .collectAsStateWithLifecycle(ThemeSelection.SystemDefault)

        CompositionLocalProvider(
            LocalTranslationCache provides translationCache,
        ) {
            SmartHomeTheme(
                theme = theme,
            ) {
                SmartHomeNavigation()
            }
        }
    }
}
