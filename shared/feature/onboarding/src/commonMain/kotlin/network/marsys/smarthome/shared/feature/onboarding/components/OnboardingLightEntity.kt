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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Switch
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Lightbulb
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.ColorSchemePreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken

@Composable
@Suppress("LongMethod")
fun OnboardingLightEntity(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    var light by retain { mutableStateOf(false) }

    val cardColors = if (light) {
        CardDefaults.colors(
            backgroundColor = LocalColorScheme.current[GradientKeyToken.DimmedPrimaryToSecondary],
            borderColor = LocalColorScheme.current[ColorKeyToken.ForegroundBrandPrimary],
        )
    } else {
        CardDefaults.colors()
    }

    val borderWidth = if (light) 1.dp else 0.dp

    Card(
        modifier = modifier
            .toggleable(
                value = light,
                indication = null,
                interactionSource = interactionSource,
                role = Role.Switch,
                onValueChange = { light = !light },
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
                isOn = light,
            )

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement
                    .spacedBy(4.dp),
            ) {
                Text(
                    text = "Living room light",
                    lineHeight = 24.sp,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalColorScheme.current[ColorKeyToken.TextPrimary],
                )

                Text(
                    text = if (light) {
                        "On • 80%"
                    } else {
                        "Off"
                    },
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                    color = LocalColorScheme.current[ColorKeyToken.TextSecondary],
                )
            }

            Switch(
                checked = light,
                onCheckedChange = {
                    light = !light
                },
            )
        }
    }
}

@Composable
private fun LightEntityIcon(
    isOn: Boolean,
    modifier: Modifier = Modifier,
) {
    val iconBackgroundColorKeyToken =
        if (isOn) {
            ColorKeyToken.BackgroundBrandPrimary
        } else {
            ColorKeyToken.BackgroundTertiary
        }

    val iconForegroundColorKeyToken =
        if (isOn) {
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

@Preview
@Composable
private fun OnboardingEntitiesScreenIconPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        OnboardingLightEntity()
    }
}
