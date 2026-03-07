package network.marsys.smarthome.shared.feature.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.entity_lamp_dining_table
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.entity_state_card_separator
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.entity_state_off
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.entity_state_on
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Switch
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Lightbulb
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import org.jetbrains.compose.resources.stringResource

@Composable
@Suppress("LongMethod")
fun OnboardingLightEntity(
    state: Boolean,
    modifier: Modifier = Modifier,
    onStateChange: (Boolean) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
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
        modifier = modifier
            .toggleable(
                value = state,
                indication = null,
                interactionSource = interactionSource,
                role = Role.Switch,
                onValueChange = onStateChange,
            ),
        colors = cardColors,
        contentPadding = PaddingValues(all = 20.dp),
        borderWidth = borderWidth,
    ) {
        Row(
            horizontalArrangement = Arrangement
                .spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LightEntityIcon(
                state = state,
            )

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement
                    .spacedBy(4.dp),
            ) {
                Text(
                    text = stringResource(Res.string.entity_lamp_dining_table),
                    lineHeight = 24.sp,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalColorScheme.current[ColorKeyToken.TextPrimary],
                )

                val separator = stringResource(Res.string.entity_state_card_separator)

                Text(
                    text = if (state) {
                        listOf(
                            stringResource(Res.string.entity_state_on),
                            "80%",
                        ).joinToString(separator = separator)
                    } else {
                        stringResource(Res.string.entity_state_off)
                    },
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                    color = LocalColorScheme.current[ColorKeyToken.TextSecondary],
                )
            }

            Switch(
                checked = state,
                onCheckedChange = onStateChange,
            )
        }
    }
}

@Composable
private fun LightEntityIcon(
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
        imageVector = Icons.Lightbulb,
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
private fun OnboardingEntitiesLightOffPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        OnboardingLightEntity(
            state = false,
        )
    }
}

@PreviewLocales
@Composable
private fun OnboardingEntitiesLightOnPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        OnboardingLightEntity(
            state = true,
        )
    }
}
