package network.marsys.smarthome.shared.feature.zones

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.then
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.Res
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.zones_active_entities
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.zones_description
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.zones_empty_description
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.zones_empty_title
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.zones_error_description
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.zones_error_retry
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.zones_error_title
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.zones_header
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.zones_loading_description
import network.marsys.smarthome.shared.library.core.coroutines.collectEffectsWithLifecycle
import network.marsys.smarthome.shared.library.core.coroutines.produceStateWithLifecycle
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.adaptive.Breakpoints
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.Border
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.ErrorIconButton
import network.marsys.smarthome.shared.library.design.component.IconCard
import network.marsys.smarthome.shared.library.design.component.LoadingIndicator
import network.marsys.smarthome.shared.library.design.component.ShimmerBox
import network.marsys.smarthome.shared.library.design.component.ShimmerBoxDefaults
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.component.TextDefaults
import network.marsys.smarthome.shared.library.design.component.TextStyles
import network.marsys.smarthome.shared.library.design.domain.icon
import network.marsys.smarthome.shared.library.design.icons.Component
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Reset
import network.marsys.smarthome.shared.library.design.icons.TriangleAlert
import network.marsys.smarthome.shared.library.design.modifier.instantPressClickable
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.i18n.pluralStringResource
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.library.navigation.NavigationDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ZonesScreenView(
    onNavigate: (NavigationDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ZonesViewModel = koinViewModel(),
) {
    val state = viewModel.produceStateWithLifecycle()
    viewModel.collectEffectsWithLifecycle { effect ->
        when (effect) {
            is ZonesScreenEffect.Navigate -> onNavigate(effect.target)
        }
    }

    ZonesScreenViewContent(
        state = state,
        onAction = viewModel.accept,
        modifier = modifier,
    )
}

@Composable
private fun ZonesScreenViewContent(
    state: ZonesScreenState,
    onAction: (ZonesScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = Breakpoints.MEDIUM.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            ZonesScreenHeader()

            when (state.condition) {
                is ZonesScreenState.Condition.Loading ->
                    ZonesScreenLoadingViewContent()

                is ZonesScreenState.Condition.Empty ->
                    ZonesScreenEmptyViewContent()

                is ZonesScreenState.Condition.Error ->
                    ZonesScreenErrorViewContent(
                        onAction = onAction,
                    )

                is ZonesScreenState.Condition.Success ->
                    ZonesScreenLoadedViewContent(
                        state = state,
                        onAction = onAction,
                    )
            }
        }
    }
}

@Composable
private fun ZonesScreenLoadingViewContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(16.dp),
    ) {
        @OptIn(ExperimentalFoundationStyleApi::class)
        LoadingIndicator(
            message = stringResource(Res.string.zones_loading_description),
        )

        repeat(LOADING_ZONES_SHIMMER_COUNT) {
            ZonesScreenShimmerRow()
        }
    }
}

@Composable
private fun ZonesScreenShimmerRow(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(24.dp),
    ) {
        @OptIn(ExperimentalFoundationStyleApi::class)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement
                    .spacedBy(4.dp),
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .size(width = 112.dp, height = 12.dp),
                )

                ShimmerBox(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(width = 80.dp, height = 10.dp),
                    style = ShimmerBoxDefaults.defaultStyle(
                        backgroundColor = SmartHomeTheme.colors[ColorKeyToken.BackgroundTertiaryAlternative],
                    ),
                )
            }

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement
                    .spacedBy(0.dp - 8.dp),
            ) {
                repeat(LOADING_ENTITY_SHIMMER_COUNT) {
                    ShimmerBox(
                        modifier = Modifier
                            .size(width = 40.dp, height = 40.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ZonesScreenEmptyViewContent(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(top = 52.dp),
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
private fun ZonesScreenErrorViewContent(
    onAction: (ZonesScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(top = 52.dp),
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
                    onAction.invoke(ZonesScreenAction.RetryZones)
                },
            )
        }
    }
}

@Composable
private fun ZonesScreenLoadedViewContent(
    state: ZonesScreenState,
    onAction: (ZonesScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(16.dp),
    ) {
        ZonesScreenDescription(
            zones = state.zones.size,
            modifier = Modifier
                .padding(bottom = 16.dp),
        )

        state.zones.values.forEach { zoneState ->
            ZonesScreenZoneRow(
                state = zoneState,
                onAction = onAction,
            )
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationStyleApi::class)
private fun ZonesScreenZoneRow(
    state: ZonesScreenState.ZoneState,
    onAction: (ZonesScreenAction) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val active = state.entities.values.count { it is Entity.Activatable && it.active }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .instantPressClickable(
                interactionSource = interactionSource,
                onClick = {
                    onAction.invoke(ZonesScreenAction.OpenZoneScreen(state.zone.identifier))
                },
            ),
        colors = CardDefaults.colors(
            pressedBackgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundDisabledAlternative]),
        ),
        contentPadding = PaddingValues(24.dp),
        interactionSource = interactionSource,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement
                    .spacedBy(4.dp),
            ) {
                Text(
                    text = stringResource(state.zone.identifier),
                    style = TextDefaults.title then TextStyles.bold,
                    color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
                )

                Text(
                    text = pluralStringResource(
                        resource = Res.plurals.zones_active_entities,
                        quantity = state.entities.size,
                        formatArgs = arrayOf(active, state.entities.size),
                    ),
                    style = TextDefaults.description,
                )
            }

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement
                    .spacedBy(0.dp - 8.dp),
            ) {
                state.entities.values
                    .take(ZONE_ENTITY_COUNT)
                    .forEachIndexed { index, entity ->
                        ZonesScreenEntityIcon(
                            index = index,
                            entity = entity,
                        )
                    }
            }
        }
    }
}

private const val ZONE_ENTITY_COUNT = 3

@Composable
private fun ZonesScreenEntityIcon(
    index: Int,
    entity: Entity<*>,
    modifier: Modifier = Modifier,
) {
    val colors = when (entity) {
        is Entity.Activatable if entity.active -> CardDefaults.colors(
            backgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundBrandPrimary]),
            contentColor = SmartHomeTheme.colors[ColorKeyToken.ForegroundPrimaryAlternative],
        )

        else -> CardDefaults.colors(
            backgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundTertiary]),
            contentColor = SmartHomeTheme.colors[ColorKeyToken.ForegroundPrimary],
        )
    }

    IconCard(
        icon = entity::class.icon(),
        colors = colors,
        modifier = modifier
            .size(width = 40.dp, height = 40.dp)
            .zIndex(index.toFloat()),
    )
}

@Composable
private fun ZonesScreenHeader(
    modifier: Modifier = Modifier,
) {
    @OptIn(ExperimentalFoundationStyleApi::class)
    Text(
        text = stringResource(Res.string.zones_header),
        style = TextDefaults.title then TextStyles.bold,
        modifier = modifier,
        color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
    )
}

@Composable
private fun ZonesScreenDescription(
    zones: Int,
    modifier: Modifier = Modifier,
) {
    @OptIn(ExperimentalFoundationStyleApi::class)
    Text(
        text = pluralStringResource(
            resource = Res.plurals.zones_description,
            quantity = zones,
        ),
        style = TextDefaults.description,
        modifier = modifier,
    )
}

private const val LOADING_ZONES_SHIMMER_COUNT = 4
private const val LOADING_ENTITY_SHIMMER_COUNT = 3

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun LoadingZonesScreenPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ZonesScreenViewContent(
            state = ZonesScreenPreviewData.loading(),
            onAction = {},
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun EmptyZonesScreenPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ZonesScreenViewContent(
            state = ZonesScreenPreviewData.empty(),
            onAction = {},
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun ErrorZonesScreenPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ZonesScreenViewContent(
            state = ZonesScreenPreviewData.error(),
            onAction = {},
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun LoadedZonesScreenPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ZonesScreenViewContent(
            state = ZonesScreenPreviewData.loaded(),
            onAction = {},
        )
    }
}

internal object ZonesScreenPreviewData {
    fun loading() = object : ZonesScreenState {
        override val condition: ZonesScreenState.Condition = ZonesScreenState.Condition.Loading
        override val zones: Map<EntityIdentifier, ZonesScreenState.ZoneState> = emptyMap()
    }

    fun empty() = object : ZonesScreenState {
        override val condition: ZonesScreenState.Condition = ZonesScreenState.Condition.Empty
        override val zones: Map<EntityIdentifier, ZonesScreenState.ZoneState> = emptyMap()
    }

    fun error() = object : ZonesScreenState {
        override val condition: ZonesScreenState.Condition = ZonesScreenState.Condition.Error
        override val zones: Map<EntityIdentifier, ZonesScreenState.ZoneState> = emptyMap()
    }

    fun loaded() = object : ZonesScreenState {
        override val condition: ZonesScreenState.Condition = ZonesScreenState.Condition.Success
        override val zones: Map<EntityIdentifier, ZonesScreenState.ZoneState> = emptyMap()
    }
}
