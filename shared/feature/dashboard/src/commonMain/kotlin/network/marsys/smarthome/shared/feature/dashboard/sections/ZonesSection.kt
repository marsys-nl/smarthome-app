package network.marsys.smarthome.shared.feature.dashboard.sections

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.then
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.zone.Zone
import network.marsys.smarthome.shared.feature.dashboard.DashboardScreenAction
import network.marsys.smarthome.shared.feature.dashboard.DashboardScreenEntityData
import network.marsys.smarthome.shared.feature.dashboard.DashboardScreenState
import network.marsys.smarthome.shared.feature.dashboard.MAX_ZONES
import network.marsys.smarthome.shared.feature.dashboard.components.ShimmerCard
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.Res
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.zones_active_devices
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.zones_empty_description
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.zones_empty_title
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.zones_error_description
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.zones_error_retry
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.zones_error_title
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.zones_loading_description
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.zones_section_title
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.zones_see_all
import network.marsys.smarthome.shared.library.design.EntityCardDefaults
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Border
import network.marsys.smarthome.shared.library.design.component.Button
import network.marsys.smarthome.shared.library.design.component.ButtonStyle
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.ErrorIconButton
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.IconCard
import network.marsys.smarthome.shared.library.design.component.LoadingIndicator
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.component.TextDefaults
import network.marsys.smarthome.shared.library.design.component.TextStyles
import network.marsys.smarthome.shared.library.design.icons.Bath
import network.marsys.smarthome.shared.library.design.icons.Bed
import network.marsys.smarthome.shared.library.design.icons.Briefcase
import network.marsys.smarthome.shared.library.design.icons.Car
import network.marsys.smarthome.shared.library.design.icons.ChefHat
import network.marsys.smarthome.shared.library.design.icons.ChevronRight
import network.marsys.smarthome.shared.library.design.icons.Component
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Reset
import network.marsys.smarthome.shared.library.design.icons.Sofa
import network.marsys.smarthome.shared.library.design.icons.TriangleAlert
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.i18n.stringResource

@Composable
fun ZonesSection(
    state: DashboardScreenState.ZonesState,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(16.dp),
    ) {
        ZonesSectionHeader(
            condition = state.condition,
            onAction = onAction,
        )

        when (state.condition) {
            DashboardScreenState.Condition.Loading ->
                ZonesSectionLoadingContent()

            DashboardScreenState.Condition.Empty ->
                ZonesSectionEmptyContent()

            DashboardScreenState.Condition.Error ->
                ZonesSectionErrorContent(onAction = onAction)

            DashboardScreenState.Condition.Success ->
                ZonesSectionLoadedContent(
                    zones = state.zones,
                    onAction = onAction,
                )
        }
    }
}

@Composable
private fun ZonesSectionLoadingContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        @OptIn(ExperimentalFoundationStyleApi::class)
        LoadingIndicator(
            message = stringResource(Res.string.zones_loading_description),
        )

        @OptIn(ExperimentalGridApi::class)
        Grid(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            config = dashboardSectionGridConfig,
        ) {
            repeat(LOADING_CARD_COUNT) {
                ShimmerCard()
            }
        }
    }
}

private const val LOADING_CARD_COUNT = 4

@Composable
private fun ZonesSectionEmptyContent(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.colors(
            borderColor = SmartHomeTheme.colors[ColorKeyToken.BorderPrimary],
        ),
        contentPadding = PaddingValues(32.dp),
        border = Border.Dashed(1.dp),
    ) {
        @OptIn(ExperimentalFoundationStyleApi::class)
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconCard(
                icon = Icons.Component,
                colors = CardDefaults.colors(
                    contentColor = SmartHomeTheme.colors[ColorKeyToken.ForegroundPrimary],
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp),
                size = 64.dp,
            )

            Text(
                text = stringResource(Res.string.zones_empty_title),
                style = TextDefaults.header then TextStyles.centered,
                modifier = Modifier
                    .padding(bottom = 4.dp),
            )

            Text(
                text = stringResource(Res.string.zones_empty_description),
                style = TextDefaults.description then TextStyles.centered,
                minLines = 2,
            )
        }
    }
}

@Composable
private fun ZonesSectionErrorContent(
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.colors(
            backgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundErrorPrimary]),
            contentColor = SmartHomeTheme.colors[ColorKeyToken.TextErrorPrimary],
            borderColor = SmartHomeTheme.colors[ColorKeyToken.BorderErrorSubtle],
        ),
        contentPadding = PaddingValues(32.dp),
        border = Border.Solid(1.dp),
    ) {
        @OptIn(ExperimentalFoundationStyleApi::class)
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconCard(
                icon = Icons.TriangleAlert,
                colors = CardDefaults.colors(
                    backgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundErrorSecondary]),
                    contentColor = SmartHomeTheme.colors[ColorKeyToken.ForegroundErrorPrimary],
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp),
                size = 64.dp,
            )

            Text(
                text = stringResource(Res.string.zones_error_title),
                style = TextDefaults.header then TextStyles.centered,
                modifier = Modifier
                    .padding(bottom = 6.dp),
                color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
            )

            Text(
                text = stringResource(Res.string.zones_error_description),
                style = TextDefaults.description then TextStyles.centered,
                modifier = Modifier
                    .padding(bottom = 20.dp),
                color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
                minLines = 2,
            )

            ErrorIconButton(
                text = stringResource(Res.string.zones_error_retry),
                icon = Icons.Reset,
                onClick = {
                    onAction.invoke(DashboardScreenAction.RetryZones)
                },
            )
        }
    }
}

@Composable
private fun ZonesSectionLoadedContent(
    @Suppress("UnstableCollections")
    zones: Map<EntityIdentifier, DashboardScreenState.ZoneState>,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    @OptIn(ExperimentalGridApi::class)
    Grid(
        modifier = modifier,
        config = dashboardSectionGridConfig,
    ) {
        zones.forEach { (identifier, item) ->
            key(identifier) {
                val clickModifier = remember(item.zone.identifier) {
                    Modifier.clickable(
                        interactionSource = null,
                        indication = null,
                    ) {
                        onAction.invoke(
                            DashboardScreenAction.OpenZoneScreen(
                                zone = item.zone.identifier,
                            ),
                        )
                    }
                }

                ZonesSectionZoneCard(
                    state = item,
                    modifier = clickModifier,
                )
            }
        }
    }
}

@OptIn(ExperimentalGridApi::class, ExperimentalFoundationStyleApi::class)
@Composable
private fun ZonesSectionZoneCard(
    state: DashboardScreenState.ZoneState,
    modifier: Modifier = Modifier,
) {
    val total = state.entities.size
    val active = state.entities.values.count {
        it is Entity.Activatable && it.active
    }

    Card(
        modifier = modifier,
        contentPadding = EntityCardDefaults.contentPadding(),
        border = Border.Solid(1.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                ZoneIcon(
                    zone = state.zone,
                    active = active,
                )

                Text(
                    text = stringResource(state.zone.identifier),
                    style = TextDefaults.header,
                    modifier = Modifier
                        .padding(top = 8.dp),
                    color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
                )

                Text(
                    text = stringResource(
                        Res.string.zones_active_devices,
                        active,
                        total,
                    ),
                    style = TextDefaults.description,
                    modifier = Modifier
                        .padding(top = 4.dp),
                )
            }

            if (active > 0) {
                ActiveZoneIndicator(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                )
            }
        }
    }
}

@Composable
private fun ZoneIcon(
    zone: Zone,
    active: Int,
    modifier: Modifier = Modifier,
) {
    val background = if (active > 0) {
        SmartHomeTheme.colors[ColorKeyToken.BackgroundSuccessPrimary]
    } else {
        SmartHomeTheme.colors[ColorKeyToken.BackgroundTertiary]
    }

    val foreground = if (active > 0) {
        SmartHomeTheme.colors[ColorKeyToken.ForegroundSuccessPrimary]
    } else {
        SmartHomeTheme.colors[ColorKeyToken.TextSecondary]
    }

    Icon(
        icon = determineZoneIcon(zone.icon),
        modifier = modifier
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(background)
            .padding(12.dp),
        size = 24.dp,
        tint = foreground,
    )
}

private fun determineZoneIcon(icon: Zone.Icon) = when (icon) {
    Zone.Icon.Bathroom -> Icons.Bath
    Zone.Icon.Bedroom -> Icons.Bed
    Zone.Icon.Garage -> Icons.Car
    Zone.Icon.Kitchen -> Icons.ChefHat
    Zone.Icon.LivingRoom -> Icons.Sofa
    Zone.Icon.Office -> Icons.Briefcase
    Zone.Icon.Other -> Icons.Component
}

@Composable
private fun ActiveZoneIndicator(
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition("blinking-indicator")

    val indicatorColor by transition.animateColor(
        initialValue = PaletteTokens.Emerald.Emerald500,
        targetValue = PaletteTokens.Emerald.Emerald500
            .copy(alpha = .5f),
        label = "blinking-indicator-color",
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = CubicBezierEasing(.4f, 0f, .6f, 1f),
            ),
            repeatMode = RepeatMode.Reverse,
        ),
    )

    Box(
        modifier = modifier
            .width(8.dp)
            .aspectRatio(1f)
            .background(indicatorColor, CircleShape),
    )
}

@Composable
private fun ZonesSectionHeader(
    condition: DashboardScreenState.Condition,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionHeader(
        title = stringResource(Res.string.zones_section_title),
        modifier = modifier,
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered = interactionSource.collectIsHoveredAsState().value
        val isPressed = interactionSource.collectIsPressedAsState().value

        if (condition is DashboardScreenState.Condition.Success) {
            CompositionLocalProvider(
                LocalContentColor provides SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
            ) {
                @OptIn(ExperimentalFoundationStyleApi::class)
                Button(
                    onClick = {
                        onAction.invoke(DashboardScreenAction.NavigateToZones)
                    },
                    style = ButtonStyle.text(
                        pressedContent = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
                    ),
                    interactionSource = interactionSource,
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement
                            .spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(Res.string.zones_see_all),
                            style = TextDefaults.normal then Style {
                                fontWeight(FontWeight.W500)
                            },
                        )

                        Icon(
                            icon = Icons.ChevronRight,
                            size = 16.dp,
                            tint = when {
                                isPressed || isHovered -> SmartHomeTheme.colors[ColorKeyToken.TextPrimary]
                                else -> SmartHomeTheme.colors[ColorKeyToken.TextSecondary]
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ZonesSectionEmptyPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        ZonesSection(
            state = ZonesSectionPreviewData.empty(),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun ZonesSectionErrorPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        ZonesSection(
            state = ZonesSectionPreviewData.error(),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun ZonesSectionLoadedPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        ZonesSection(
            state = ZonesSectionPreviewData.loaded(),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun ZonesSectionLoadingPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        ZonesSection(
            state = ZonesSectionPreviewData.loading(),
            onAction = {},
        )
    }
}

internal object ZonesSectionPreviewData {
    fun empty() = object : DashboardScreenState.ZonesState {
        override val zones: Map<EntityIdentifier, DashboardScreenState.ZoneState> = emptyMap()
        override val condition: DashboardScreenState.Condition = DashboardScreenState.Condition.Empty
    }

    fun error() = object : DashboardScreenState.ZonesState {
        override val zones: Map<EntityIdentifier, DashboardScreenState.ZoneState> = emptyMap()
        override val condition: DashboardScreenState.Condition = DashboardScreenState.Condition.Error
    }

    fun loaded() = object : DashboardScreenState.ZonesState {
        override val zones: Map<EntityIdentifier, DashboardScreenState.ZoneState> = DashboardScreenEntityData.zones
            .take(MAX_ZONES)
            .associateBy { it.identifier }
            .mapValues { (_, zone) ->
                object : DashboardScreenState.ZoneState {
                    override val zone: Zone = zone
                    override val entities: Map<EntityIdentifier, Entity<*>> = DashboardScreenEntityData.entities
                        .filter { it.zone?.identifier == zone.identifier }
                        .associateBy { it.identifier }
                }
            }

        override val condition: DashboardScreenState.Condition = DashboardScreenState.Condition.Success
    }

    fun loading() = object : DashboardScreenState.ZonesState {
        override val zones: Map<EntityIdentifier, DashboardScreenState.ZoneState> = emptyMap()
        override val condition: DashboardScreenState.Condition = DashboardScreenState.Condition.Loading
    }
}
