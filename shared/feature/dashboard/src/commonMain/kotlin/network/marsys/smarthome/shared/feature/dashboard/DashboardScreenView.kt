package network.marsys.smarthome.shared.feature.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.feature.dashboard.components.DashboardHeader
import network.marsys.smarthome.shared.feature.dashboard.sections.QuickControlSection
import network.marsys.smarthome.shared.library.core.coroutines.collectEffectsWithLifecycle
import network.marsys.smarthome.shared.library.core.coroutines.produceStateWithLifecycle
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.adaptive.AdaptiveScaffold
import network.marsys.smarthome.shared.library.design.adaptive.PanePolicy
import network.marsys.smarthome.shared.library.design.adaptive.SplitPane
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.navigation.NavigationDestination
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun DashboardScreenView(
    onNavigate: (NavigationDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel(),
    instant: Instant = Clock.System.now(),
) {
    val state = viewModel.produceStateWithLifecycle()
    viewModel.collectEffectsWithLifecycle { effect ->
        when (effect) {
            is DashboardScreenEffect.Navigate -> onNavigate(effect.target)
        }
    }

    DashboardScreenViewContent(
        name = state.user,
        entities = state.quickControlState.entities,
        groupEntitiesByType = state.quickControlState.groupedEntitiesByType,
        onAction = viewModel.accept,
        modifier = modifier,
        instant = instant,
    )
}

@Composable
private fun DashboardScreenViewContent(
    name: String,
    @Suppress("UnstableCollections")
    entities: Map<EntityIdentifier, Entity<*>>,
    groupEntitiesByType: Boolean,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
    instant: Instant = Clock.System.now(),
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        DashboardHeader(
            instant = instant,
            name = name,
            onAction = onAction,
        )

        AdaptiveScaffold(
            modifier = Modifier
                .padding(top = 32.dp),
            panePolicy = PanePolicy.AllowSplit(
                minimumWidthDp = 1000,
            ),
            singlePane = {
                SinglePaneDashboard(
                    entities = entities,
                    groupEntitiesByType = groupEntitiesByType,
                    onAction = onAction,
                )
            },
            splitPane = {
                SplitPaneDashboard(
                    entities = entities,
                    groupEntitiesByType = groupEntitiesByType,
                    onAction = onAction,
                )
            },
        )
    }
}

@Composable
private fun SinglePaneDashboard(
    @Suppress("UnstableCollections")
    entities: Map<EntityIdentifier, Entity<*>>,
    groupEntitiesByType: Boolean,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        QuickControlSection(
            entities = entities,
            groupEntitiesByType = groupEntitiesByType,
            onAction = onAction,
        )
    }
}

@Composable
private fun SplitPaneDashboard(
    @Suppress("UnstableCollections")
    entities: Map<EntityIdentifier, Entity<*>>,
    groupEntitiesByType: Boolean,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    SplitPane(
        modifier = modifier
            .fillMaxWidth(),
        left = {
            // No-op for now
        },
        right = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                QuickControlSection(
                    entities = entities,
                    groupEntitiesByType = groupEntitiesByType,
                    onAction = onAction,
                )
            }
        },
    )
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun DashboardScreenViewPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        DashboardScreenViewContent(
            name = "John",
            entities = DashboardScreenEntityData.entities
                .associateBy { it.identifier },
            groupEntitiesByType = false,
            onAction = {},
        )
    }
}
