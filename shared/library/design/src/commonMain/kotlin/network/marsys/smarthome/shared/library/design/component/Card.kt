package network.marsys.smarthome.shared.library.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.components.CardTokens
import com.composeunstyled.LocalContentColor as UnstyledLocalContentColor

@Composable
fun Card(
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.colors(),
    shape: Shape = CardDefaults.shape(),
    contentPadding: PaddingValues = CardDefaults.contentPadding(),
    border: Border = Border.None,
    contentAlignment: Alignment = Alignment.TopStart,
    interactionSource: InteractionSource = remember { MutableInteractionSource() },
    content: @Composable BoxScope.() -> Unit,
) {
    val pressed by interactionSource.collectIsPressedAsState()
    val hovered by interactionSource.collectIsHoveredAsState()

    val backgroundColor = colors.backgroundColor(pressed = pressed, hovered = hovered).value
    val contentColor = colors.contentColor(pressed = pressed, hovered = hovered).value
    val borderColor = colors.borderColor(pressed = pressed, hovered = hovered).value

    val borderModifier = determineBorderModifier(
        border = border,
        shape = shape,
        color = borderColor,
    )

    Box(
        modifier = modifier
            .dropShadow(
                shape = shape,
                shadow = Shadow(
                    radius = 2.dp,
                    spread = 0.dp,
                    color = PaletteTokens.Base.Black
                        .copy(alpha = 0.05f),
                    offset = DpOffset(x = 0.dp, y = 1.dp),
                ),
            )
            .background(
                brush = backgroundColor,
                shape = shape,
            )
            .then(borderModifier)
            .padding(contentPadding),
        contentAlignment = contentAlignment,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            UnstyledLocalContentColor provides contentColor,
        ) {
            content.invoke(this)
        }
    }
}

@Composable
private fun determineBorderModifier(
    border: Border,
    shape: Shape,
    color: Color,
): Modifier {
    val density = LocalDensity.current

    return when (border) {
        is Border.Solid if color.isSpecified && border.width > 0.dp ->
            Modifier.border(
                width = border.width,
                color = color,
                shape = shape,
            )

        is Border.Dashed if color.isSpecified && border.width > 0.dp ->
            Modifier.drawBehind {
                val stroke = Stroke(
                    width = border.width.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(10f, 10f),
                        0f,
                    ),
                )

                val outline = shape.createOutline(
                    size = size,
                    layoutDirection = layoutDirection,
                    density = density,
                )

                drawOutline(
                    outline = outline,
                    color = color,
                    style = stroke,
                )
            }

        else -> Modifier
    }
}

@Immutable
@ConsistentCopyVisibility
data class CardColors internal constructor(
    private val backgroundColor: Brush,
    private val contentColor: Color,
    private val borderColor: Color,
    private val pressedBackgroundColor: Brush,
    private val pressedContentColor: Color,
    private val pressedBorderColor: Color,
    private val hoveredBackgroundColor: Brush,
    private val hoveredContentColor: Color,
    private val hoveredBorderColor: Color,
) {
    @Composable
    internal fun backgroundColor(pressed: Boolean, hovered: Boolean): State<Brush> =
        rememberUpdatedState(
            when {
                pressed -> pressedBackgroundColor
                hovered -> hoveredBackgroundColor
                else -> backgroundColor
            },
        )

    @Composable
    internal fun contentColor(pressed: Boolean, hovered: Boolean): State<Color> =
        rememberUpdatedState(
            when {
                pressed -> pressedContentColor
                hovered -> hoveredContentColor
                else -> contentColor
            },
        )

    @Composable
    internal fun borderColor(pressed: Boolean, hovered: Boolean): State<Color> =
        rememberUpdatedState(
            when {
                pressed -> pressedBorderColor
                hovered -> hoveredBorderColor
                else -> borderColor
            },
        )
}

object CardDefaults {
    @Composable
    fun shape(): Shape = CardTokens.CardShape

    @Composable
    fun colors(
        backgroundColor: Brush = CardTokens.BackgroundColor,
        contentColor: Color = CardTokens.ContentColor,
        borderColor: Color = CardTokens.BorderColor,
        pressedBackgroundColor: Brush = backgroundColor,
        pressedContentColor: Color = contentColor,
        pressedBorderColor: Color = borderColor,
        hoveredBackgroundColor: Brush = backgroundColor,
        hoveredContentColor: Color = contentColor,
        hoveredBorderColor: Color = borderColor,
    ): CardColors = CardColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        borderColor = borderColor,
        pressedBackgroundColor = pressedBackgroundColor,
        pressedContentColor = pressedContentColor,
        pressedBorderColor = pressedBorderColor,
        hoveredBackgroundColor = hoveredBackgroundColor,
        hoveredContentColor = hoveredContentColor,
        hoveredBorderColor = hoveredBorderColor,
    )

    @Composable
    fun contentPadding(
        horizontal: Dp = CardTokens.CardHorizontalPadding,
        vertical: Dp = CardTokens.CardVerticalPadding,
    ): PaddingValues = PaddingValues(
        horizontal = horizontal,
        vertical = vertical,
    )
}

sealed interface Border {
    data object None : Border

    data class Solid(val width: Dp) : Border
    data class Dashed(val width: Dp) : Border
}

@Preview
@Composable
private fun CardPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        Card {
            Box(
                modifier = Modifier
                    .size(size = 100.dp),
            )
        }
    }
}

@Preview
@Composable
private fun BorderedCardPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        Card(
            colors = CardDefaults.colors(
                borderColor = LocalColorScheme.current[ColorKeyToken.ForegroundBrandPrimary],
            ),
            border = Border.Solid(1.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(size = 100.dp),
            )
        }
    }
}

@Preview
@Composable
private fun DashedBorderedCardPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        Card(
            colors = CardDefaults.colors(
                borderColor = LocalColorScheme.current[ColorKeyToken.ForegroundBrandPrimary],
            ),
            border = Border.Dashed(1.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(size = 100.dp),
            )
        }
    }
}
