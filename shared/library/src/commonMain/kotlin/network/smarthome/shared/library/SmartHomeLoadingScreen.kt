package network.smarthome.shared.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import network.marsys.smarthome.shared.library.resources.SmartHomeRes
import network.marsys.smarthome.shared.library.resources.logo
import org.jetbrains.compose.resources.painterResource

private val BackgroundColor = Color(color = 0xFFF1BF42)

@Composable
fun SmartHomeLoadingScreen(
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .background(BackgroundColor)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(SmartHomeRes.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .sizeIn(maxHeight = maxHeight / 6)
                .aspectRatio(1f),
        )
    }
}
