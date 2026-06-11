package network.marsys.smarthome.shared.feature.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.Res
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.dashboard_greeting_afternoon
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.dashboard_greeting_appendix
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.dashboard_greeting_evening
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.dashboard_greeting_morning
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.dashboard_greeting_night
import network.marsys.smarthome.shared.feature.dashboard.DashboardScreenAction
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Bell
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.SunMoon
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

private const val DASHBOARD_BUTTON_ALPHA_ENABLED = 1f
private const val DASHBOARD_BUTTON_ALPHA_DISABLED = 0.5f

@Composable
internal fun DashboardHeader(
    instant: Instant,
    name: String,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DashboardHeaderGreeting(
            instant = instant,
            name = name,
        )

        DashboardHeaderQuickActions(
            onAction = onAction,
        )
    }
}

@Composable
private fun DashboardHeaderGreeting(
    instant: Instant,
    name: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = determinePersonalizedGreeting(
                instant = instant,
            ),
            lineHeight = 20.sp,
            fontSize = 14.sp,
            color = LocalColorScheme.current[ColorKeyToken.TextSecondary],
        )

        Text(
            text = name,
            lineHeight = 32.sp,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = LocalColorScheme.current[ColorKeyToken.TextPrimary],
        )
    }
}

@Composable
private fun determinePersonalizedGreeting(
    instant: Instant,
): String {
    val hour = instant
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .hour

    val greeting = stringResource(
        resource = when {
            hour !in 6..23 -> Res.string.dashboard_greeting_night
            hour < 12 -> Res.string.dashboard_greeting_morning
            hour < 18 -> Res.string.dashboard_greeting_afternoon
            else -> Res.string.dashboard_greeting_evening
        },
    )

    return stringResource(
        resource = Res.string.dashboard_greeting_appendix,
        formatArgs = arrayOf(greeting),
    )
}

@Composable
private fun DashboardHeaderQuickActions(
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement
            .spacedBy(
                space = 8.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DashboardHeaderQuickActionButton(
            onClick = {
                onAction.invoke(DashboardScreenAction.ChangeAppAppearance)
            },
            icon = Icons.SunMoon,
        )

        DashboardHeaderQuickActionButton(
            onClick = {},
            enabled = false,
            icon = Icons.Bell,
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color.Green,
                        shape = CircleShape,
                    )
                    .size(8.dp)
                    .aspectRatio(1f)
                    .align(Alignment.TopEnd),
            )
        }
    }
}

@Composable
private fun DashboardHeaderQuickActionButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
    badge: (@Composable BoxScope.() -> Unit)? = null,
) {
    val dashboardButtonAlpha = if (enabled) {
        DASHBOARD_BUTTON_ALPHA_ENABLED
    } else {
        DASHBOARD_BUTTON_ALPHA_DISABLED
    }

    Card(
        modifier = modifier
            .size(40.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .alpha(dashboardButtonAlpha),
        colors = CardDefaults.colors(
            contentColor = contentColor,
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            icon = icon,
            size = 16.dp,
        )

        badge
            ?.takeIf { enabled }
            ?.invoke(this)
    }
}

@Preview
@Composable
private fun DashboardScreenViewMorningPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        DashboardHeader(
            name = "John",
            instant = Instant.parse("2026-05-01T08:00:00Z"),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun DashboardScreenViewAfternoonPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        DashboardHeader(
            name = "John",
            instant = Instant.parse("2026-05-01T15:00:00Z"),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun DashboardScreenViewEveningPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        DashboardHeader(
            name = "John",
            instant = Instant.parse("2026-05-01T19:00:00Z"),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun DashboardScreenViewNightPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        DashboardHeader(
            name = "John",
            instant = Instant.parse("2026-05-01T23:00:00Z"),
            onAction = {},
        )
    }
}
