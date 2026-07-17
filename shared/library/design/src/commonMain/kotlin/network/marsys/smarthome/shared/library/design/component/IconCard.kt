package network.marsys.smarthome.shared.library.design.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconCard(
    icon: ImageVector,
    colors: CardColors,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
) {
    Card(
        modifier = modifier
            .width(size)
            .aspectRatio(1f),
        colors = colors,
        shape = RoundedCornerShape(size / ICON_CARD_CORNER_DIVISOR),
        contentPadding = PaddingValues(size / ICON_CARD_PADDING_DIVISOR),
    ) {
        Icon(
            icon = icon,
            size = size / ICON_CARD_ICON_SIZE_DIVISOR,
        )
    }
}

private const val ICON_CARD_CORNER_DIVISOR = 4
private const val ICON_CARD_PADDING_DIVISOR = 4
private const val ICON_CARD_ICON_SIZE_DIVISOR = 2
