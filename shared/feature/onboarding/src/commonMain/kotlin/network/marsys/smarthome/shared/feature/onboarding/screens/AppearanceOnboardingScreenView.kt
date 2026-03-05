package network.marsys.smarthome.shared.feature.onboarding.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.feature.onboarding.OnboardingScreens
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingBackButton
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingNextButton
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIcon
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenScaffold
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.ButtonDefaults
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.SunMoon
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens

private val BrandPrimaryToSecondaryGradient
    @Composable
    get() = LocalColorScheme.current[GradientKeyToken.BrandPrimaryToSecondary]

@Composable
fun AppearanceOnboardingScreenView(
    navigateToConfiguration: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val numberOfScreens: Int = OnboardingScreens.SCREEN_COUNT
    val indexOfScreen: Int = OnboardingScreens.indexOf(OnboardingScreens.Appearance)

    OnboardingScreenScaffold(
        modifier = modifier,
        header = {
            OnboardingProgressIndicator(
                numberOfScreens = numberOfScreens,
                indexOfScreen = indexOfScreen,
                colors = OnboardingProgressIndicatorDefaults.colors(
                    foreground = BrandPrimaryToSecondaryGradient,
                ),
                modifier = Modifier
                    .padding(bottom = 40.dp),
            )

            OnboardingScreenIcon(
                icon = Icons.SunMoon,
                modifier = Modifier
                    .padding(bottom = 16.dp),
            )
        },
        footer = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OnboardingBackButton(
                    onClick = navigateBack,
                )

                OnboardingNextButton(
                    onClick = navigateToConfiguration,
                    colors = ButtonDefaults.colors(
                        backgroundColor = BrandPrimaryToSecondaryGradient,
                        contentColor = PaletteTokens.Base.White,
                    ),
                )
            }

            OnboardingScreenIndicator(
                screen = indexOfScreen,
                screens = numberOfScreens,
                modifier = Modifier
                    .padding(top = 24.dp),
                colors = OnboardingScreenIndicatorDefaults.colors(
                    activeScreenIndicatorColor = SolidColor(PaletteTokens.Emerald.Emerald500),
                    currentScreenIndicatorColor = BrandPrimaryToSecondaryGradient,
                ),
            )
        },
    ) {
        // Not implemented yet
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun AppearanceOnboardingScreenLightModePreview() {
    SmartHomeTheme(
        darkMode = false,
    ) {
        AppearanceOnboardingScreenView(
            navigateToConfiguration = {},
            navigateBack = {},
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun AppearanceOnboardingScreenDarkModePreview() {
    SmartHomeTheme(
        darkMode = true,
    ) {
        AppearanceOnboardingScreenView(
            navigateToConfiguration = {},
            navigateBack = {},
        )
    }
}
