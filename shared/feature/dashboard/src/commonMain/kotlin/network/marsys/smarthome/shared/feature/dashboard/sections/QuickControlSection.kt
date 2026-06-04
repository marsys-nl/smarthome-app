package network.marsys.smarthome.shared.feature.dashboard.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.Res
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.quick_control_section_title
import network.marsys.smarthome.shared.feature.dashboard.sections.controls.GroupEntitiesButton
import network.marsys.smarthome.shared.feature.dashboard.sections.controls.GroupedEntityHeader
import network.marsys.smarthome.shared.feature.dashboard.sections.controls.GroupedEntityHeaderDefaults
import network.marsys.smarthome.shared.library.design.EntityCard
import network.marsys.smarthome.shared.library.design.EntityCardDefaults
import network.marsys.smarthome.shared.library.design.EntityCardScope
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Lightbulb
import network.marsys.smarthome.shared.library.design.icons.Thermostat
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import org.jetbrains.compose.resources.stringResource

@Composable
fun QuickControlSection(
    modifier: Modifier = Modifier,
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

        Lights()
        Thermostats()
    }
}

@Composable
@Suppress("LongMethod")
private fun ColumnScope.Lights() {
    val groupedEntityHeaderColors = GroupedEntityHeaderDefaults.colors(
        markerBackgroundColor = GradientTokens.Amber.Amber400.ToOrange500,
        textColor = PaletteTokens.Amber.Amber300,
    )

    val entityCardColors = EntityCardDefaults.activeCardColors(
        background = GradientTokens.Amber.Amber400.ToOrange500,
        border = PaletteTokens.Amber.Amber400
            .copy(alpha = .4f),
    )

    GroupedEntityHeader(
        title = "Lights",
        count = 5,
        colors = groupedEntityHeaderColors,
    )

    LazyVerticalGrid(
        columns = GridCells
            .Adaptive(minSize = 150.dp),
        horizontalArrangement = Arrangement
            .spacedBy(16.dp),
        verticalArrangement = Arrangement
            .spacedBy(16.dp),
    ) {
        item {
            EntityCard(
                title = "Bedroom lamp",
                subtitle = "69% brightness",
                icon = Icons.Lightbulb,
                active = true,
                activeColors = entityCardColors,
            ) {
                EntityCardScope.Switch(
                    checked = true,
                    onCheckedChange = {},
                )
            }
        }

        item {
            EntityCard(
                title = "Porch lamp",
                subtitle = "On",
                icon = Icons.Lightbulb,
                active = true,
                activeColors = entityCardColors,
            ) {
                EntityCardScope.Switch(
                    checked = true,
                    onCheckedChange = {},
                )
            }
        }

        item {
            EntityCard(
                title = "Kitchen light",
                subtitle = "55% brightness",
                icon = Icons.Lightbulb,
                active = true,
                activeColors = entityCardColors,
            )
        }

        item {
            EntityCard(
                title = "Ceiling light",
                subtitle = "Off",
                icon = Icons.Lightbulb,
                active = false,
                activeColors = entityCardColors,
            ) {
                EntityCardScope.Switch(
                    checked = false,
                    onCheckedChange = {},
                )
            }
        }

        item {
            EntityCard(
                title = "Bathroom light",
                subtitle = "Off",
                icon = Icons.Lightbulb,
                active = false,
                activeColors = entityCardColors,
            )
        }
    }
}

@Composable
@Suppress("LongMethod")
private fun ColumnScope.Thermostats() {
    val groupedEntityHeaderColors = GroupedEntityHeaderDefaults.colors(
        markerBackgroundColor = GradientTokens.Rose.Rose400.ToRose600,
        textColor = PaletteTokens.Rose.Rose300,
    )

    val entityCardColors = EntityCardDefaults.activeCardColors(
        background = GradientTokens.Rose.Rose400.ToRose600,
        border = PaletteTokens.Rose.Rose400
            .copy(alpha = .4f),
    )

    GroupedEntityHeader(
        title = "Thermostats",
        count = 4,
        colors = groupedEntityHeaderColors,
    )

    LazyVerticalGrid(
        columns = GridCells
            .Adaptive(minSize = 150.dp),
        horizontalArrangement = Arrangement
            .spacedBy(16.dp),
        verticalArrangement = Arrangement
            .spacedBy(16.dp),
    ) {
        item {
            EntityCard(
                title = "Office",
                subtitle = "18°C",
                icon = Icons.Thermostat,
                active = true,
                activeColors = entityCardColors,
            )
        }

        item {
            EntityCard(
                title = "Nursery",
                subtitle = "30°C • Idle",
                icon = Icons.Thermostat,
                active = true,
                activeColors = entityCardColors,
            )
        }

        item {
            EntityCard(
                title = "Main bedroom",
                subtitle = "18°C • Heating",
                icon = Icons.Thermostat,
                active = true,
                activeColors = entityCardColors,
            )
        }

        item {
            EntityCard(
                title = "Floor heating",
                subtitle = "Idle",
                icon = Icons.Thermostat,
                active = false,
                activeColors = entityCardColors,
            )
        }
    }
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
