package network.marsys.smarthome.shared.feature.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.logo
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import org.jetbrains.compose.resources.painterResource

private val OnboardingBackgroundColor = Color(color = 0xFFF1BF42)

@Composable
fun OnboardingScreenView(
    modifier: Modifier = Modifier,
) {
    SmartHomeTheme(
        scheme = ColorScheme.lightColorScheme,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(OnboardingBackgroundColor),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Onboarding")
            }

            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "SmartHome logo",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(108.dp),
            )
        }
    }
}

@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun OnboardingScreenViewPreview() {
    OnboardingScreenView()
}
