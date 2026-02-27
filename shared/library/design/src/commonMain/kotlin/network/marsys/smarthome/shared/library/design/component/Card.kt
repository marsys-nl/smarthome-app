package network.marsys.smarthome.shared.library.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.ColorSchemePreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.components.CardTokens

@Composable
fun Card(
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.colors(),
    shape: Shape = CardDefaults.shape(),
    contentPadding: PaddingValues = CardDefaults.contentPadding(),
    content: @Composable () -> Unit,
) {
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
                brush = colors.backgroundColor,
                shape = shape,
            )
            .padding(contentPadding)
    ) {
        content.invoke()
    }
}

@Immutable
@ConsistentCopyVisibility
data class CardColors internal constructor(
    internal val backgroundColor: Brush,
    internal val contentColor: Color,
    internal val borderColor: Color,
)

object CardDefaults {
    @Composable
    fun shape(): Shape = CardTokens.CardShape

    @Composable
    fun colors(
        backgroundColor: Brush = CardTokens.BackgroundColor,
        contentColor: Color = CardTokens.ContentColor,
        borderColor: Color = CardTokens.BorderColor,
    ): CardColors = CardColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        borderColor = borderColor,
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

@Preview
@Composable
private fun CardPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        Card {
            Box(
                modifier = Modifier
                    .size(size = 100.dp),
            )
        }
    }
}
