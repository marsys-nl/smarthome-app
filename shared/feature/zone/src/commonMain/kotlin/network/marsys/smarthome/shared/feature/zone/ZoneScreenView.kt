package network.marsys.smarthome.shared.feature.zone

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.then
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.feature.zone.zone.generated.resources.Res
import network.marsys.smarthome.shared.feature.zone.zone.generated.resources.zone_description_nr_entities
import network.marsys.smarthome.shared.library.core.coroutines.collectEffectsWithLifecycle
import network.marsys.smarthome.shared.library.core.coroutines.produceStateWithLifecycle
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.adaptive.Breakpoints
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.ButtonStyle
import network.marsys.smarthome.shared.library.design.component.IconOnlyButton
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.component.TextDefaults
import network.marsys.smarthome.shared.library.design.component.TextStyles
import network.marsys.smarthome.shared.library.design.icons.ChevronLeft
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
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
        }
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
        modifier = modifier,
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

            val entities = when (state.condition) {
                is ZoneScreenState.Condition.Success -> pluralStringResource(
                    resource = Res.plurals.zone_description_nr_entities,
                    quantity = state.entities.size,
                )

                else -> "…"
            }

            Text(
                text = entities,
                style = TextDefaults.description,
            )
        }
    }
}

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
