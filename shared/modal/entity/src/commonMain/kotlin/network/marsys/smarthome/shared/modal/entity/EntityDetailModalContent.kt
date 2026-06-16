package network.marsys.smarthome.shared.modal.entity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.library.design.SmartHomeModalPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.Switch
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Clock
import network.marsys.smarthome.shared.library.design.icons.Close
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.i18n.pluralStringResource
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.Res
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_capability_on_off
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_on
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_update_max
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_update_now
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_updated
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_updated_days
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_updated_hours
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_updated_minutes
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_updated_seconds
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

@Composable
fun EntityDetailModalContent(
    entity: EntityIdentifier,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    lastUpdate: Instant = Clock.System.now(),
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement
            .spacedBy(24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(identifier = entity),
                    lineHeight = 32.sp,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W700,
                )

                CloseModalButton(
                    onDismissRequest = onDismissRequest,
                )
            }

            Row(
                horizontalArrangement = Arrangement
                    .spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides SmartHomeTheme.colors[ColorKeyToken.TextDisabled],
                ) {
                    Icon(
                        icon = Icons.Clock,
                        size = 12.dp,
                    )

                    Text(
                        text = entityUpdatedLabel(
                            elapsed = Clock.System.now() - lastUpdate,
                        ),
                        lineHeight = 16.sp,
                        fontSize = 12.sp,
                    )
                }
            }
        }

        EntityDetailOnOffSection()
    }
}

@Composable
private fun EntityDetailOnOffSection(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                shape = CardDefaults.shape(),
                color = SmartHomeTheme.colors[ColorKeyToken.BackgroundTertiaryDisabled],
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.entity_capability_on_off),
            lineHeight = 24.sp,
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
        )

        Row(
            horizontalArrangement = Arrangement
                .spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.entity_state_on),
                lineHeight = 20.sp,
                fontSize = 14.sp,
            )

            Switch(
                checked = true,
                onCheckedChange = {},
            )
        }
    }
}

@Composable
private fun CloseModalButton(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onDismissRequest,
            ),
        colors = CardDefaults.colors(
            backgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundSecondary]),
        ),
        contentPadding = CardDefaults.contentPadding(
            horizontal = 8.dp,
            vertical = 8.dp,
        ),
    ) {
        Icon(
            icon = Icons.Close,
            size = 20.dp,
        )
    }
}

private const val MINIMUM_SECONDS = 10
private const val MAXIMUM_DAYS = 99

@Composable
private fun entityUpdatedLabel(elapsed: Duration): String = when {
    elapsed.inWholeSeconds < MINIMUM_SECONDS -> {
        stringResource(Res.string.entity_state_update_now)
    }

    elapsed.inWholeDays > MAXIMUM_DAYS -> {
        stringResource(Res.string.entity_state_update_max)
    }

    else -> {
        val duration = when {
            elapsed.inWholeSeconds < 60 -> pluralStringResource(
                resource = Res.plurals.entity_state_updated_seconds,
                quantity = elapsed.inWholeSeconds.toInt(),
            )

            elapsed.inWholeMinutes < 60 -> pluralStringResource(
                resource = Res.plurals.entity_state_updated_minutes,
                quantity = elapsed.inWholeMinutes.toInt(),
            )

            elapsed.inWholeHours < 24 -> pluralStringResource(
                resource = Res.plurals.entity_state_updated_hours,
                quantity = elapsed.inWholeHours.toInt(),
            )

            else -> pluralStringResource(
                resource = Res.plurals.entity_state_updated_days,
                quantity = elapsed.inWholeDays.toInt(),
            )
        }

        stringResource(
            resource = Res.string.entity_state_updated,
            formatArgs = arrayOf(duration),
        )
    }
}

@PreviewScreenSizes
@Composable
private fun AppAppearanceModalContentPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeModalPreview(
        theme = theme,
    ) {
        EntityDetailModalContent(
            entity = EntityIdentifier("light.kitchen-light"),
            onDismissRequest = {},
        )
    }
}
