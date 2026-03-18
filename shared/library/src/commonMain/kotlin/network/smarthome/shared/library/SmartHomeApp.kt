package network.smarthome.shared.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import network.marsys.smarthome.shared.feature.onboarding.OnboardingScreenView
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import org.koin.compose.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.koinConfiguration

@Composable
fun SmartHomeApp(
    vararg applicationModules: Module
) {
    KoinApplication(
        configuration = koinConfiguration {
            modules(*applicationModules)
        },
    ) {
        val theme = retain { mutableStateOf(ThemeSelection.SystemDefault) }

        SmartHomeTheme(
            theme = theme.value,
        ) {
            OnboardingScreenView(
                onSelectTheme = {
                    theme.value = it
                },
            )
        }
    }
}
