package network.marsys.smarthome.shared.feature.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.entity_state_card_separator
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.entity_state_heating
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.entity_state_idle
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.entity_thermostat_nursery
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Thermostat
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingThermostatEntity(
    state: Boolean,
    modifier: Modifier = Modifier,
) {
    val cardColors = if (state) {
        CardDefaults.colors(
            backgroundColor = LocalColorScheme.current[GradientKeyToken.DimmedPrimaryToSecondary],
            borderColor = LocalColorScheme.current[ColorKeyToken.BorderBrandPrimaryDimmed],
        )
    } else {
        CardDefaults.colors()
    }

    val borderWidth = if (state) 1.dp else 0.dp

    Card(
        modifier = modifier,
        colors = cardColors,
        contentPadding = PaddingValues(all = 20.dp),
        borderWidth = borderWidth,
    ) {
        Row(
            horizontalArrangement = Arrangement
                .spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ThermostatEntityIcon(
                state = state,
            )

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement
                    .spacedBy(4.dp),
            ) {
                Text(
                    text = stringResource(Res.string.entity_thermostat_nursery),
                    lineHeight = 24.sp,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalColorScheme.current[ColorKeyToken.TextPrimary],
                )

                val separator = stringResource(Res.string.entity_state_card_separator)
                val stateStringResource = if (state) {
                    Res.string.entity_state_heating
                } else {
                    Res.string.entity_state_idle
                }

                Text(
                    text = listOf("22°C", stringResource(stateStringResource))
                        .joinToString(separator = separator),
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                    color = LocalColorScheme.current[ColorKeyToken.TextSecondary],
                )
            }
        }
    }
}

@Composable
private fun ThermostatEntityIcon(
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
        imageVector = Icons.Thermostat,
        modifier = modifier
            .background(
                shape = RoundedCornerShape(16.dp),
                color = LocalColorScheme.current[iconBackgroundColorKeyToken],
            )
            .padding(12.dp),
        contentDescription = null,
        colorFilter = ColorFilter.tint(
            color = LocalColorScheme.current[iconForegroundColorKeyToken],
        ),
    )
}

@PreviewLocales
@Composable
private fun OnboardingEntitiesThermostatIdlePreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        OnboardingThermostatEntity(
            state = false,
        )
    }
}

@PreviewLocales
@Composable
private fun OnboardingEntitiesThermostatHeatingPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        OnboardingThermostatEntity(
            state = true,
        )
    }
}
