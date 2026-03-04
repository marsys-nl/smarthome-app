package network.marsys.smarthome.shared.feature.onboarding.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenScaffold
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.logo
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_initial_logo_description
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_initial_subtitle
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_initial_title
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
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

private val ScreenIndicatorActiveBackgroundColor = PaletteTokens.Slate.Slate800
private val ScreenIndicatorInactiveBackgroundColor = PaletteTokens.Amber.Amber600
    .copy(alpha = .3f)

@Composable
@Suppress("LongMethod")
fun InitialOnboardingScreenView(
    numberOfScreens: Int,
    indexOfScreen: Int,
    navigateToEntities: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingScreenScaffold(
        modifier = modifier,
        backgroundColor = OnboardingBackgroundColor,
        centeredSlot = {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = stringResource(Res.string.onboarding_initial_logo_description),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(108.dp),
            )
        },
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
                backgroundColor = SolidColor(
                    value = OnboardingInitialScreenButtonBackgroundColor,
                ),
                contentColor = OnboardingInitialScreenButtonTextColor,
            ),
        )

        OnboardingScreenIndicator(
            screen = 1,
            screens = 5,
            modifier = Modifier
                .padding(top = 24.dp),
            colors = OnboardingScreenIndicatorDefaults.colors(
                activeScreenIndicatorColor = SolidColor(ScreenIndicatorActiveBackgroundColor),
                inactiveScreenIndicatorColor = SolidColor(ScreenIndicatorInactiveBackgroundColor),
            ),
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun InitialOnboardingScreenLightModePreview() {
    SmartHomeTheme(
        darkMode = false,
    ) {
        InitialOnboardingScreenView(
            numberOfScreens = 4,
            indexOfScreen = 1,
            navigateToEntities = {},
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun InitialOnboardingScreenDarkModePreview() {
    SmartHomeTheme(
        darkMode = true,
    ) {
        InitialOnboardingScreenView(
            numberOfScreens = 4,
            indexOfScreen = 1,
            navigateToEntities = {},
        )
    }
}
