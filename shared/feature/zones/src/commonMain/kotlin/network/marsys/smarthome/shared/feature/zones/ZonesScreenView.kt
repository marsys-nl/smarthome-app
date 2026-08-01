package network.marsys.smarthome.shared.feature.zones

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.then
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.Res
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.zones_description
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
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.LoadingIndicator
import network.marsys.smarthome.shared.library.design.component.ShimmerBox
import network.marsys.smarthome.shared.library.design.component.ShimmerBoxDefaults
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.component.TextDefaults
import network.marsys.smarthome.shared.library.design.component.TextStyles
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
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
    Column(
        modifier = modifier,
    ) {
        // No-op for now
    }
}

@Composable
private fun ZonesScreenErrorViewContent(
    @Suppress("UnusedParameter", "UNUSED_PARAMETER")
    onAction: (ZonesScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        // No-op for now
    }
}

@Composable
private fun ZonesScreenLoadedViewContent(
    state: ZonesScreenState,
    @Suppress("UnusedParameter", "UNUSED_PARAMETER")
    onAction: (ZonesScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        ZonesScreenDescription(
            zones = state.zones.size,
        )
    }
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
        text = stringResource(Res.string.zones_description, zones),
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
