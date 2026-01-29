package network.marsys.smarthome.shared.feature.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

private val OnboardingBackgroundColor = Color(0xFFF1BF42)

@Composable
fun OnboardingScreenView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OnboardingBackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BasicText("Onboarding")

        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = "SmartHome logo",
        )
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
private fun OnboardingScreenViewPreview() {
    OnboardingScreenView()
}
