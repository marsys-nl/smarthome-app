package network.marsys.smarthome.shared.modal.entity.section

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.shared.domain.entity.capability.MeasureTemperature
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.Res
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_capability_temperature_current
import network.marsys.smarthome.shared.modal.entity.section.TemperatureGaugeDefaults.drawGauge
import network.marsys.smarthome.shared.modal.entity.section.TemperatureGaugeDefaults.drawPointer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun TemperatureControlSection(
    measureTemperature: MeasureTemperature,
    modifier: Modifier = Modifier,
    range: ClosedRange<Quantity<Dimension.Temperature>> = 0.celsius..50.celsius,
    colors: TemperatureGaugeColors = TemperatureGaugeDefaults.colors(),
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        TemperatureGauge(
            temperature = measureTemperature.current,
            range = range,
            colors = colors,
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(2.dp),
        ) {
            Text(
                text = "${measureTemperature.current}",
                fontSize = 36.sp,
                fontWeight = FontWeight.W700,
            )

            Text(
                text = stringResource(Res.string.entity_capability_temperature_current),
                lineHeight = 20.sp,
                fontSize = 14.sp,
                color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
            )
        }
    }
}

@Composable
private fun TemperatureGauge(
    temperature: Quantity<Dimension.Temperature>,
    range: ClosedRange<Quantity<Dimension.Temperature>>,
    colors: TemperatureGaugeColors,
    modifier: Modifier = Modifier,
) {
    val position = temperature.toGaugeOffset(range)
    val stops = remember(colors.stops, range) {
        colors.stops
            .map { (temperature, color) -> temperature.toGaugeOffset(range) to color }
            .toTypedArray()
    }

    Box(
        modifier = modifier
            .size(200.dp)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier
                .width(200.dp)
                .aspectRatio(1f),
        ) {
            val stroke = Stroke(
                width = 16.dp.toPx(),
                cap = StrokeCap.Round,
            )

            rotate(SWEEP_GRADIENT_ANGLE) {
                drawGauge(
                    brush = Brush.sweepGradient(
                        *stops,
                    ),
                    stroke = stroke,
                )

                drawPointer(
                    position = position,
                    brush = colors.pointer,
                    stroke = stroke,
                )
            }
        }
    }
}

private fun Quantity<Dimension.Temperature>.toGaugeOffset(
    range: ClosedRange<Quantity<Dimension.Temperature>>,
): Float = ((this - range.start).value / (range.endInclusive - range.start).value)
    .toFloat()
    .coerceIn(0f, 1f)

@Immutable
@ConsistentCopyVisibility
data class TemperatureGaugeColors internal constructor(
    internal val pointer: Brush,
    internal val stops: List<Pair<Quantity<Dimension.Temperature>, Color>>,
)

private object TemperatureGaugeDefaults {
    @Composable
    @Suppress("UnstableCollections")
    fun colors(
        pointer: Brush = SolidColor(SmartHomeTheme.colors[ColorKeyToken.TextPrimary]),
        stops: List<Pair<Quantity<Dimension.Temperature>, Color>> = listOf(
            10.celsius to PaletteTokens.Blue.Blue300,
            18.celsius to PaletteTokens.Emerald.Emerald200,
            28.celsius to PaletteTokens.Orange.Orange500,
            35.celsius to PaletteTokens.Red.Red400,
        ),
    ) = TemperatureGaugeColors(
        pointer = pointer,
        stops = stops,
    )

    fun DrawScope.drawGauge(
        brush: Brush,
        stroke: Stroke,
    ) {
        drawArc(
            brush = brush,
            startAngle = TEMPERATURE_GAUGE_START_ANGLE - SWEEP_GRADIENT_ANGLE,
            sweepAngle = TEMPERATURE_GAUGE_SWEEP_ANGLE,
            useCenter = false,
            topLeft = Offset(
                x = 8.dp.toPx(),
                y = 8.dp.toPx(),
            ),
            size = Size(
                width = size.width - stroke.width,
                height = size.height - stroke.width,
            ),
            style = stroke,
        )
    }

    fun DrawScope.drawPointer(
        position: Float,
        brush: Brush,
        stroke: Stroke,
    ) {
        val pointerWidth = 4.dp.toPx()
        val pointerHeight = 28.dp.toPx()

        val pointerAngle = TEMPERATURE_GAUGE_START_ANGLE -
            SWEEP_GRADIENT_ANGLE +
            TEMPERATURE_GAUGE_SWEEP_ANGLE *
            position
        val pointerRadians = pointerAngle * DEGREES_TO_RADIANS
        val trackRadius = (size.minDimension - stroke.width) / 2f

        val pointerDistance = pointerHeight + (trackRadius / 1.85f)
        val pointerCenter = Offset(
            x = center.x + pointerDistance * cos(pointerRadians),
            y = center.y + pointerDistance * sin(pointerRadians),
        )

        rotate(
            degrees = pointerAngle + 90f,
            pivot = pointerCenter,
        ) {
            drawRoundRect(
                brush = brush,
                topLeft = Offset(
                    x = pointerCenter.x - pointerWidth / 2f,
                    y = pointerCenter.y - pointerHeight / 2f,
                ),
                size = Size(pointerWidth, pointerHeight),
                cornerRadius = CornerRadius(pointerWidth / 2f),
            )
        }
    }
}

private const val TEMPERATURE_GAUGE_START_ANGLE = 135f
private const val TEMPERATURE_GAUGE_SWEEP_ANGLE = 270f
private const val SWEEP_GRADIENT_ANGLE = 90f
private const val DEGREES_TO_RADIANS = (PI / 180f).toFloat()

@Preview
@Composable
private fun TemperatureControlSectionPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        TemperatureControlSection(
            measureTemperature = MeasureTemperatureSectionPreviewData.measurement,
            range = (-30).celsius..50.celsius,
        )
    }
}

@Preview
@Composable
private fun SmallRangeTemperatureControlSectionPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        TemperatureControlSection(
            measureTemperature = MeasureTemperatureSectionPreviewData.measurement,
            range = 15.celsius..30.celsius,
        )
    }
}

@Preview
@Composable
private fun OutOfRangeTemperatureControlSectionPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        TemperatureControlSection(
            measureTemperature = MeasureTemperatureSectionPreviewData.measurement
                .copy(current = 35.celsius),
            range = 15.celsius..30.celsius,
        )
    }
}

private object MeasureTemperatureSectionPreviewData {
    val measurement = MeasureTemperature(current = 22.5.celsius)
}
