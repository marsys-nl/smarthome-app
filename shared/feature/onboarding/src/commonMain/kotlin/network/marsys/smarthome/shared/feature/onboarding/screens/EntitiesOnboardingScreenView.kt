package network.marsys.smarthome.shared.feature.onboarding.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingBackButton
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingLightEntity
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingNextButton
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIcon
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenScaffold
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingThermostatEntity
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_entities_interaction
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_entities_subtitle
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_entities_title
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
import kotlin.random.Random

private val BrandPrimaryToSecondaryGradient
    @Composable
    get() = LocalColorScheme.current[GradientKeyToken.BrandPrimaryToSecondary]

private const val ENTITY_WIDTH_FRACTION = .95f
private const val THERMOSTAT_DELAY_MULTIPLIER = 500L

@Composable
@Suppress("LongMethod")
internal fun EntitiesOnboardingScreenView(
    numberOfScreens: Int,
    indexOfScreen: Int,
    navigateToScenes: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(ENTITY_WIDTH_FRACTION)
                .padding(vertical = 24.dp),
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

            var thermostat by retain { mutableStateOf(Random.nextBoolean()) }

            LaunchedEffect(key1 = Unit) {
                while (true) {
                    val delayInMillis = Random.nextInt(from = 10, until = 30) * THERMOSTAT_DELAY_MULTIPLIER
                    delay(delayInMillis)
                    thermostat = !thermostat
                }
            }

            OnboardingThermostatEntity(
                state = thermostat,
                modifier = Modifier
                    .padding(bottom = 12.dp),
            )

            Text(
                text = stringResource(Res.string.onboarding_entities_interaction),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                fontSize = 14.sp,
            )
        }
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun EntitiesOnboardingScreenLightModePreview() {
    SmartHomeTheme(
        darkMode = false,
    ) {
        EntitiesOnboardingScreenView(
            numberOfScreens = 4,
            indexOfScreen = 2,
            navigateToScenes = {},
            navigateBack = {},
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun EntitiesOnboardingScreenDarkModePreview() {
    SmartHomeTheme(
        darkMode = true,
    ) {
        EntitiesOnboardingScreenView(
            numberOfScreens = 4,
            indexOfScreen = 2,
            navigateToScenes = {},
            navigateBack = {},
        )
    }
}
