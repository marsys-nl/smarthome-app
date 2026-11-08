package network.marsys.smarthome.shared.modal.entity.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Fraction
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.domain.unit.percent
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Cpu
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.MemoryStick
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken

@Composable
fun PercentageDisplay(
    quantity: Quantity<Dimension.Ratio>,
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement
            .spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement
                .spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon = icon,
                size = 20.dp,
            )

            Text(
                text = title,
                modifier = Modifier
                    .weight(1f),
                lineHeight = 20.sp,
                fontSize = 14.sp,
            )

            Text(
                text = quantity.format(),
                lineHeight = 20.sp,
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(percent = 100))
                .fillMaxWidth()
                .height(8.dp)
                .background(
                    color = SmartHomeTheme.colors[ColorKeyToken.BackgroundTertiaryAlternative],
                    shape = RoundedCornerShape(percent = 100),
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        fraction = (quantity into Fraction).value.toFloat(),
                    )
                    .height(8.dp)
                    .background(
                        color = SmartHomeTheme.colors[ColorKeyToken.BackgroundBrandSecondary],
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun MemoryPercentageDisplayPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        PercentageDisplay(
            quantity = 78.12314.percent,
            icon = Icons.MemoryStick,
            title = "Memory",
        )
    }
}

@Preview
@Composable
private fun ProcessorPercentageDisplayPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        PercentageDisplay(
            quantity = 50.percent,
            icon = Icons.Cpu,
            title = "CPU",
        )
    }
}
