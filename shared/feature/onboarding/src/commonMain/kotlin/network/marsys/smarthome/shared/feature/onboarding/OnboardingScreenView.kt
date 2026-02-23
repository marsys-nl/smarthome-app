package network.marsys.smarthome.shared.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import network.marsys.smarthome.shared.feature.onboarding.screens.InitialOnboardingScreenView
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes

@Composable
fun OnboardingScreenView(
    modifier: Modifier = Modifier,
) {
    InitialOnboardingScreenView(
        modifier = modifier,
    )
}

@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun OnboardingScreenViewPreview() {
    OnboardingScreenView()
}
