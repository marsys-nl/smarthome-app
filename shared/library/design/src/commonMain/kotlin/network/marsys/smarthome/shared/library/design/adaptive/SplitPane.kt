package network.marsys.smarthome.shared.library.design.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SplitPane(
    left: @Composable () -> Unit,
    right: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leftWeight: Float = 1f,
    rightWeight: Float = 1f,
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
