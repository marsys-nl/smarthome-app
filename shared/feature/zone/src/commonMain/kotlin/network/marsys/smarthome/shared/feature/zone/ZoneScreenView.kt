package network.marsys.smarthome.shared.feature.zone

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.feature.zone.zone.generated.resources.Res
import network.marsys.smarthome.shared.feature.zone.zone.generated.resources.zone_description_nr_entities
import network.marsys.smarthome.shared.feature.zone.zone.generated.resources.zone_entities_empty_description
import network.marsys.smarthome.shared.feature.zone.zone.generated.resources.zone_entities_empty_title
import network.marsys.smarthome.shared.feature.zone.zone.generated.resources.zone_entities_error_description
import network.marsys.smarthome.shared.feature.zone.zone.generated.resources.zone_entities_error_retry
import network.marsys.smarthome.shared.feature.zone.zone.generated.resources.zone_entities_error_title
import network.marsys.smarthome.shared.library.core.coroutines.collectEffectsWithLifecycle
import network.marsys.smarthome.shared.library.core.coroutines.produceStateWithLifecycle
import network.marsys.smarthome.shared.library.core.helper.ifPresent
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.adaptive.Breakpoints
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.Border
import network.marsys.smarthome.shared.library.design.component.ButtonStyle
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.ErrorIconButton
import network.marsys.smarthome.shared.library.design.component.IconCard
import network.marsys.smarthome.shared.library.design.component.IconOnlyButton
import network.marsys.smarthome.shared.library.design.component.LoadingIndicator
import network.marsys.smarthome.shared.library.design.component.ShimmerBox
import network.marsys.smarthome.shared.library.design.component.ShimmerBoxDefaults
import network.marsys.smarthome.shared.library.design.component.Switch
import network.marsys.smarthome.shared.library.design.component.SwitchSize
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.component.TextDefaults
import network.marsys.smarthome.shared.library.design.component.TextStyles
import network.marsys.smarthome.shared.library.design.domain.icon
import network.marsys.smarthome.shared.library.design.icons.Blinds
import network.marsys.smarthome.shared.library.design.icons.ChevronLeft
import network.marsys.smarthome.shared.library.design.icons.Component
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Reset
import network.marsys.smarthome.shared.library.design.icons.TriangleAlert
import network.marsys.smarthome.shared.library.design.modifier.instantPressClickable
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.i18n.localized
import network.marsys.smarthome.shared.library.i18n.pluralStringResource
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.library.navigation.NavigationDestination
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ZoneScreenView(
    zone: EntityIdentifier,
    onNavigate: (NavigationDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<ZoneViewModel>(
        key = "zone-screen-view-$zone",
    ) {
        parametersOf(zone)
    }

    val state = viewModel.produceStateWithLifecycle()
    viewModel.collectEffectsWithLifecycle { effect ->
        when (effect) {
            is ZoneScreenEffect.Navigate -> onNavigate(effect.target)
        }
    }

    ZoneScreenViewContent(
        state = state,
        onAction = viewModel.accept,
        modifier = modifier,
    )
}

@Composable
private fun ZoneScreenViewContent(
    state: ZoneScreenState,
    onAction: (ZoneScreenAction) -> Unit,
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
            ZoneScreenHeader(
                state = state,
                onAction = onAction,
            )

            when (state.condition) {
                is ZoneScreenState.Condition.Loading ->
                    ZoneScreenLoadingViewContent()

                is ZoneScreenState.Condition.Empty ->
                    ZoneScreenEmptyViewContent()

                is ZoneScreenState.Condition.Error ->
                    ZoneScreenErrorViewContent(
                        onAction = onAction,
                    )

                is ZoneScreenState.Condition.Success ->
                    ZoneScreenLoadedViewContent(
                        state = state,
                        onAction = onAction,
                    )
            }
        }
    }
}

@Composable
private fun ZoneScreenLoadingViewContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(16.dp),
    ) {
        @OptIn(ExperimentalFoundationStyleApi::class)
        LoadingIndicator(
            message = "Loading entities…",
        )

        repeat(LOADING_ENTITY_SHIMMER_COUNT) {
            ZoneScreenShimmerRow()
        }
    }
}

@Composable
private fun ZoneScreenShimmerRow(
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
            horizontalArrangement = Arrangement
                .spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ShimmerBox(
                modifier = Modifier
                    .size(width = 48.dp, height = 48.dp),
            )

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement
                    .spacedBy(4.dp),
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .size(width = 112.dp, height = 16.dp),
                )

                ShimmerBox(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(width = 64.dp, height = 10.dp),
                    style = ShimmerBoxDefaults.defaultStyle(
                        backgroundColor = SmartHomeTheme.colors[ColorKeyToken.BackgroundTertiaryAlternative],
                    ),
                )
            }

            ShimmerBox(
                modifier = Modifier
                    .size(width = 36.dp, height = 20.dp),
            )
        }
    }
}

@Composable
private fun ZoneScreenEmptyViewContent(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
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
                text = stringResource(Res.string.zone_entities_empty_title),
                style = TextDefaults.header then TextStyles.centered,
                modifier = Modifier
                    .padding(bottom = 4.dp),
            )

            Text(
                text = stringResource(Res.string.zone_entities_empty_description),
                style = TextDefaults.description then TextStyles.centered,
                minLines = 2,
            )
        }
    }
}

@Composable
private fun ZoneScreenErrorViewContent(
    onAction: (ZoneScreenAction) -> Unit,
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
                text = stringResource(Res.string.zone_entities_error_title),
                style = TextDefaults.header then TextStyles.centered,
                modifier = Modifier
                    .padding(bottom = 6.dp),
                color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
            )

            Text(
                text = stringResource(Res.string.zone_entities_error_description),
                style = TextDefaults.description then TextStyles.centered,
                modifier = Modifier
                    .padding(bottom = 20.dp),
                color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
                minLines = 2,
            )

            ErrorIconButton(
                text = stringResource(Res.string.zone_entities_error_retry),
                icon = Icons.Reset,
                onClick = {
                    onAction.invoke(ZoneScreenAction.RetryLoadingEntities)
                },
            )
        }
    }
}

@Composable
private fun ZoneScreenLoadedViewContent(
    state: ZoneScreenState,
    onAction: (ZoneScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(12.dp),
    ) {
        state.entities.forEach { entity ->
            ZoneScreenEntityRow(
                entity = entity.value,
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun ZoneScreenEntityRow(
    entity: Entity<*>,
    onAction: (ZoneScreenAction) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .instantPressClickable(
                interactionSource = interactionSource,
                onClick = {
                    onAction.invoke(ZoneScreenAction.OpenEntityDetailModal(entity.identifier))
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
            EntityIcon(entity = entity)
            EntityDetails(entity = entity)

            entity.ifPresent<OnOff> {
                Switch(
                    checked = it.current,
                    onCheckedChange = { value ->
                        onAction.invoke(
                            ZoneScreenAction.ToggleEntityState(
                                entity = entity.identifier,
                                state = value,
                            ),
                        )
                    },
                    modifier = Modifier
                        .padding(start = 16.dp),
                    size = SwitchSize.Small,
                )
            }
        }
    }
}

@Composable
private fun EntityIcon(
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
            .padding(end = 16.dp),
        size = 48.dp,
    )
}

@Composable
@OptIn(ExperimentalFoundationStyleApi::class)
private fun RowScope.EntityDetails(
    entity: Entity<*>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .weight(1f),
        verticalArrangement = Arrangement
            .spacedBy(4.dp),
    ) {
        Text(
            text = stringResource(entity.identifier),
            style = TextDefaults.header then TextStyles.bold,
            color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = entity.descriptor
                .localized(),
            style = TextDefaults.description,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ZoneScreenHeader(
    state: ZoneScreenState,
    onAction: (ZoneScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    @OptIn(ExperimentalFoundationStyleApi::class)
    Row(
        modifier = modifier
            .padding(bottom = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconOnlyButton(
            icon = Icons.ChevronLeft,
            onClick = {
                onAction.invoke(ZoneScreenAction.NavigateToZones)
            },
            modifier = Modifier
                .padding(end = 16.dp),
            foregroundColor = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
            style = ButtonStyle.secondary(),
        )

        Column(
            modifier = Modifier,
        ) {
            Text(
                text = stringResource(state.zone),
                style = TextDefaults.title then TextStyles.bold,
                color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
            )

            Text(
                text = determineHeaderDescriptionText(state = state),
                style = TextDefaults.description,
            )
        }
    }
}

@Composable
private fun determineHeaderDescriptionText(state: ZoneScreenState): String = when (state.condition) {
    is ZoneScreenState.Condition.Loading -> "…"

    else -> pluralStringResource(
        resource = Res.plurals.zone_description_nr_entities,
        quantity = state.entities.size,
    )
}

private const val LOADING_ENTITY_SHIMMER_COUNT = 3

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun LoadingZoneScreenPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ZoneScreenViewContent(
            state = ZoneScreenPreviewData.loading(),
            onAction = {},
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun EmptyZoneScreenPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ZoneScreenViewContent(
            state = ZoneScreenPreviewData.empty(),
            onAction = {},
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun ErrorZoneScreenPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ZoneScreenViewContent(
            state = ZoneScreenPreviewData.error(),
            onAction = {},
        )
    }
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun LoadedZoneScreenPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ZoneScreenViewContent(
            state = ZoneScreenPreviewData.loaded(),
            onAction = {},
        )
    }
}

internal object ZoneScreenPreviewData {
    private val zone: EntityIdentifier = EntityIdentifier("zone.living-room")

    fun loading() = object : ZoneScreenState {
        override val condition = ZoneScreenState.Condition.Loading
        override val zone: EntityIdentifier = ZoneScreenPreviewData.zone
        override val entities: Map<EntityIdentifier, Entity<*>> = emptyMap()
    }

    fun empty() = object : ZoneScreenState {
        override val condition = ZoneScreenState.Condition.Empty
        override val zone: EntityIdentifier = ZoneScreenPreviewData.zone
        override val entities: Map<EntityIdentifier, Entity<*>> = emptyMap()
    }

    fun error() = object : ZoneScreenState {
        override val condition = ZoneScreenState.Condition.Error
        override val zone: EntityIdentifier = ZoneScreenPreviewData.zone
        override val entities: Map<EntityIdentifier, Entity<*>> = emptyMap()
    }

    fun loaded() = object : ZoneScreenState {
        override val condition = ZoneScreenState.Condition.Success
        override val zone: EntityIdentifier = ZoneScreenPreviewData.zone
        override val entities: Map<EntityIdentifier, Entity<*>> = emptyMap()
    }
}
