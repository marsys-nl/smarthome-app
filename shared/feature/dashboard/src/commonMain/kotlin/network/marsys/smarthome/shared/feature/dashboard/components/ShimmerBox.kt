@file:OptIn(ExperimentalFoundationStyleApi::class)

package network.marsys.smarthome.shared.feature.dashboard.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.StyleScope
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.style.then
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    style: Style = ShimmerBoxDefaults.defaultStyle(),
) {
    val mergedStyle = ShimmerBoxDefaults.base then style
    val transition = rememberInfiniteTransition("shimmering-box")

    val alpha = transition.animateFloat(
        initialValue = 1f,
        targetValue = .5f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = CubicBezierEasing(.4f, 0f, .6f, 1f),
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "alpha",
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                this.alpha = alpha.value
            }
            .styleable(null, mergedStyle),
    )
}

object ShimmerBoxDefaults {
    internal val base = Style {
        shape(RoundedCornerShape(12.dp))
    }

    @Composable
    fun defaultStyle(
        backgroundColor: Color = SmartHomeTheme.colors[ColorKeyToken.BackgroundTertiary],
        block: StyleScope.() -> Unit = {},
    ) = Style {
        background(color = backgroundColor)
        block.invoke(this)
    }
}

@Composable
@Preview
private fun ShimmerBoxPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        ShimmerBox(
            modifier = Modifier
                .size(width = 96.dp, height = 12.dp),
        )
    }
}

@Composable
@Preview
private fun StyledShimmerBoxPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        ShimmerBox(
            style = ShimmerBoxDefaults.defaultStyle {
                size(width = 96.dp, height = 12.dp)
            },
        )
    }
}
