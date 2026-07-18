package network.marsys.smarthome.shared.modal.entity.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.then
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.percent
import network.marsys.smarthome.shared.domain.entity.capability.Movement
import network.marsys.smarthome.shared.domain.entity.capability.Opened
import network.marsys.smarthome.shared.domain.entity.capability.Position
import network.marsys.smarthome.shared.domain.entity.entity.Cover
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Button
import network.marsys.smarthome.shared.library.design.component.ButtonDefaults
import network.marsys.smarthome.shared.library.design.component.ButtonStyle
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.ChevronDown
import network.marsys.smarthome.shared.library.design.icons.ChevronLeftRight
import network.marsys.smarthome.shared.library.design.icons.ChevronRightLeft
import network.marsys.smarthome.shared.library.design.icons.ChevronUp
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Square
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.components.ButtonTokens
import network.marsys.smarthome.shared.library.i18n.localized
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.modal.entity.EntityDetailModalAction
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.Res
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_capability_opened_current_state
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_capability_position
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_capability_position_close
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_capability_position_open
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_capability_position_stop
import org.jetbrains.compose.resources.StringResource

@Composable
internal fun CoverControlSection(
    entity: EntityIdentifier,
    orientation: Cover.Orientation,
    opened: Opened,
    position: Position?,
    movement: Movement?,
    onAction: (EntityDetailModalAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement
            .spacedBy(24.dp),
    ) {
        position?.let {
            CoverPositionSection(
                position = position,
            )
        } ?: run {
            CoverStateSection(
                opened = opened,
            )
        }

        movement?.let {
            CoverMovementControlSection(
                entity = entity,
                orientation = orientation,
                position = position,
                movement = movement,
                opened = opened,
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun CoverStateSection(
    opened: Opened,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement
            .spacedBy(12.dp),
    ) {
        val stateTextColor = when (opened.current) {
            true -> SmartHomeTheme.colors[ColorKeyToken.ForegroundBrandPrimary]
            false -> SmartHomeTheme.colors[ColorKeyToken.TextPrimary]
        }

        Text(
            text = opened.descriptor
                .localized(),
            lineHeight = 36.sp,
            fontSize = 30.sp,
            fontWeight = FontWeight.W700,
            color = stateTextColor,
        )

        Text(
            text = stringResource(Res.string.entity_capability_opened_current_state),
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
        )
    }
}

@Composable
private fun CoverPositionSection(
    position: Position,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement
            .spacedBy(12.dp),
    ) {
        Text(
            text = "${position.current}",
            lineHeight = 72.sp,
            fontSize = 72.sp,
            fontWeight = FontWeight.W700,
        )

        Text(
            text = stringResource(Res.string.entity_capability_position),
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
        )
    }
}

@Composable
private fun CoverMovementControlSection(
    entity: EntityIdentifier,
    orientation: Cover.Orientation,
    position: Position?,
    movement: Movement,
    opened: Opened,
    onAction: (EntityDetailModalAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement
            .spacedBy(12.dp),
    ) {
        CoverMovementButton(
            icon = when (orientation) {
                Cover.Orientation.Horizontal -> Icons.ChevronLeftRight
                Cover.Orientation.Vertical -> Icons.ChevronUp
            },
            text = Res.string.entity_capability_position_open,
            onClick = {
                if (movement.current != Movement.Direction.Opening) {
                    onAction.invoke(EntityDetailModalAction.MoveCover.Open(entity = entity))
                }
            },
            active = movement.current == Movement.Direction.Opening,
            disabled = position?.current?.equals(100.percent) ?: opened.current &&
                movement.current == Movement.Direction.Idle,
        )

        CoverMovementButton(
            icon = Icons.Square,
            text = Res.string.entity_capability_position_stop,
            onClick = {
                if (movement.current != Movement.Direction.Idle) {
                    onAction.invoke(EntityDetailModalAction.MoveCover.Stop(entity = entity))
                }
            },
            active = false,
            disabled = movement.current == Movement.Direction.Idle,
        )

        CoverMovementButton(
            icon = when (orientation) {
                Cover.Orientation.Horizontal -> Icons.ChevronRightLeft
                Cover.Orientation.Vertical -> Icons.ChevronDown
            },
            text = Res.string.entity_capability_position_close,
            onClick = {
                if (movement.current != Movement.Direction.Closing) {
                    onAction.invoke(EntityDetailModalAction.MoveCover.Close(entity = entity))
                }
            },
            active = movement.current == Movement.Direction.Closing,
            disabled = position?.current?.equals(0.percent) ?: !opened.current &&
                movement.current == Movement.Direction.Idle,
        )
    }
}

@Composable
@OptIn(ExperimentalFoundationStyleApi::class)
private fun CoverMovementButton(
    icon: ImageVector,
    text: StringResource,
    onClick: () -> Unit,
    active: Boolean,
    disabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val style = if (active) {
        ButtonStyle.primary()
    } else {
        ButtonStyle.secondary()
    }

    val foreground = if (active) {
        PaletteTokens.Base.White
    } else if (disabled) {
        ButtonTokens.DisabledContentColor
    } else {
        SmartHomeTheme.colors[ColorKeyToken.TextPrimary]
    }

    Button(
        onClick = onClick,
        style = style then Style {
            borderWidth(1.dp)
            contentPadding(
                horizontal = 16.dp,
                vertical = 20.dp,
            )
        },
        modifier = modifier
            .fillMaxWidth(),
        enabled = !disabled,
    ) {
        Row(
            horizontalArrangement = Arrangement
                .spacedBy(20.dp),
        ) {
            Icon(
                icon = icon,
                size = 16.dp,
                tint = foreground,
            )

            Text(
                text = stringResource(text),
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
            )
        }
    }
}

@Preview
@Composable
private fun CoverControlSectionOpenedOnlyPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        CoverControlSection(
            entity = EntityIdentifier("blinds.living-room"),
            orientation = Cover.Orientation.Vertical,
            opened = Opened(true),
            position = null,
            movement = null,
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun CoverControlSectionClosedOnlyPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        CoverControlSection(
            entity = EntityIdentifier("blinds.living-room"),
            orientation = Cover.Orientation.Vertical,
            opened = Opened(false),
            position = null,
            movement = null,
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun CoverControlSectionPositionOnlyPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        CoverControlSection(
            entity = EntityIdentifier("blinds.living-room"),
            orientation = Cover.Orientation.Vertical,
            opened = Opened(true),
            position = Position(current = 50.percent),
            movement = null,
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun CoverControlSectionHorizontalMovementPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        CoverControlSection(
            entity = EntityIdentifier("curtain.living-room"),
            orientation = Cover.Orientation.Horizontal,
            opened = Opened(true),
            position = Position(current = 50.percent),
            movement = Movement(current = Movement.Direction.Closing),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun CoverControlSectionVerticalMovementPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        CoverControlSection(
            entity = EntityIdentifier("curtain.living-room"),
            orientation = Cover.Orientation.Vertical,
            opened = Opened(true),
            position = Position(current = 50.percent),
            movement = Movement(current = Movement.Direction.Opening),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun CoverControlSectionIdleMovementPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        CoverControlSection(
            entity = EntityIdentifier("curtain.living-room"),
            orientation = Cover.Orientation.Horizontal,
            opened = Opened(false),
            position = null,
            movement = Movement(current = Movement.Direction.Idle),
            onAction = {},
        )
    }
}
