package network.smarthome.shared.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import network.marsys.smarthome.shared.feature.onboarding.OnboardingScreenView
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
        val theme by appearancePreferencesRepository.theme.collectAsState(ThemeSelection.SystemDefault)

        SmartHomeTheme(
            theme = theme,
        ) {
            OnboardingScreenView()
        }
    }
}
