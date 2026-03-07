package network.smarthome.shared.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import network.marsys.smarthome.shared.feature.onboarding.OnboardingScreenView
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection

@Composable
fun SmartHomeApp() {
    val theme = retain { mutableStateOf(ThemeSelection.SystemDefault) }

    SmartHomeTheme(
        theme = theme.value,
    ) {
        OnboardingScreenView(
            onThemeSelected = {
                theme.value = it
            },
        )
    }
}
