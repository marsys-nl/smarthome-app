package network.smarthome.shared.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import network.marsys.smarthome.shared.feature.onboarding.OnboardingViewModel
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.store.AppearancePreferencesRepository
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

private val viewModelModule = module {
    viewModelOf(::OnboardingViewModel)
}

@Composable
fun SmartHomeApp(
    vararg applicationModules: Module,
) {
    KoinApplication(
        configuration = koinConfiguration {
            modules(*applicationModules, viewModelModule)
        },
    ) {
        val appearancePreferencesRepository = koinInject<AppearancePreferencesRepository>()
        val theme by appearancePreferencesRepository.theme
            .collectAsStateWithLifecycle(ThemeSelection.SystemDefault)

        SmartHomeTheme(
            theme = theme,
        ) {
            SmartHomeNavigation()
        }
    }
}
