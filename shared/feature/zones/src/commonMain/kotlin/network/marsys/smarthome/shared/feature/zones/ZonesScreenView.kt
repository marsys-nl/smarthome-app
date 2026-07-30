package network.marsys.smarthome.shared.feature.zones

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.Res
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.zones_description
import network.marsys.smarthome.shared.feature.zones.zones.generated.resources.zones_header
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.adaptive.Breakpoints
import network.marsys.smarthome.shared.library.design.component.Text
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
    Text(
        text = stringResource(Res.string.zones_header),
        modifier = modifier,
        lineHeight = 32.sp,
        fontSize = 24.sp,
        fontWeight = FontWeight.W700,
    )
}

@Composable
private fun ZonesScreenDescription(
    modifier: Modifier = Modifier,
    zones: Int = 6,
) {
    Text(
        text = stringResource(Res.string.zones_description, zones),
        modifier = modifier,
        lineHeight = 20.sp,
        fontSize = 14.sp,
        color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
    )
}
