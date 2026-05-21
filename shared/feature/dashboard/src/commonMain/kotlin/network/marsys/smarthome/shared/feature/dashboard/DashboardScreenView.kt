package network.marsys.smarthome.shared.feature.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.feature.dashboard.components.DashboardHeader
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.adaptive.AdaptiveScaffold
import network.marsys.smarthome.shared.library.design.adaptive.PanePolicy
import network.marsys.smarthome.shared.library.design.adaptive.SplitPane
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun DashboardScreenView(
    name: String,
    onChangeAppearanceClick: () -> Unit,
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
            onChangeAppearanceClick = onChangeAppearanceClick,
        )

        AdaptiveScaffold(
            modifier = Modifier
                .padding(top = 32.dp),
            panePolicy = PanePolicy.AllowSplit(
                minimumWidthDp = 1000,
            ),
            singlePane = {
                Column {
                    Text(text = "Single pane content")

                    content.invoke()
                }
            },
            splitPane = {
                SplitPane(
                    left = {
                        content.invoke()
                    },
                    right = {
                        Text(text = "Split pane content")
                    },
                )
            },
        )
    }
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
        DashboardScreenView(
            name = "John",
            onChangeAppearanceClick = {},
        ) {
            // No-op for now
        }
    }
}
