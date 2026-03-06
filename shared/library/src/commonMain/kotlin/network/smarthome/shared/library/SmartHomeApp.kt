package network.smarthome.shared.library

import androidx.compose.runtime.Composable
import network.marsys.smarthome.shared.feature.onboarding.OnboardingScreenView
import network.marsys.smarthome.shared.library.design.SmartHomeTheme

@Composable
fun SmartHomeApp() {
    SmartHomeTheme {
        OnboardingScreenView()
    }
}
