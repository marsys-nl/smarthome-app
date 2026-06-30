package network.marsys.smarthome.shared.modal.entity.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEventTimeoutCancellationException
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.domain.unit.percent
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.Res
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_quantity_range
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun <T : Dimension> QuantityControl(
    quantity: Quantity<T>,
    range: ClosedRange<Quantity<T>>,
    onQuantityChange: (Quantity<T>) -> Unit,
    modifier: Modifier = Modifier,
    strategy: QuantityControlStrategy<T> =
        defaultQuantityControlStrategy(range = range),
) {
    Row(
        modifier = modifier
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement
            .spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AdjustQuantityButton(
            symbol = "-",
            onAdjust = onQuantityChange,
            onTap = { strategy.onTap(quantity, QuantityControlStrategy.Direction.Decrease) },
            onHold = { strategy.onHold(quantity, QuantityControlStrategy.Direction.Decrease) },
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(Res.string.entity_quantity_range),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
            )

            Text(
                text = "${range.start} - ${range.endInclusive}",
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
            )
        }

        AdjustQuantityButton(
            symbol = "+",
            onAdjust = onQuantityChange,
            onTap = { strategy.onTap(quantity, QuantityControlStrategy.Direction.Increase) },
            onHold = { strategy.onHold(quantity, QuantityControlStrategy.Direction.Increase) },
        )
    }
}

@Composable
private fun <T : Dimension> AdjustQuantityButton(
    symbol: String,
    onAdjust: (Quantity<T>) -> Unit,
    onTap: () -> Quantity<T>,
    onHold: () -> Quantity<T>,
) {
    val currentOnTap by rememberUpdatedState(onTap)
    val currentOnHold by rememberUpdatedState(onHold)

    Box(
        modifier = Modifier
            .width(64.dp)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(SmartHomeTheme.colors[ColorKeyToken.BackgroundTertiary])
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown()

                    if (waitForUp(delay = INITIAL_DELAY)) {
                        onAdjust.invoke(currentOnTap.invoke())
                    } else {
                        var delayInMillis = INITIAL_DELAY
                        while (true) {
                            onAdjust.invoke(currentOnHold.invoke())

                            if (waitForUp(delay = delayInMillis)) {
                                break
                            }

                            delayInMillis = (delayInMillis * DECAY_FACTOR)
                                .toLong()
                                .coerceAtLeast(MIN_DELAY)
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(text = symbol)
    }
}

private suspend fun AwaitPointerEventScope.waitForUp(delay: Long) = try {
    withTimeout(delay) {
        waitForUpOrCancellation()
    } != null
} catch (_: PointerEventTimeoutCancellationException) {
    false
}

private const val INITIAL_DELAY = 400L
private const val MIN_DELAY = 100L
private const val DECAY_FACTOR = .85

interface QuantityControlStrategy<D : Dimension> {
    fun onTap(current: Quantity<D>, direction: Direction): Quantity<D>
    fun onHold(current: Quantity<D>, direction: Direction): Quantity<D>

    sealed class Direction(internal val sign: Int) {
        data object Increase : Direction(sign = 1)
        data object Decrease : Direction(sign = -1)
    }
}

fun <T : Dimension> snappingQuantityControlStrategy(
    step: Double,
    snap: Double,
    range: ClosedRange<Quantity<T>>,
): QuantityControlStrategy<T> = object : QuantityControlStrategy<T> {
    override fun onTap(
        current: Quantity<T>,
        direction: QuantityControlStrategy.Direction,
    ): Quantity<T> = (current + (step * direction.sign))
        .coerceIn(range)

    override fun onHold(
        current: Quantity<T>,
        direction: QuantityControlStrategy.Direction,
    ): Quantity<T> {
        val multiple = current.value / snap
        val target = when (direction) {
            is QuantityControlStrategy.Direction.Increase ->
                (floor(multiple) + 1) * snap

            is QuantityControlStrategy.Direction.Decrease ->
                (ceil(multiple) - 1) * snap
        }

        return (current + (target - current.value))
            .coerceIn(range)
    }
}

fun <T : Dimension> defaultQuantityControlStrategy(
    range: ClosedRange<Quantity<T>>,
) = snappingQuantityControlStrategy(
    step = 1.0,
    snap = 10.0,
    range = range,
)

fun <T : Dimension> temperatureQuantityControlStrategy(
    range: ClosedRange<Quantity<T>>,
) = snappingQuantityControlStrategy<T>(
    step = 0.5,
    snap = 1.0,
    range = range,
)

@Preview
@Composable
private fun BrightnessQuantityControlPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        QuantityControl(
            quantity = 55.percent,
            range = 0.percent..100.percent,
            onQuantityChange = {},
        )
    }
}

@Preview
@Composable
private fun TemperatureQuantityControlPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        QuantityControl(
            quantity = 22.5.celsius,
            range = 16.celsius..50.celsius,
            onQuantityChange = {},
        )
    }
}
