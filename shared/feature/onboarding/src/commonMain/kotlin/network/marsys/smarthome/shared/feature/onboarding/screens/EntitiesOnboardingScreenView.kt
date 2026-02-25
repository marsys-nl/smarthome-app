package network.marsys.smarthome.shared.feature.onboarding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingBackButton
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingNextButton
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingPageIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicatorDefaults
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens

@Composable
internal fun EntitiesOnboardingScreenView(
    numberOfScreens: Int,
    indexOfScreen: Int,
    navigateToScenes: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SmartHomeTheme {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(LocalColorScheme.current[ColorKeyToken.BackgroundPrimary]),
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
                        foreground = Brush.linearGradient(
                            colors = listOf(
                                PaletteTokens.Amber.Amber400,
                                PaletteTokens.Emerald.Emerald400,
                            ),
                        ),
                    ),
                    modifier = Modifier
                        .padding(bottom = 40.dp),
                )

                // Content goes here.

                Spacer(
                    modifier = Modifier
                        .weight(1f),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OnboardingBackButton(
                        onClick = navigateBack,
                    )

                    OnboardingNextButton(
                        onClick = navigateToScenes,
                    )
                }

                OnboardingPageIndicator(
                    page = indexOfScreen,
                    pages = numberOfScreens,
                )
            }
        }
    }
}

@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun EntitiesOnboardingScreenViewPreview() {
    EntitiesOnboardingScreenView(
        numberOfScreens = 4,
        indexOfScreen = 2,
        navigateToScenes = {},
        navigateBack = {},
    )
}
