package network.marsys.smarthome.shared.feature.onboarding.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingNextButton
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingPageIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.logo
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_initial_logo_description
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_initial_subtitle
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_initial_title
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.ButtonDefaults
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val OnboardingBackgroundColor = Color(color = 0xFFF1BF42)

private val OnboardingInitialScreenButtonBackgroundColor = PaletteTokens.Slate.Slate800
private val OnboardingInitialScreenButtonTextColor = PaletteTokens.Base.White
private val OnboardingInitialScreenProgressIndicatorBackgroundColor = PaletteTokens.Amber.Amber600
    .copy(alpha = .3f)
private val OnboardingInitialScreenProgressIndicatorForegroundColor = PaletteTokens.Slate.Slate800

private val OnboardingInitialScreenPageIndicatorActiveBackgroundColor = PaletteTokens.Slate.Slate800
private val OnboardingInitialScreenPageIndicatorInactiveBackgroundColor = PaletteTokens.Amber.Amber600
    .copy(alpha = .3f)

@Composable
@Suppress("LongMethod")
fun InitialOnboardingScreenView(
    numberOfScreens: Int,
    indexOfScreen: Int,
    navigateToEntities: () -> Unit,
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
                    .padding(all = 40.dp)
                    .widthIn(max = 400.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OnboardingProgressIndicator(
                    numberOfScreens = numberOfScreens,
                    indexOfScreen = indexOfScreen,
                    colors = OnboardingProgressIndicatorDefaults.colors(
                        background = SolidColor(OnboardingInitialScreenProgressIndicatorBackgroundColor),
                        foreground = SolidColor(OnboardingInitialScreenProgressIndicatorForegroundColor),
                    ),
                    modifier = Modifier
                        .padding(bottom = 40.dp),
                )

                Text(
                    text = stringResource(Res.string.onboarding_initial_title),
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W700,
                )

                Text(
                    text = stringResource(Res.string.onboarding_initial_subtitle),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                )

                Spacer(
                    modifier = Modifier
                        .weight(1f),
                )

                OnboardingNextButton(
                    onClick = navigateToEntities,
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
                contentDescription = stringResource(Res.string.onboarding_initial_logo_description),
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
private fun InitialOnboardingScreenViewPreview() {
    InitialOnboardingScreenView(
        numberOfScreens = 4,
        indexOfScreen = 1,
        navigateToEntities = {},
    )
}
