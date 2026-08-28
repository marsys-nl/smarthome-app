package network.marsys.smarthome.shared.library.design.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SplitPane(
    left: @Composable () -> Unit,
    right: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leftWeight: Float = 1f,
    rightWeight: Float = 1f,
    spacerWidth: Dp = 0.dp
) {
    Row(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .weight(leftWeight)
                .fillMaxHeight(),
            content = {
                left.invoke()
            },
        )

        if (spacerWidth > 0.dp) {
            Box(
                modifier = Modifier
                    .width(spacerWidth)
                    .fillMaxHeight(),
                content = {
                    // No-op spacer
                },
            )
        }

        Box(
            modifier = Modifier
                .weight(rightWeight)
                .fillMaxHeight(),
            content = {
                right.invoke()
            },
        )
    }
}
