package network.marsys.smarthome.shared.feature.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.feature.dashboard.components.DashboardHeader
import network.marsys.smarthome.shared.feature.dashboard.entity.Action
import network.marsys.smarthome.shared.feature.dashboard.entity.Effect
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
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun DashboardScreenView(
    onChangeAppearanceClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel(),
    instant: Instant = Clock.System.now(),
    content: @Composable () -> Unit,
) {
    val state = viewModel.produceStateWithLifecycle()
    viewModel.collectEffectsWithLifecycle { effect ->
        when (effect) {
            is Effect.OpenAppearanceModal -> onChangeAppearanceClick()
        }
    }

    DashboardScreenViewScreen(
        name = state.user,
        groupEntitiesByType = state.quickControlState.groupedEntitiesByType,
        onAction = viewModel.accept,
        modifier = modifier,
        instant = instant,
        content = content,
    )
}

@Composable
private fun DashboardScreenViewScreen(
    name: String,
    groupEntitiesByType: Boolean,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier,
    instant: Instant = Clock.System.now(),
    content: @Composable () -> Unit,
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
                    groupEntitiesByType = groupEntitiesByType,
                    onAction = onAction,
                    content = content,
                )
            },
            splitPane = {
                SplitPaneDashboard(
                    groupEntitiesByType = groupEntitiesByType,
                    onAction = onAction,
                    content = content,
                )
            },
        )
    }
}

@Composable
private fun SinglePaneDashboard(
    groupEntitiesByType: Boolean,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        QuickControlSection(
            groupEntitiesByType = groupEntitiesByType,
            onAction = onAction,
        )

        content.invoke()
    }
}

@Composable
private fun SplitPaneDashboard(
    groupEntitiesByType: Boolean,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    SplitPane(
        modifier = modifier
            .fillMaxWidth(),
        left = content,
        right = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                QuickControlSection(
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
        DashboardScreenViewScreen(
            name = "John",
            groupEntitiesByType = false,
            onAction = {},
        ) {
            // No-op for now
        }
    }
}
