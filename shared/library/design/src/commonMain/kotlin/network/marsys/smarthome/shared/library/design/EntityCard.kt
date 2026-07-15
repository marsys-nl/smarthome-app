package network.marsys.smarthome.shared.library.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.library.design.component.Border
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.Switch
import network.marsys.smarthome.shared.library.design.component.SwitchColors
import network.marsys.smarthome.shared.library.design.component.SwitchDefaults
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Lightbulb
import network.marsys.smarthome.shared.library.design.icons.Monitor
import network.marsys.smarthome.shared.library.design.icons.Thermostat
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens

@Composable
fun EntityCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    active: Boolean,
    modifier: Modifier = Modifier,
    activeColors: ActiveEntityCardColors = EntityCardDefaults.activeCardColors(),
    topRight: @Composable (EntityCardScope.() -> Unit)? = null,
) {
    val cardColors = if (active) {
        CardDefaults.colors(
            backgroundColor = activeColors.background,
            borderColor = activeColors.border,
        )
    } else {
        CardDefaults.colors(
            borderColor = SmartHomeTheme.colors[ColorKeyToken.BorderPrimary]
                .copy(alpha = .5f),
        )
    }

    Card(
        modifier = modifier,
        colors = cardColors,
        contentPadding = EntityCardDefaults.contentPadding(),
        border = Border.Solid(1.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement
                    .SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                EntityCardIcon(
                    enabled = active,
                    icon = icon,
                )

                topRight?.invoke(EntityCardScope)
            }

            EntityCardTitle(
                title = title,
                subtitle = subtitle,
                active = active,
            )
        }
    }
}

@Composable
private fun EntityCardTitle(
    title: String,
    subtitle: String,
    active: Boolean,
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement
            .spacedBy(4.dp),
    ) {
        Text(
            text = title,
            lineHeight = 24.sp,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = EntityCardDefaults.titleColor(active).value,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = subtitle,
            lineHeight = 20.sp,
            fontSize = 14.sp,
            color = EntityCardDefaults.subtitleColor(active).value,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Immutable
data class ActiveEntityCardColors(
    val background: Brush,
    val border: Color,
)

object EntityCardDefaults {
    @Composable
    fun contentPadding() = PaddingValues(20.dp)

    @Composable
    fun activeCardColors(
        background: Brush = SmartHomeTheme.colors[GradientKeyToken.BrandPrimaryToSecondary],
        border: Color = SmartHomeTheme.colors[ColorKeyToken.BorderBrandPrimaryDimmed],
    ): ActiveEntityCardColors = ActiveEntityCardColors(
        background = background,
        border = border,
    )

    @Composable
    fun titleColor(active: Boolean): State<Color> =
        rememberUpdatedState(
            newValue = if (active) {
                PaletteTokens.Base.White
            } else {
                SmartHomeTheme.colors[ColorKeyToken.TextPrimary]
            },
        )

    @Composable
    fun subtitleColor(active: Boolean): State<Color> =
        rememberUpdatedState(
            newValue = if (active) {
                PaletteTokens.Base.White
                    .copy(alpha = .7f)
            } else {
                SmartHomeTheme.colors[ColorKeyToken.TextPrimary]
            },
        )

    @Composable
    fun iconBackgroundColor(active: Boolean): State<Color> =
        rememberUpdatedState(
            newValue = SmartHomeTheme.colors.get(
                token = if (active) {
                    ColorKeyToken.BackgroundDimmed
                } else {
                    ColorKeyToken.BackgroundTertiaryDisabled
                },
            ),
        )

    @Composable
    fun iconTintColor(active: Boolean): State<Color> =
        rememberUpdatedState(
            newValue = SmartHomeTheme.colors.get(
                token = if (active) {
                    ColorKeyToken.ForegroundPrimaryAlternative
                } else {
                    ColorKeyToken.ForegroundPrimary
                },
            ),
        )

    @Composable
    fun switchColors(): SwitchColors = SwitchDefaults.colors(
        checkedTrackColor = PaletteTokens.Base.White
            .copy(alpha = .3f),
        checkedThumbColor = PaletteTokens.Base.White,
    )
}

object EntityCardScope {
    @Composable
    fun Switch(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
    ) {
        EntityCardSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
private fun EntityCardIcon(
    enabled: Boolean,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Icon(
        icon = icon,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(EntityCardDefaults.iconBackgroundColor(enabled).value)
            .padding(12.dp),
        size = 24.dp,
        tint = EntityCardDefaults.iconTintColor(enabled).value,
    )
}

@Composable
private fun EntityCardSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Switch(
        checked = checked,
        modifier = modifier,
        onCheckedChange = onCheckedChange,
        colors = EntityCardDefaults.switchColors(),
    )
}

@Preview
@Composable
private fun LightOnEntityCardPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        EntityCard(
            title = "Kitchen light",
            subtitle = "55% brightness",
            icon = Icons.Lightbulb,
            active = true,
        ) {
            EntityCardScope.Switch(
                checked = true,
                onCheckedChange = {},
            )
        }
    }
}

@Preview
@Composable
private fun ThermostatOnEntityCardPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        EntityCard(
            title = "Downstairs",
            subtitle = "30°C • Idle",
            icon = Icons.Thermostat,
            active = true,
            activeColors = EntityCardDefaults.activeCardColors(
                background = GradientTokens.Rose.Rose400.ToRose600,
                border = PaletteTokens.Rose.Rose400
                    .copy(alpha = .4f),
            ),
        ) {
            EntityCardScope.Switch(
                checked = true,
                onCheckedChange = {},
            )
        }
    }
}

@Preview
@Composable
private fun SmartTvOnEntityCardPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        EntityCard(
            title = "Smart TV",
            subtitle = "On",
            icon = Icons.Monitor,
            active = true,
            activeColors = EntityCardDefaults.activeCardColors(
                background = GradientTokens.Blue.Blue400.ToBlue600,
                border = PaletteTokens.Blue.Blue400
                    .copy(alpha = .4f),
            ),
        )
    }
}

@Preview
@Composable
private fun LightOffEntityCardPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        EntityCard(
            title = "Kitchen light",
            subtitle = "55% brightness",
            icon = Icons.Lightbulb,
            active = false,
        ) {
            EntityCardScope.Switch(
                checked = false,
                onCheckedChange = {},
            )
        }
    }
}
