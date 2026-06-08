package network.marsys.smarthome.shared.feature.dashboard.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.domain.entity.entity.Blind
import network.marsys.smarthome.shared.domain.entity.entity.Camera
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.Fan
import network.marsys.smarthome.shared.domain.entity.entity.Light
import network.marsys.smarthome.shared.domain.entity.entity.Lock
import network.marsys.smarthome.shared.domain.entity.entity.SmartPlug
import network.marsys.smarthome.shared.domain.entity.entity.Speaker
import network.marsys.smarthome.shared.domain.entity.entity.Thermostat
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.Res
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.quick_control_section_title
import network.marsys.smarthome.shared.feature.dashboard.demo.DemoEntities
import network.marsys.smarthome.shared.feature.dashboard.sections.controls.GroupEntitiesButton
import network.marsys.smarthome.shared.feature.dashboard.sections.controls.GroupedEntityHeader
import network.marsys.smarthome.shared.feature.dashboard.sections.controls.GroupedEntityHeaderColors
import network.marsys.smarthome.shared.feature.dashboard.sections.controls.GroupedEntityHeaderDefaults
import network.marsys.smarthome.shared.library.design.ActiveEntityCardColors
import network.marsys.smarthome.shared.library.design.EntityCard
import network.marsys.smarthome.shared.library.design.EntityCardDefaults
import network.marsys.smarthome.shared.library.design.LocalDarkMode
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.icons.Blinds
import network.marsys.smarthome.shared.library.design.icons.Component
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Lightbulb
import network.marsys.smarthome.shared.library.design.icons.Monitor
import network.marsys.smarthome.shared.library.design.icons.Plug
import network.marsys.smarthome.shared.library.design.icons.Thermostat
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import org.jetbrains.compose.resources.stringResource

@Composable
fun QuickControlSection(
    modifier: Modifier = Modifier,
    entityList: EntityList = EntityList(entities = DemoEntities),
) {
    var groupByType by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(16.dp),
    ) {
        SectionHeader(
            title = stringResource(Res.string.quick_control_section_title),
            right = {
                GroupEntitiesButton(
                    groupByType = groupByType,
                    onClick = {
                        groupByType = !groupByType
                    },
                )
            },
        )

        if (groupByType) {
            QuickControlSectionGroupedEntities(entityList = entityList)
        } else {
            QuickControlSectionEntities(entityList = entityList)
        }
    }
}

@Immutable
data class EntityList(
    val entities: List<Entity<*>>,
)

@Composable
private fun QuickControlSectionGroupedEntities(
    entityList: EntityList,
) {
    entityList.entities
        .groupBy { it::class }
        .map { EntityList(it.value) }
        .forEach {
            QuickControlSectionEntityGroup(entityList = it)
        }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuickControlSectionEntityGroup(
    entityList: EntityList,
) {
    val representative = entityList.entities.first()

    GroupedEntityHeader(
        title = representative.groupTitle(),
        count = entityList.entities.size,
        colors = representative.headerColors(),
    )

    QuickControlSectionEntities(
        entityList = entityList,
    )
}

@Composable
private fun QuickControlSectionEntities(
    entityList: EntityList,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement
            .spacedBy(16.dp),
        verticalArrangement = Arrangement
            .spacedBy(16.dp),
    ) {
        entityList.entities.forEach { entity ->
            EntityCard(
                title = entity.label,
                subtitle = entity.description,
                icon = entity.icon(),
                active = entity is Entity.Activatable
                    && entity.active,
                modifier = Modifier
                    .widthIn(min = 150.dp)
                    .weight(1f),
                activeColors = entity.cardColors(),
            ) {
                if (entity is Entity.Toggleable) {
                    Switch(
                        checked = entity is Entity.Activatable
                            && entity.active,
                        onCheckedChange = {
                            // NO-OP for now, implement toggle logic
                        },
                    )
                }
            }
        }
    }
}

private fun Entity<*>.groupTitle(): String = when (this) {
    is Light -> "Lights"
    is Thermostat -> "Thermostats"
    is SmartPlug -> "Plugs"
    is Blind -> "Blinds"
    is Fan -> "Fans"
    is Speaker -> "Speakers"
    is Camera -> "Cameras"
    is Lock -> "Locks"
    else -> "Devices"
}

private fun Entity<*>.icon(): ImageVector = when (this) {
    is Light -> Icons.Lightbulb
    is Thermostat -> Icons.Thermostat
    is SmartPlug -> Icons.Plug
    is Blind -> Icons.Blinds
    is Camera -> Icons.Monitor
    else -> Icons.Component
}

@Composable
@Suppress("CyclomaticComplexMethod")
private fun Entity<*>.headerColors(): GroupedEntityHeaderColors = when (this) {
    is Light -> GroupedEntityHeaderDefaults.colors(
        markerBackgroundColor = GradientTokens.Amber.Amber400.ToOrange500,
        textColor = when (LocalDarkMode.current) {
            true -> PaletteTokens.Amber.Amber300
            else -> PaletteTokens.Amber.Amber600
        },
    )

    is Thermostat -> GroupedEntityHeaderDefaults.colors(
        markerBackgroundColor = GradientTokens.Rose.Rose400.ToRose600,
        textColor = when (LocalDarkMode.current) {
            true -> PaletteTokens.Rose.Rose300
            else -> PaletteTokens.Rose.Rose600
        },
    )

    is SmartPlug -> GroupedEntityHeaderDefaults.colors(
        markerBackgroundColor = GradientTokens.Blue.Blue400.ToBlue600,
        textColor = when (LocalDarkMode.current) {
            true -> PaletteTokens.Blue.Blue300
            else -> PaletteTokens.Blue.Blue600
        },
    )

    is Blind, is Speaker -> GroupedEntityHeaderDefaults.colors(
        markerBackgroundColor = GradientTokens.Indigo.Indigo400.ToPurple600,
        textColor = when (LocalDarkMode.current) {
            true -> PaletteTokens.Indigo.Indigo300
            else -> PaletteTokens.Indigo.Indigo600
        },
    )

    is Lock, is Camera -> GroupedEntityHeaderDefaults.colors(
        markerBackgroundColor = GradientTokens.Emerald.Emerald400.ToTeal600,
        textColor = when (LocalDarkMode.current) {
            true -> PaletteTokens.Emerald.Emerald300
            else -> PaletteTokens.Emerald.Emerald600
        },
    )

    is Fan -> GroupedEntityHeaderDefaults.colors(
        markerBackgroundColor = GradientTokens.Green.Green400.ToEmerald600,
        textColor = when (LocalDarkMode.current) {
            true -> PaletteTokens.Green.Green300
            else -> PaletteTokens.Green.Green600
        },
    )

    else -> GroupedEntityHeaderDefaults.colors()
}

@Composable
private fun Entity<*>.cardColors(): ActiveEntityCardColors = when (this) {
    is Light -> EntityCardDefaults.activeCardColors(
        background = GradientTokens.Amber.Amber400.ToOrange500,
        border = PaletteTokens.Amber.Amber400
            .copy(alpha = .4f),
    )

    is Thermostat -> EntityCardDefaults.activeCardColors(
        background = GradientTokens.Rose.Rose400.ToRose600,
        border = PaletteTokens.Rose.Rose400
            .copy(alpha = .4f),
    )

    is SmartPlug -> EntityCardDefaults.activeCardColors(
        background = GradientTokens.Blue.Blue400.ToBlue600,
        border = PaletteTokens.Blue.Blue400
            .copy(alpha = .4f),
    )

    is Blind, is Speaker -> EntityCardDefaults.activeCardColors(
        background = GradientTokens.Indigo.Indigo400.ToPurple600,
        border = PaletteTokens.Indigo.Indigo400
            .copy(alpha = .4f),
    )

    is Lock, is Camera -> EntityCardDefaults.activeCardColors(
        background = GradientTokens.Emerald.Emerald400.ToTeal600,
        border = PaletteTokens.Emerald.Emerald400
            .copy(alpha = .4f),
    )

    is Fan -> EntityCardDefaults.activeCardColors(
        background = GradientTokens.Green.Green400.ToEmerald600,
        border = PaletteTokens.Green.Green400
            .copy(alpha = .4f),
    )

    else -> EntityCardDefaults.activeCardColors()
}

@Preview
@Composable
private fun QuickControlSectionPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        QuickControlSection()
    }
}
