package network.marsys.smarthome.shared.feature.onboarding.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingBackButton
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingLightEntity
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingNextButton
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIcon
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenScaffold
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_entities_subtitle
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_entities_title
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_entities_try_interaction
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.ButtonDefaults
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.HousePlug
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import org.jetbrains.compose.resources.stringResource

private val BrandPrimaryToSecondaryGradient
    @Composable
    get() = LocalColorScheme.current[GradientKeyToken.BrandPrimaryToSecondary]

private const val ENTITY_WIDTH_FRACTION = .95f

@Composable
@Suppress("LongMethod")
internal fun EntitiesOnboardingScreenView(
    numberOfScreens: Int,
    indexOfScreen: Int,
    navigateToScenes: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SmartHomeTheme {
        OnboardingScreenScaffold(
            modifier = modifier,
        ) {
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
                icon = Icons.HousePlug,
                modifier = Modifier
                    .padding(bottom = 16.dp),
            )

            Text(
                text = stringResource(Res.string.onboarding_entities_title),
                modifier = Modifier
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
                lineHeight = 32.sp,
                fontSize = 22.sp,
                fontWeight = FontWeight.W700,
            )

            Text(
                text = stringResource(Res.string.onboarding_entities_subtitle),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                fontSize = 14.sp,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(ENTITY_WIDTH_FRACTION)
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                var light by retain { mutableStateOf(false) }

                OnboardingLightEntity(
                    state = light,
                    modifier = Modifier
                        .padding(bottom = 12.dp),
                    onStateChange = { light = it },
                )

                Text(
                    text = stringResource(Res.string.onboarding_entities_try_interaction),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OnboardingBackButton(
                    onClick = navigateBack,
                )

                OnboardingNextButton(
                    onClick = navigateToScenes,
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
        }
    }
}

@PreviewLocales
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
