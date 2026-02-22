package network.marsys.smarthome.shared.feature.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingLoadingIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingNextButton
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingPageIndicator
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.logo
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.ButtonDefaults
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import org.jetbrains.compose.resources.painterResource

private val OnboardingBackgroundColor = Color(color = 0xFFF1BF42)

private val OnboardingInitialScreenButtonBackgroundColor = PaletteTokens.Slate.Slate800
private val OnboardingInitialScreenButtonTextColor = PaletteTokens.Base.White
private val OnboardingInitialScreenProgressIndicatorBackgroundColor = PaletteTokens.Amber.Amber600
    .copy(alpha = .3f)
private val OnboardingInitialScreenProgressIndicatorForegroundColor = PaletteTokens.Slate.Slate800

private val OnboardingInitialScreenPageIndicatorActiveBackgroundColor = PaletteTokens.Slate.Slate800
private val OnboardingInitialScreenPageIndicatorInactiveBackgroundColor = PaletteTokens.Amber.Amber600.copy(alpha = .3f)

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
                    .padding(40.dp)
                    .widthIn(max = 400.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OnboardingLoadingIndicator(
                    progress = .2f,
                    backgroundColor = OnboardingInitialScreenProgressIndicatorBackgroundColor,
                    foregroundColor = OnboardingInitialScreenProgressIndicatorForegroundColor,
                    modifier = Modifier
                        .padding(bottom = 40.dp),
                )

                OnboardingTitles()

                Spacer(
                    modifier = Modifier
                        .weight(1f),
                )

                OnboardingNextButton(
                    onClick = {
                        // No-op
                    },
                    colors = ButtonDefaults.colors(
                        backgroundColor = OnboardingInitialScreenButtonBackgroundColor,
                        contentColor = OnboardingInitialScreenButtonTextColor,
                    ),
                )

                OnboardingPageIndicator(
                    page = 1,
                    pages = 5,
                    activePageIndicatorColor = OnboardingInitialScreenPageIndicatorActiveBackgroundColor,
                    inactivePageIndicatorColor = OnboardingInitialScreenPageIndicatorInactiveBackgroundColor,
                )
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

@Composable
private fun ColumnScope.OnboardingTitles() {
    Text(
        text = "Welcome to your SmartHome",
        modifier = Modifier
            .padding(bottom = 8.dp),
        textAlign = TextAlign.Center,
        lineHeight = 32.sp,
        fontSize = 22.sp,
        fontWeight = FontWeight.W700,
    )

    Text(
        text = "Control all your devices from one beautiful app",
        textAlign = TextAlign.Center,
        lineHeight = 20.sp,
        fontSize = 14.sp,
    )
}

@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun OnboardingScreenViewPreview() {
    OnboardingScreenView()
}
