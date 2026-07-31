package network.marsys.smarthome.shared.feature.zones

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.Res
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.zones_description
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.zones_header
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.adaptive.Breakpoints
import network.marsys.smarthome.shared.library.design.annotation.PreviewFontScales
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.component.TextDefaults
import network.marsys.smarthome.shared.library.design.component.TextStyles
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.i18n.stringResource

@Composable
fun ZonesScreenView(
    modifier: Modifier = Modifier,
) {
    ZonesScreenViewContent(
        modifier = modifier,
    )
}

@Composable
fun ZonesScreenViewContent(
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
            ZonesScreenDescription()
        }
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
    modifier: Modifier = Modifier,
    zones: Int = 6,
) {
    @OptIn(ExperimentalFoundationStyleApi::class)
    Text(
        text = stringResource(Res.string.zones_description, zones),
        style = TextDefaults.description,
        modifier = modifier,
    )
}

@PreviewLocales
@PreviewFontScales
@PreviewScreenSizes
@Composable
private fun ZonesScreenModalPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        ZonesScreenViewContent()
    }
}
