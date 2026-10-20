package network.marsys.smarthome.shared.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.feature.dashboard.components.DashboardHeader
import network.marsys.smarthome.shared.feature.dashboard.sections.QuickControlSection
import network.marsys.smarthome.shared.feature.dashboard.sections.QuickControlSectionPreviewData
import network.marsys.smarthome.shared.feature.dashboard.sections.ZonesSection
import network.marsys.smarthome.shared.feature.dashboard.sections.ZonesSectionPreviewData
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
        state = state,
        onAction = viewModel.accept,
        modifier = modifier,
        instant = instant,
    )
}

@Composable
private fun DashboardScreenViewContent(
    state: DashboardScreenState,
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
            name = state.user,
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
                    state = state,
                    onAction = onAction,
                )
            },
            splitPane = {
                SplitPaneDashboard(
                    state = state,
                    onAction = onAction,
                )
            },
        )
    }
}

@Composable
private fun SinglePaneDashboard(
    state: DashboardScreenState,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement
            .spacedBy(32.dp),
    ) {
        ZonesSection(
            state = state.zonesState,
            onAction = onAction,
        )

        QuickControlSection(
            state = state.quickControlState,
            onAction = onAction,
        )
    }
}

@Composable
private fun SplitPaneDashboard(
    state: DashboardScreenState,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    SplitPane(
        modifier = modifier
            .fillMaxWidth(),
        left = {
            ZonesSection(
                state = state.zonesState,
                onAction = onAction,
            )
        },
        right = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                QuickControlSection(
                    state = state.quickControlState,
                    onAction = onAction,
                )
            }
        },
        spacerWidth = 40.dp,
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
            state = DashboardScreenPreviewData.loaded(),
            onAction = {},
        )
    }
}

internal object DashboardScreenPreviewData {
    fun loaded(
        zonesState: DashboardScreenState.ZonesState =
            ZonesSectionPreviewData.loaded(),
        quickControlState: DashboardScreenState.QuickControlState =
            QuickControlSectionPreviewData.loaded(),
    ): DashboardScreenState = object : DashboardScreenState {
        override val zonesState: DashboardScreenState.ZonesState = zonesState
        override val quickControlState: DashboardScreenState.QuickControlState = quickControlState
        override val user: String = "John"
    }
}
