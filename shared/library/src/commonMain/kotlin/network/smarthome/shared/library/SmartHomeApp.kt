package network.smarthome.shared.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import network.marsys.smarthome.shared.data.connection.connectionDataModule
import network.marsys.smarthome.shared.data.entity.entityDataModule
import network.marsys.smarthome.shared.feature.dashboard.DashboardViewModel
import network.marsys.smarthome.shared.feature.onboarding.OnboardingViewModel
import network.marsys.smarthome.shared.feature.profile.ProfileViewModel
import network.marsys.smarthome.shared.library.core.coroutines.viewModelCoroutineScope
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.i18n.LocalDescriptorStrings
import network.marsys.smarthome.shared.library.i18n.LocalTranslationCache
import network.marsys.smarthome.shared.library.i18n.TranslationCache
import network.marsys.smarthome.shared.library.i18n.rememberDescriptorStrings
import network.marsys.smarthome.shared.library.network.networkModule
import network.marsys.smarthome.shared.library.store.AppearancePreferencesRepository
import network.marsys.smarthome.shared.modal.entity.EntityDetailModalViewModel
import network.smarthome.shared.library.di.smartHomeApplicationModule
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

private val viewModelModule = module {
    viewModel {
        DashboardViewModel(
            applicationConfigurationRepository = get(),
            entityRepository = get(),
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
        ProfileViewModel(
            applicationConfigurationRepository = get(),
            coroutineScope = viewModelCoroutineScope(),
        )
    }

    viewModel {
        EntityDetailModalViewModel(
            identifier = it.get(),
            entityRepository = get(),
            coroutineScope = viewModelCoroutineScope(),
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
                entityDataModule,
                connectionDataModule,
                networkModule,
                smartHomeApplicationModule,
                viewModelModule,
            )
        },
    ) {
        val translationCache = koinInject<TranslationCache>()
        val descriptorStrings = rememberDescriptorStrings()

        val appearancePreferencesRepository = koinInject<AppearancePreferencesRepository>()
        val theme by appearancePreferencesRepository.theme
            .collectAsStateWithLifecycle(ThemeSelection.SystemDefault)

        CompositionLocalProvider(
            LocalDescriptorStrings provides descriptorStrings,
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
