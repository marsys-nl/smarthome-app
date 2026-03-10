package network.marsys.smarthome.shared.feature.onboarding.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.feature.onboarding.OnboardingScreens
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingBackButton
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIcon
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenScaffold
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_get_started
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_skip_setup
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_subtitle
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_configuration_title
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.Button
import network.marsys.smarthome.shared.library.design.component.ButtonDefaults
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Check
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Server
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.LocalTextStyle
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import org.jetbrains.compose.resources.stringResource

private val BrandPrimaryToSecondaryGradient
    @Composable
    get() = LocalColorScheme.current[GradientKeyToken.BrandPrimaryToSecondary]

@Composable
@Suppress("LongMethod")
fun ConfigurationOnboardingScreenView(
    finishOnboarding: () -> Unit,
    skipToDemo: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    val numberOfScreens: Int = OnboardingScreens.SCREEN_COUNT
    val indexOfScreen: Int = OnboardingScreens.indexOf(OnboardingScreens.Configuration)

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
                icon = Icons.Server,
                modifier = Modifier
                    .padding(bottom = 16.dp),
            )

            Text(
                text = stringResource(Res.string.onboarding_configuration_title),
                modifier = Modifier
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
                lineHeight = 32.sp,
                fontSize = 22.sp,
                fontWeight = FontWeight.W700,
            )

            Text(
                text = stringResource(Res.string.onboarding_configuration_subtitle),
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

                FinishOnboardingButton(
                    onClick = finishOnboarding,
                )
            }

            OnboardingSkipConfigurationButton(
                onClick = skipToDemo,
                modifier = Modifier,
            )

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

@Composable
fun FinishOnboardingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.colors(
            backgroundColor = BrandPrimaryToSecondaryGradient,
            contentColor = PaletteTokens.Base.White,
        ),
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement
                .spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val density = LocalDensity.current
            var textHeight by remember { mutableStateOf(0.dp) }

            Text(
                text = stringResource(Res.string.onboarding_configuration_get_started),
                modifier = Modifier
                    .onGloballyPositioned {
                        textHeight = with(density) {
                            it.size.height.toDp()
                        }
                    },
                fontWeight = FontWeight.Bold,
            )

            Image(
                imageVector = Icons.Check,
                contentDescription = null,
                modifier = Modifier
                    .height(textHeight),
                colorFilter = ColorFilter.tint(
                    color = LocalContentColor.current,
                ),
            )
        }
    }
}

@Composable
fun OnboardingSkipConfigurationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val skipConfigButtonStyle = if (!isPressed) {
        LocalTextStyle.current.copy(
            textDecoration = TextDecoration.Underline,
        )
    } else {
        LocalTextStyle.current
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .padding(top = 24.dp),
        colors = ButtonDefaults.colors(
            backgroundColor = SolidColor(Color.Transparent),
        ),
        indication = null,
        interactionSource = interactionSource,
    ) {
        Text(
            text = stringResource(Res.string.onboarding_configuration_skip_setup),
            style = skipConfigButtonStyle,
            lineHeight = 20.sp,
            fontSize = 14.sp,
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun ConfigurationOnboardingScreenPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ConfigurationOnboardingScreenView(
            finishOnboarding = {},
            skipToDemo = {},
            navigateBack = {},
        )
    }
}
