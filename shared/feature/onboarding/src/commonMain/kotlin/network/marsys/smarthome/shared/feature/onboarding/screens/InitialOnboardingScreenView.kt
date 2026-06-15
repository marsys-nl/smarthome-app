package network.marsys.smarthome.shared.feature.onboarding.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.feature.onboarding.OnboardingScreens
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingNextButton
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenScaffold
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_initial_logo_description
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_initial_subtitle
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_initial_title
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.ButtonDefaults
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.library.resources.SmartHomeRes
import network.marsys.smarthome.shared.library.resources.logo
import org.jetbrains.compose.resources.painterResource

private val OnboardingBackgroundColor = Color(color = 0xFFF1BF42)
private val OnboardingContentColor = PaletteTokens.Slate.Slate800

private val OnboardingButtonBackgroundColor = PaletteTokens.Slate.Slate800
private val OnboardingButtonTextColor = PaletteTokens.Base.White
private val OnboardingProgressIndicatorBackgroundColor = PaletteTokens.Amber.Amber600
    .copy(alpha = .3f)
private val OnboardingProgressIndicatorForegroundColor = PaletteTokens.Slate.Slate800

private val ScreenIndicatorActiveBackgroundColor = PaletteTokens.Slate.Slate800
private val ScreenIndicatorInactiveBackgroundColor = PaletteTokens.Amber.Amber600
    .copy(alpha = .3f)

@Composable
@Suppress("LongMethod")
fun InitialOnboardingScreenView(
    navigateToEntities: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val numberOfScreens: Int = OnboardingScreens.SCREEN_COUNT
    val indexOfScreen: Int = OnboardingScreens.indexOf(OnboardingScreens.Initial)

    OnboardingScreenScaffold(
        modifier = modifier,
        backgroundColor = OnboardingBackgroundColor,
        centeredSlot = {
            Image(
                painter = painterResource(SmartHomeRes.drawable.logo),
                contentDescription = stringResource(Res.string.onboarding_initial_logo_description),
                modifier = Modifier
                    .align(Alignment.Center)
                    .sizeIn(maxHeight = maxHeight / 6)
                    .aspectRatio(1f),
            )
        },
        header = {
            OnboardingProgressIndicator(
                numberOfScreens = numberOfScreens,
                indexOfScreen = indexOfScreen,
                colors = OnboardingProgressIndicatorDefaults.colors(
                    background = SolidColor(OnboardingProgressIndicatorBackgroundColor),
                    foreground = SolidColor(OnboardingProgressIndicatorForegroundColor),
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
                color = OnboardingContentColor,
            )

            Text(
                text = stringResource(Res.string.onboarding_initial_subtitle),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                fontSize = 14.sp,
                color = OnboardingContentColor,
            )
        },
        footer = {
            OnboardingNextButton(
                onClick = navigateToEntities,
                colors = ButtonDefaults.colors(
                    backgroundColor = SolidColor(
                        value = OnboardingButtonBackgroundColor,
                    ),
                    contentColor = OnboardingButtonTextColor,
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
        },
    )
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun InitialOnboardingScreenPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        InitialOnboardingScreenView(
            navigateToEntities = {},
        )
    }
}
