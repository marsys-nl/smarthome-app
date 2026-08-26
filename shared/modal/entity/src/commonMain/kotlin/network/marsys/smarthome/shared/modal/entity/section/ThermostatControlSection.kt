package network.marsys.smarthome.shared.modal.entity.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.capability.ChildLock
import network.marsys.smarthome.shared.domain.entity.capability.ScheduledMode
import network.marsys.smarthome.shared.domain.entity.capability.WindowDetection
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.Switch
import network.marsys.smarthome.shared.library.design.component.SwitchDefaults
import network.marsys.smarthome.shared.library.design.component.SwitchSize
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Calendar
import network.marsys.smarthome.shared.library.design.icons.DoorOpen
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Lock
import network.marsys.smarthome.shared.library.design.icons.LockOpen
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.modal.entity.EntityDetailModalAction

@Composable
fun ThermostatControlSection(
    entity: EntityIdentifier,
    childLock: ChildLock?,
    windowDetection: WindowDetection?,
    scheduledMode: ScheduledMode?,
    onAction: (EntityDetailModalAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (childLock == null && windowDetection == null && scheduledMode == null) {
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                shape = CardDefaults.shape(),
                color = SmartHomeTheme.colors[ColorKeyToken.BackgroundTertiaryDisabled],
            )
            .padding(16.dp),
        verticalArrangement = Arrangement
            .spacedBy(12.dp),
    ) {
        childLock?.let {
            ChildLockSection(
                entity = entity,
                childLock = it,
                onAction = onAction,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        windowDetection?.let {
            WindowDetectionSection(
                entity = entity,
                windowDetection = it,
                onAction = onAction,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        scheduledMode?.let {
            ScheduledModeSection(
                entity = entity,
                scheduledMode = it,
                onAction = onAction,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ChildLockSection(
    entity: EntityIdentifier,
    childLock: ChildLock,
    onAction: (EntityDetailModalAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberUpdatedState(childLock.current)
    val icon = remember(state.value) {
        derivedStateOf {
            if (state.value) {
                Icons.Lock
            } else {
                Icons.LockOpen
            }
        }
    }

    ToggleableRow(
        icon = icon.value,
        title = "Child lock",
        state = state.value,
        onCheckedChange = {
            onAction.invoke(
                EntityDetailModalAction.ToggleChildLock(
                    entity = entity,
                    state = it,
                ),
            )
        },
        modifier = modifier,
    )
}

@Composable
private fun WindowDetectionSection(
    entity: EntityIdentifier,
    windowDetection: WindowDetection,
    onAction: (EntityDetailModalAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberUpdatedState(windowDetection.current)

    ToggleableRow(
        icon = Icons.DoorOpen,
        title = "Window detection",
        state = state.value,
        onCheckedChange = {
            onAction.invoke(
                EntityDetailModalAction.ToggleWindowDetection(
                    entity = entity,
                    state = it,
                ),
            )
        },
        modifier = modifier,
        onActiveColor = PaletteTokens.Blue.Blue500,
    )
}

@Composable
private fun ScheduledModeSection(
    entity: EntityIdentifier,
    scheduledMode: ScheduledMode,
    onAction: (EntityDetailModalAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberUpdatedState(scheduledMode.current)

    ToggleableRow(
        icon = Icons.Calendar,
        title = "Scheduled mode",
        state = state.value,
        onCheckedChange = {
            onAction.invoke(
                EntityDetailModalAction.ToggleScheduledMode(
                    entity = entity,
                    state = it,
                ),
            )
        },
        modifier = modifier,
        onActiveColor = PaletteTokens.Emerald.Emerald500,
    )
}

@Composable
private fun ToggleableRow(
    icon: ImageVector,
    title: String,
    state: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onActiveColor: Color = SmartHomeTheme.colors[ColorKeyToken.ForegroundBrandPrimary],
    onInactiveColor: Color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
) {
    val iconColor = remember(state) {
        if (state) {
            onActiveColor
        } else {
            onInactiveColor
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement
                .spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon = icon,
                size = 20.dp,
                tint = iconColor,
            )

            Text(
                text = title,
                lineHeight = 20.sp,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
            )
        }

        Switch(
            checked = state,
            colors = SwitchDefaults.colors(
                checkedTrackColor = onActiveColor,
                uncheckedTrackColor = PaletteTokens.Slate.Slate950
                    .copy(alpha = .2f),
            ),
            onCheckedChange = onCheckedChange,
            size = SwitchSize.Small,
        )
    }
}

@Preview
@Composable
private fun ThermostatControlSectionPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        ThermostatControlSection(
            entity = ThermostatControlSectionPreviewData.identifier,
            childLock = ThermostatControlSectionPreviewData.childLock,
            windowDetection = ThermostatControlSectionPreviewData.windowDetection,
            scheduledMode = ThermostatControlSectionPreviewData.scheduledMode,
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun ChildLockOnlyPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        ThermostatControlSection(
            entity = ThermostatControlSectionPreviewData.identifier,
            childLock = ThermostatControlSectionPreviewData.childLock,
            windowDetection = null,
            scheduledMode = null,
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun WindowDetectionOnlyPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        ThermostatControlSection(
            entity = ThermostatControlSectionPreviewData.identifier,
            childLock = null,
            windowDetection = ThermostatControlSectionPreviewData.windowDetection,
            scheduledMode = null,
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun ScheduledModeOnlyPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        ThermostatControlSection(
            entity = ThermostatControlSectionPreviewData.identifier,
            childLock = null,
            windowDetection = null,
            scheduledMode = ThermostatControlSectionPreviewData.scheduledMode,
            onAction = {},
        )
    }
}

private object ThermostatControlSectionPreviewData {
    val identifier = EntityIdentifier("thermostat.nursery")

    val childLock = ChildLock(current = true)
    val windowDetection = WindowDetection(current = true)
    val scheduledMode = ScheduledMode(current = false)
}
