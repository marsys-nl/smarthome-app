package network.marsys.smarthome.shared.library.design.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.LocalContentColor

@Composable
fun Icon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = IconDefaults.size(),
    tint: Color = LocalContentColor.current,
) {
    Image(
        imageVector = icon,
        contentDescription = null,
        modifier = modifier
            .size(size = size),
        colorFilter = ColorFilter.tint(
            color = tint,
        ),
    )
}

object IconDefaults {
    @Composable
    fun size() = 32.dp
}
