package network.marsys.smarthome.shared.modal.entity

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.capability.Brightness
import network.marsys.smarthome.shared.domain.entity.capability.ChildLock
import network.marsys.smarthome.shared.domain.entity.capability.MeasureTemperature
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import network.marsys.smarthome.shared.domain.entity.capability.ScheduledMode
import network.marsys.smarthome.shared.domain.entity.capability.WindowDetection
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.Light
import network.marsys.smarthome.shared.domain.entity.entity.Thermostat
import network.marsys.smarthome.shared.library.core.coroutines.produceStateWithLifecycle
import network.marsys.smarthome.shared.library.core.helper.ifPresent
import network.marsys.smarthome.shared.library.design.SmartHomeModalPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Clock
import network.marsys.smarthome.shared.library.design.icons.Close
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.LoaderCircle
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.i18n.pluralStringResource
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.Res
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_update_max
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_update_now
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_updated
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_updated_days
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_updated_hours
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_updated_minutes
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_updated_seconds
import network.marsys.smarthome.shared.modal.entity.section.BrightnessSection
import network.marsys.smarthome.shared.modal.entity.section.OnOffSection
import network.marsys.smarthome.shared.modal.entity.section.TemperatureControlSection
import network.marsys.smarthome.shared.modal.entity.section.ThermostatControlSection
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@Composable
fun EntityDetailModal(
    entity: EntityIdentifier,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<EntityDetailModalViewModel>(
        key = "entity-detail-modal-$entity",
    ) {
        parametersOf(entity)
    }

    val state = viewModel.produceStateWithLifecycle()

    EntityDetailModalContent(
        title = stringResource(identifier = entity),
        state = state,
        onAction = viewModel.accept,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    )
}

@Composable
private fun EntityDetailModalContent(
    title: String,
    state: EntityDetailModalState,
    onAction: (EntityDetailModalAction) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
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
                    text = title,
                    lineHeight = 32.sp,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W700,
                )

                CloseModalButton(
                    onDismissRequest = onDismissRequest,
                )
            }

            if (!state.isLoading) {
                EntityDetailLastUpdated()
            }
        }

        when {
            state.isLoading ->
                EntityDetailLoadingModalContent()

            state.entity != null ->
                EntityDetailLoadedModalContent(
                    entity = state.entity!!,
                    onAction = onAction,
                )
        }
    }
}

@Composable
private fun EntityDetailLoadingModalContent() {
    val transition = rememberInfiniteTransition("loading-indicator")

    val angle = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = LinearEasing,
            ),
        ),
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            icon = Icons.LoaderCircle,
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = angle.value
                },
            size = 32.dp,
        )
    }
}

@Composable
private fun EntityDetailLoadedModalContent(
    entity: Entity<*>,
    onAction: (EntityDetailModalAction) -> Unit,
) {
    entity.ifPresent<OnOff> {
        OnOffSection(
            entity = entity.identifier,
            onOff = it,
            onAction = onAction,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }

    entity.ifPresent<Brightness> {
        BrightnessSection(
            entity = entity.identifier,
            brightness = it,
            onAction = onAction,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }

    entity.ifPresent<ChildLock, WindowDetection, ScheduledMode> { childLock, windowDetection, scheduledMode ->
        ThermostatControlSection(
            entity = entity.identifier,
            childLock = childLock,
            windowDetection = windowDetection,
            scheduledMode = scheduledMode,
            onAction = onAction,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }

    entity.ifPresent<MeasureTemperature> {
        TemperatureControlSection(
            measureTemperature = it,
        )
    }
}

@Composable
private fun EntityDetailLastUpdated(
    lastUpdate: Instant = Clock.System.now()
        .minus((0..3600).random().seconds),
) {
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
private fun LoadingEntityDetailModalPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeModalPreview(
        theme = theme,
    ) {
        EntityDetailModalContent(
            title = "Kitchen spots",
            state = EntityDetailModalPreviewData.loading,
            onAction = {},
            onDismissRequest = {},
        )
    }
}

@PreviewScreenSizes
@Composable
private fun LoadedLightEntityDetailModalPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeModalPreview(
        theme = theme,
    ) {
        val state = EntityDetailModalPreviewData.loaded[Light::class] ?: error("Missing preview data")

        EntityDetailModalContent(
            title = "Kitchen spots",
            state = state,
            onAction = {},
            onDismissRequest = {},
        )
    }
}

@PreviewScreenSizes
@Composable
private fun LoadedThermostatEntityDetailModalPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeModalPreview(
        theme = theme,
    ) {
        val state = EntityDetailModalPreviewData.loaded[Thermostat::class] ?: error("Missing preview data")

        EntityDetailModalContent(
            title = "Living room",
            state = state,
            onAction = {},
            onDismissRequest = {},
        )
    }
}
