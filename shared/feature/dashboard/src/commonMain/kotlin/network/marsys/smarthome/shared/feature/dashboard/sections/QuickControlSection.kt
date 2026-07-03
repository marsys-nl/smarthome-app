package network.marsys.smarthome.shared.feature.dashboard.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import network.marsys.smarthome.shared.domain.entity.entity.Cover
import network.marsys.smarthome.shared.domain.entity.entity.Camera
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.Fan
import network.marsys.smarthome.shared.domain.entity.entity.Light
import network.marsys.smarthome.shared.domain.entity.entity.Lock
import network.marsys.smarthome.shared.domain.entity.entity.SmartPlug
import network.marsys.smarthome.shared.domain.entity.entity.Speaker
import network.marsys.smarthome.shared.domain.entity.entity.Thermostat
import network.marsys.smarthome.shared.feature.dashboard.DashboardScreenAction
import network.marsys.smarthome.shared.feature.dashboard.DashboardScreenPreviewData
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.Res
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.quick_control_section_title
import network.marsys.smarthome.shared.feature.dashboard.sections.controls.GroupEntitiesButton
import network.marsys.smarthome.shared.feature.dashboard.sections.controls.GroupedEntityHeader
import network.marsys.smarthome.shared.feature.dashboard.sections.controls.GroupedEntityHeaderColors
import network.marsys.smarthome.shared.feature.dashboard.sections.controls.GroupedEntityHeaderDefaults
import network.marsys.smarthome.shared.library.core.helper.ifPresent
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
import network.marsys.smarthome.shared.library.i18n.localized
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.library.resources.SmartHomeRes
import network.marsys.smarthome.shared.library.resources.entity_category_blinds
import network.marsys.smarthome.shared.library.resources.entity_category_cameras
import network.marsys.smarthome.shared.library.resources.entity_category_fans
import network.marsys.smarthome.shared.library.resources.entity_category_lights
import network.marsys.smarthome.shared.library.resources.entity_category_locks
import network.marsys.smarthome.shared.library.resources.entity_category_other
import network.marsys.smarthome.shared.library.resources.entity_category_smartplugs
import network.marsys.smarthome.shared.library.resources.entity_category_speakers
import network.marsys.smarthome.shared.library.resources.entity_category_thermostats
import kotlin.reflect.KClass

@Composable
fun QuickControlSection(
    @Suppress("UnstableCollections")
    entities: Map<EntityIdentifier, Entity<*>>,
    groupEntitiesByType: Boolean,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(16.dp),
    ) {
        SectionHeader(
            title = stringResource(Res.string.quick_control_section_title),
            right = {
                GroupEntitiesButton(
                    groupByType = groupEntitiesByType,
                    onClick = {
                        onAction.invoke(DashboardScreenAction.ToggleGroupEntitiesByType)
                    },
                )
            },
        )

        if (groupEntitiesByType) {
            QuickControlSectionGroupedEntities(
                entities = entities,
                onAction = onAction,
            )
        } else {
            val identifiers by remember(entities) {
                derivedStateOf { entities.keys.toImmutableList() }
            }

            QuickControlSectionEntities(
                entities = entities,
                identifiers = identifiers,
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun QuickControlSectionGroupedEntities(
    @Suppress("UnstableCollections")
    entities: Map<EntityIdentifier, Entity<*>>,
    onAction: (DashboardScreenAction) -> Unit,
) {
    val groupedIds: Map<KClass<out Entity<*>>, ImmutableList<EntityIdentifier>> by
        remember(entities) {
            derivedStateOf {
                entities.entries
                    .groupBy(
                        keySelector = { it.value::class },
                        valueTransform = { it.key },
                    )
                    .mapValues { (_, identifiers) ->
                        identifiers.toImmutableList()
                    }
            }
        }

    groupedIds.forEach { (type, identifiers) ->
        key(type) {
            GroupedEntityHeader(
                title = type.groupTitle(),
                count = identifiers.size,
                colors = type.headerColors(),
            )

            QuickControlSectionEntities(
                entities = entities,
                identifiers = identifiers,
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun QuickControlSectionEntities(
    @Suppress("UnstableCollections")
    entities: Map<EntityIdentifier, Entity<*>>,
    identifiers: ImmutableList<EntityIdentifier>,
    onAction: (DashboardScreenAction) -> Unit,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement
            .spacedBy(16.dp),
        verticalArrangement = Arrangement
            .spacedBy(16.dp),
    ) {
        identifiers.forEach { identifier ->
            key(identifier) {
                val entity = entities.getValue(identifier)

                QuickControlSectionEntityCard(
                    entity = entity,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
private fun FlowRowScope.QuickControlSectionEntityCard(
    entity: Entity<*>,
    onAction: (DashboardScreenAction) -> Unit,
) {
    val type = entity::class
    val icon = remember(type) { type.icon() }
    val colors = type.cardColors()

    val active = entity is Entity.Activatable && entity.active

    val clickModifier = remember(entity.identifier) {
        Modifier.clickable(
            interactionSource = null,
            indication = null,
        ) {
            onAction.invoke(
                DashboardScreenAction.OpenEntityDetailModal(
                    entity = entity.identifier,
                ),
            )
        }
    }

    EntityCard(
        title = stringResource(entity.identifier),
        subtitle = entity.descriptor
            .localized(),
        icon = icon,
        active = active,
        modifier = Modifier
            .widthIn(min = 150.dp)
            .weight(1f)
            .then(clickModifier),
        activeColors = colors,
    ) {
        entity.ifPresent<OnOff> {
            Switch(
                checked = it.current,
                onCheckedChange = {
                    onAction.invoke(
                        DashboardScreenAction.ToggleEntityState(
                            entity = entity.identifier,
                            state = it,
                        ),
                    )
                },
            )
        }
    }
}

@Composable
private fun KClass<out Entity<*>>.groupTitle(): String =
    stringResource(
        resource = when (this) {
            Cover::class -> SmartHomeRes.string.entity_category_blinds
            Camera::class -> SmartHomeRes.string.entity_category_cameras
            Fan::class -> SmartHomeRes.string.entity_category_fans
            Light::class -> SmartHomeRes.string.entity_category_lights
            Lock::class -> SmartHomeRes.string.entity_category_locks
            SmartPlug::class -> SmartHomeRes.string.entity_category_smartplugs
            Speaker::class -> SmartHomeRes.string.entity_category_speakers
            Thermostat::class -> SmartHomeRes.string.entity_category_thermostats
            else -> SmartHomeRes.string.entity_category_other
        },
    )

private fun KClass<out Entity<*>>.icon(): ImageVector = when (this) {
    Light::class -> Icons.Lightbulb
    Thermostat::class -> Icons.Thermostat
    SmartPlug::class -> Icons.Plug
    Cover::class -> Icons.Blinds
    Camera::class -> Icons.Monitor
    else -> Icons.Component
}

@Composable
@Suppress("CyclomaticComplexMethod")
private fun KClass<out Entity<*>>.headerColors(): GroupedEntityHeaderColors = when (this) {
    Light::class -> GroupedEntityHeaderDefaults.colors(
        markerBackgroundColor = GradientTokens.Amber.Amber400.ToOrange500,
        textColor = when (LocalDarkMode.current) {
            true -> PaletteTokens.Amber.Amber300
            else -> PaletteTokens.Amber.Amber600
        },
    )

    Thermostat::class -> GroupedEntityHeaderDefaults.colors(
        markerBackgroundColor = GradientTokens.Rose.Rose400.ToRose600,
        textColor = when (LocalDarkMode.current) {
            true -> PaletteTokens.Rose.Rose300
            else -> PaletteTokens.Rose.Rose600
        },
    )

    SmartPlug::class -> GroupedEntityHeaderDefaults.colors(
        markerBackgroundColor = GradientTokens.Blue.Blue400.ToBlue600,
        textColor = when (LocalDarkMode.current) {
            true -> PaletteTokens.Blue.Blue300
            else -> PaletteTokens.Blue.Blue600
        },
    )

    Cover::class, Speaker::class -> GroupedEntityHeaderDefaults.colors(
        markerBackgroundColor = GradientTokens.Indigo.Indigo400.ToPurple600,
        textColor = when (LocalDarkMode.current) {
            true -> PaletteTokens.Indigo.Indigo300
            else -> PaletteTokens.Indigo.Indigo600
        },
    )

    Lock::class, Camera::class -> GroupedEntityHeaderDefaults.colors(
        markerBackgroundColor = GradientTokens.Emerald.Emerald400.ToTeal600,
        textColor = when (LocalDarkMode.current) {
            true -> PaletteTokens.Emerald.Emerald300
            else -> PaletteTokens.Emerald.Emerald600
        },
    )

    Fan::class -> GroupedEntityHeaderDefaults.colors(
        markerBackgroundColor = GradientTokens.Green.Green400.ToEmerald600,
        textColor = when (LocalDarkMode.current) {
            true -> PaletteTokens.Green.Green300
            else -> PaletteTokens.Green.Green600
        },
    )

    else -> GroupedEntityHeaderDefaults.colors()
}

@Composable
private fun KClass<out Entity<*>>.cardColors(): ActiveEntityCardColors = when (this) {
    Light::class -> EntityCardDefaults.activeCardColors(
        background = GradientTokens.Amber.Amber400.ToOrange500,
        border = PaletteTokens.Amber.Amber400
            .copy(alpha = .4f),
    )

    Thermostat::class -> EntityCardDefaults.activeCardColors(
        background = GradientTokens.Rose.Rose400.ToRose600,
        border = PaletteTokens.Rose.Rose400
            .copy(alpha = .4f),
    )

    SmartPlug::class -> EntityCardDefaults.activeCardColors(
        background = GradientTokens.Blue.Blue400.ToBlue600,
        border = PaletteTokens.Blue.Blue400
            .copy(alpha = .4f),
    )

    Cover::class, Speaker::class -> EntityCardDefaults.activeCardColors(
        background = GradientTokens.Indigo.Indigo400.ToPurple600,
        border = PaletteTokens.Indigo.Indigo400
            .copy(alpha = .4f),
    )

    Lock::class, Camera::class -> EntityCardDefaults.activeCardColors(
        background = GradientTokens.Emerald.Emerald400.ToTeal600,
        border = PaletteTokens.Emerald.Emerald400
            .copy(alpha = .4f),
    )

    Fan::class -> EntityCardDefaults.activeCardColors(
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
        QuickControlSection(
            entities = DashboardScreenPreviewData.entities
                .associateBy { it.identifier },
            groupEntitiesByType = false,
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun GroupedQuickControlSectionPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        QuickControlSection(
            entities = emptyMap(),
            groupEntitiesByType = true,
            onAction = {},
        )
    }
}
