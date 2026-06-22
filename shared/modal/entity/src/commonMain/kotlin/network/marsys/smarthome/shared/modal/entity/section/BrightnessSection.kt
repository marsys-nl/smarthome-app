package network.marsys.smarthome.shared.modal.entity.section

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEventTimeoutCancellationException
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.domain.unit.percent
import network.marsys.smarthome.shared.domain.entity.capability.Brightness
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Button
import network.marsys.smarthome.shared.library.design.component.ButtonDefaults
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.i18n.localized
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.modal.entity.EntityDetailModalAction
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.Res
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_brightness_range
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_capability_brightness
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun BrightnessSection(
    entity: EntityIdentifier,
    brightness: Brightness,
    onAction: (EntityDetailModalAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement
            .spacedBy(12.dp),
    ) {
        Text(
            text = brightness.descriptor
                .localized(),
            lineHeight = 72.sp,
            fontSize = 72.sp,
            fontWeight = FontWeight.W700,
            color = SmartHomeTheme.colors[ColorKeyToken.ForegroundBrandPrimary],
        )

        Text(
            text = stringResource(Res.string.entity_capability_brightness),
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
        )

        BrightnessControl(
            entity = entity,
            brightness = brightness,
            onAction = onAction,
        )
    }
}


@Composable
internal fun BrightnessControl(
    entity: EntityIdentifier,
    brightness: Brightness,
    onAction: (EntityDetailModalAction) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement
            .spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BrightnessAdjustButton(
            symbol = "-",
            onTap = {
                onAction(
                    EntityDetailModalAction.AdjustBrightness(
                        entity = entity,
                        brightness = (brightness.current - 1.percent)
                            .coerceIn(0.percent, 100.percent),
                    ),
                )
            },
            onHold = {
                onAction(
                    EntityDetailModalAction.AdjustBrightness(
                        entity = entity,
                        brightness = nextTenDown(brightness.current.wholePercent).percent
                            .coerceIn(0.percent, 100.percent),
                    ),
                )
            },
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(Res.string.entity_brightness_range),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
            )

            Text(
                text = "0% - 100%",
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
            )
        }

        BrightnessAdjustButton(
            symbol = "+",
            onTap = {
                onAction(
                    EntityDetailModalAction.AdjustBrightness(
                        entity = entity,
                        brightness = (brightness.current + 1.percent)
                            .coerceIn(0.percent, 100.percent),
                    ),
                )
            },
            onHold = {
                onAction(
                    EntityDetailModalAction.AdjustBrightness(
                        entity = entity,
                        brightness = nextTenUp(brightness.current.wholePercent).percent
                            .coerceIn(0.percent, 100.percent),
                    ),
                )
            },
        )
    }
}

@Composable
private fun BrightnessAdjustButton(
    symbol: String,
    onTap: () -> Unit,
    onHold: () -> Unit,
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
                        currentOnTap.invoke()
                    } else {
                        var delayInMillis = INITIAL_DELAY
                        while (true) {
                            currentOnHold.invoke()

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

private val Quantity<Dimension.Ratio>.wholePercent: Int
    get() = value.roundToInt()

private fun nextTenUp(percent: Int): Int = (percent / 10 + 1) * 10
private fun nextTenDown(percent: Int): Int = ((percent - 1) / 10) * 10

@Preview
@Composable
private fun BrightnessSectionPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        BrightnessSection(
            entity = EntityIdentifier("light.living-room"),
            brightness = BrightnessSectionPreviewData.brightness,
            onAction = {},
        )
    }
}

private object BrightnessSectionPreviewData {
    val brightness = Brightness(
        current = 55.percent,
    )
}
