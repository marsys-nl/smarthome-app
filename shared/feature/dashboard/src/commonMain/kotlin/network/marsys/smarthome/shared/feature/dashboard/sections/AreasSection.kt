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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.area.Area
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.feature.dashboard.DashboardScreenAction
import network.marsys.smarthome.shared.feature.dashboard.DashboardScreenState
import network.marsys.smarthome.shared.feature.dashboard.components.ShimmerCard
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.Res
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.areas_empty_description
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.areas_empty_title
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.areas_loading_description
import network.marsys.smarthome.shared.library.design.EntityCardDefaults
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Border
import network.marsys.smarthome.shared.library.design.component.Button
import network.marsys.smarthome.shared.library.design.component.ButtonStyle
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.IconCard
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
import network.marsys.smarthome.shared.library.design.icons.Sofa
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.i18n.stringResource

@Composable
fun AreasSection(
    state: DashboardScreenState.AreasState,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(16.dp),
    ) {
        AreasSectionHeader(
            condition = state.condition,
            onAction = onAction,
        )

        when (state.condition) {
            DashboardScreenState.Condition.Loading ->
                AreasSectionLoadingContent()

            DashboardScreenState.Condition.Empty ->
                AreasSectionEmptyContent()

            DashboardScreenState.Condition.Success ->
                AreasSectionLoadedContent(
                    areas = state.areas,
                    onAction = onAction,
                )

            else -> Unit
        }
    }
}

@Composable
private fun AreasSectionLoadingContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        @OptIn(ExperimentalFoundationStyleApi::class)
        SectionLoadingIndicator(
            message = stringResource(Res.string.areas_loading_description),
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
private fun AreasSectionEmptyContent(
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
                text = stringResource(Res.string.areas_empty_title),
                style = TextDefaults.header then TextStyles.centered,
                modifier = Modifier
                    .padding(bottom = 4.dp),
            )

            Text(
                text = stringResource(Res.string.areas_empty_description),
                style = TextDefaults.description then TextStyles.centered,
                minLines = 2,
            )
        }
    }
}

@Composable
private fun AreasSectionLoadedContent(
    @Suppress("UnstableCollections")
    areas: Map<EntityIdentifier, DashboardScreenState.AreaState>,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    @OptIn(ExperimentalGridApi::class)
    Grid(
        modifier = modifier,
        config = dashboardSectionGridConfig,
    ) {
        areas.forEach { (identifier, item) ->
            key(identifier) {
                AreasSectionAreaCard(
                    state = item,
                    onAction = onAction,
                )
            }
        }
    }
}

@OptIn(ExperimentalGridApi::class, ExperimentalFoundationStyleApi::class)
@Composable
private fun AreasSectionAreaCard(
    state: DashboardScreenState.AreaState,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val clickModifier = remember(state.area.identifier) {
        Modifier.clickable(
            interactionSource = null,
            indication = null,
        ) {
            onAction.invoke(
                DashboardScreenAction.OpenAreaScreen(
                    area = state.area.identifier,
                ),
            )
        }
    }

    val total = state.entities.size
    val active = state.entities.values.count {
        it is Entity.Activatable && it.active
    }

    Card(
        modifier = modifier
            .then(clickModifier),
        contentPadding = EntityCardDefaults.contentPadding(),
        border = Border.Solid(1.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            AreaIcon(
                area = state.area,
                active = active,
            )

            Text(
                text = stringResource(state.area.identifier),
                style = TextDefaults.header,
                modifier = Modifier
                    .padding(top = 8.dp),
                color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
            )

            Text(
                text = "${active} of ${total} devices",
                style = TextDefaults.description,
                modifier = Modifier
                    .padding(top = 4.dp),
            )
        }

        if (active > 0) {
            ActiveAreaIndicator(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
            )
        }
    }
}

@Composable
private fun AreaIcon(
    area: Area,
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
        icon = areaIcon(area.icon),
        modifier = modifier
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(background)
            .padding(12.dp),
        size = 24.dp,
        tint = foreground,
    )
}

private fun areaIcon(icon: Area.Icon) = when (icon) {
    Area.Icon.Bathroom -> Icons.Bath
    Area.Icon.Bedroom -> Icons.Bed
    Area.Icon.Garage -> Icons.Car
    Area.Icon.Kitchen -> Icons.ChefHat
    Area.Icon.LivingRoom -> Icons.Sofa
    Area.Icon.Office -> Icons.Briefcase
    Area.Icon.Other -> Icons.Component
}

@Composable
private fun ActiveAreaIndicator(
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
private fun AreasSectionHeader(
    condition: DashboardScreenState.Condition,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionHeader(
        title = "Areas",
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
                        onAction.invoke(DashboardScreenAction.NavigateToAreas)
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
                            text = "See all",
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
private fun AreasSectionEmptyPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        AreasSection(
            state = AreasSectionPreviewData.empty(),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun AreasSectionErrorPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        AreasSection(
            state = AreasSectionPreviewData.error(),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun AreasSectionLoadedPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        AreasSection(
            state = AreasSectionPreviewData.loaded(),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun AreasSectionLoadingPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        AreasSection(
            state = AreasSectionPreviewData.loading(),
            onAction = {},
        )
    }
}

internal object AreasSectionPreviewData {
    fun empty() = object : DashboardScreenState.AreasState {
        override val areas: Map<EntityIdentifier, DashboardScreenState.AreaState> = emptyMap()
        override val condition: DashboardScreenState.Condition = DashboardScreenState.Condition.Empty
    }

    fun error() = object : DashboardScreenState.AreasState {
        override val areas: Map<EntityIdentifier, DashboardScreenState.AreaState> = emptyMap()
        override val condition: DashboardScreenState.Condition = DashboardScreenState.Condition.Error
    }

    fun loaded() = object : DashboardScreenState.AreasState {
        override val areas: Map<EntityIdentifier, DashboardScreenState.AreaState> = emptyMap()
        override val condition: DashboardScreenState.Condition = DashboardScreenState.Condition.Success
    }

    fun loading() = object : DashboardScreenState.AreasState {
        override val areas: Map<EntityIdentifier, DashboardScreenState.AreaState> = emptyMap()
        override val condition: DashboardScreenState.Condition = DashboardScreenState.Condition.Loading
    }
}
