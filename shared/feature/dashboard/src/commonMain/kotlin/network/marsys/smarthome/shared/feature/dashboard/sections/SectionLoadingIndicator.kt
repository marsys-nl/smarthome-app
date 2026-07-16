@file:OptIn(ExperimentalFoundationStyleApi::class)

package network.marsys.smarthome.shared.feature.dashboard.sections

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.style.then
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.LoaderCircle
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken

@Composable
fun SectionLoadingIndicator(
    message: String,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    // Temporary style parameters until there is proper support for styling icon tint and child components.
    foregroundColor: Color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
    fontSize: TextUnit = 14.sp,
    // End of temporary style parameters.
    style: Style = SectionLoadingIndicatorDefaults.style(
        fontSize = fontSize,
        foregroundColor = foregroundColor,
    ),
) {
    val mergedStyle = SectionLoadingIndicatorDefaults.base then style

    val styleState = remember { MutableStyleState(interactionSource) }
    val transition = rememberInfiniteTransition("loading-indicator")

    val angle = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing,
            ),
        ),
    )

    Row(
        modifier = modifier
            .styleable(styleState, mergedStyle),
        horizontalArrangement = Arrangement
            .spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            icon = Icons.LoaderCircle,
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = angle.value
                },
            size = with(LocalDensity.current) {
                fontSize.toDp() + 2.dp
            },
            tint = foregroundColor,
        )

        Text(
            text = message,
        )
    }
}

object SectionLoadingIndicatorDefaults {
    internal val base = Style {
        lineHeight(value = 20.sp)
        fontSize(value = 14.sp)
        contentColor(value = LocalContentColor.currentValue)
    }

    fun style(
        fontSize: TextUnit,
        foregroundColor: Color,
    ) = Style {
        fontSize(value = fontSize)
        contentColor(value = foregroundColor)
    }
}

@Composable
@Preview
private fun SectionLoadingIndicatorPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        SectionLoadingIndicator(
            message = "Loading your areas...",
        )
    }
}
