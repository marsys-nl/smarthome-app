package network.marsys.smarthome.shared.feature.dashboard.sections.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider

@Composable
fun GroupedEntityHeader(
    title: String,
    count: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement
            .spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp, 6.dp)
                .background(Color.Green),
        )

        Text(
            text = title
                .toUpperCase(LocalLocale.current),
            lineHeight = 16.sp,
            fontSize = 12.sp,
            letterSpacing = 1.sp,
            fontWeight = FontWeight.W700,
        )

        Text(
            text = "· $count",
            lineHeight = 16.sp,
            fontSize = 12.sp,
        )
    }
}

@Preview
@Composable
private fun GroupedEntityHeaderLightPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        GroupedEntityHeader(
            title = "Lights",
            count = 3,
        )
    }
}

@Preview
@Composable
private fun GroupedEntityHeaderThermostatPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        GroupedEntityHeader(
            title = "Thermostats",
            count = 3,
        )
    }
}

@Preview
@Composable
private fun GroupedEntityHeaderSmartPlugPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        GroupedEntityHeader(
            title = "Plugs",
            count = 3,
        )
    }
}

@Preview
@Composable
private fun GroupedEntityHeaderBlindsPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        GroupedEntityHeader(
            title = "Blinds",
            count = 3,
        )
    }
}
