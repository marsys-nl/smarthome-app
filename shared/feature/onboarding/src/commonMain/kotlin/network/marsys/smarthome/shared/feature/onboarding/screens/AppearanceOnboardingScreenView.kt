package network.marsys.smarthome.shared.feature.onboarding.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.feature.onboarding.OnboardingScreens
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingBackButton
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingNextButton
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingProgressIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIcon
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicator
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenIndicatorDefaults
import network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenScaffold
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_appearance_dark
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_appearance_dark_subtitle
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_appearance_light
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_appearance_light_subtitle
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_appearance_subtitle
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_appearance_system
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_appearance_system_subtitle
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_appearance_title
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.ButtonDefaults
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Check
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Monitor
import network.marsys.smarthome.shared.library.design.icons.Moon
import network.marsys.smarthome.shared.library.design.icons.Sun
import network.marsys.smarthome.shared.library.design.icons.SunMoon
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import org.jetbrains.compose.resources.stringResource

private val BrandPrimaryToSecondaryGradient
    @Composable
    get() = LocalColorScheme.current[GradientKeyToken.BrandPrimaryToSecondary]

@Composable
@Suppress("LongMethod")
fun AppearanceOnboardingScreenView(
    onThemeSelected: (ThemeSelection) -> Unit,
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

            Text(
                text = stringResource(Res.string.onboarding_appearance_title),
                modifier = Modifier
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
                lineHeight = 32.sp,
                fontSize = 22.sp,
                fontWeight = FontWeight.W700,
            )

            Text(
                text = stringResource(Res.string.onboarding_appearance_subtitle),
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
        SelectableThemeOption(
            title = stringResource(Res.string.onboarding_appearance_system),
            subtitle = stringResource(Res.string.onboarding_appearance_system_subtitle),
            icon = Icons.Monitor,
            state = SmartHomeTheme.current == ThemeSelection.SystemDefault,
            onThemeSelected = {
                onThemeSelected(ThemeSelection.SystemDefault)
            },
        )

        SelectableThemeOption(
            title = stringResource(Res.string.onboarding_appearance_light),
            subtitle = stringResource(Res.string.onboarding_appearance_light_subtitle),
            icon = Icons.Sun,
            state = SmartHomeTheme.current == ThemeSelection.LightMode,
            onThemeSelected = {
                onThemeSelected(ThemeSelection.LightMode)
            },
        )

        SelectableThemeOption(
            title = stringResource(Res.string.onboarding_appearance_dark),
            subtitle = stringResource(Res.string.onboarding_appearance_dark_subtitle),
            icon = Icons.Moon,
            state = SmartHomeTheme.current == ThemeSelection.DarkMode,
            onThemeSelected = {
                onThemeSelected(ThemeSelection.DarkMode)
            },
        )
    }
}

@Composable
private fun SelectableThemeOption(
    title: String,
    subtitle: String,
    icon: ImageVector,
    state: Boolean,
    onThemeSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardColors = if (state) {
        CardDefaults.colors(
            backgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundSecondarySelected]),
            borderColor = LocalColorScheme.current[ColorKeyToken.BorderBrandPrimaryDimmed],
        )
    } else {
        CardDefaults.colors()
    }

    val borderWidth = if (state) 1.dp else 0.dp

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = !state,
                onClickLabel = "Select $title",
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onThemeSelected,
            ),
        colors = cardColors,
        borderWidth = borderWidth,
    ) {
        Row(
            horizontalArrangement = Arrangement
                .spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SelectableThemeIcon(
                icon = icon,
                state = state,
            )

            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    lineHeight = 24.sp,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )

                Text(
                    text = subtitle,
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                )
            }

            if (state) {
                SelectedThemeIcon()
            }
        }
    }
}

@Composable
private fun SelectableThemeIcon(
    icon: ImageVector,
    state: Boolean,
    modifier: Modifier = Modifier,
) {
    val iconBackgroundColorKeyToken =
        if (state) {
            ColorKeyToken.BackgroundBrandPrimary
        } else {
            ColorKeyToken.BackgroundTertiary
        }

    val iconForegroundColorKeyToken =
        if (state) {
            ColorKeyToken.ForegroundPrimaryAlternative
        } else {
            ColorKeyToken.ForegroundPrimary
        }

    Image(
        imageVector = icon,
        contentDescription = null,
        modifier = modifier
            .background(
                shape = RoundedCornerShape(16.dp),
                color = LocalColorScheme.current[iconBackgroundColorKeyToken],
            )
            .padding(12.dp),
        colorFilter = ColorFilter.tint(
            color = LocalColorScheme.current[iconForegroundColorKeyToken],
        ),
    )
}

@Composable
private fun SelectedThemeIcon() {
    Image(
        imageVector = Icons.Check,
        contentDescription = null,
        modifier = Modifier
            .background(
                shape = CircleShape,
                color = PaletteTokens.Emerald.Emerald500,
            )
            .size(size = 18.dp)
            .padding(all = 4.dp),
        colorFilter = ColorFilter.tint(
            color = PaletteTokens.Base.White,
        ),
    )
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun AppearanceOnboardingScreenPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        AppearanceOnboardingScreenView(
            onThemeSelected = {},
            navigateToConfiguration = {},
            navigateBack = {},
        )
    }
}
