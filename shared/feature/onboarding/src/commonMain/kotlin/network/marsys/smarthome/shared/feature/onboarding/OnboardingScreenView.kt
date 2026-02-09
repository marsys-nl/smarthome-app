package network.marsys.smarthome.shared.feature.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.logo
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import org.jetbrains.compose.resources.painterResource

@Composable
fun OnboardingScreenView(
    modifier: Modifier = Modifier,
) {
    SmartHomeTheme(
        scheme = ColorScheme.lightColorScheme,
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(SmartHomeTheme.colors[ColorKeyToken.BackgroundBrandPrimary]),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Onboarding")

            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "SmartHome logo",
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
private fun OnboardingScreenViewPreview() {
    OnboardingScreenView()
}
