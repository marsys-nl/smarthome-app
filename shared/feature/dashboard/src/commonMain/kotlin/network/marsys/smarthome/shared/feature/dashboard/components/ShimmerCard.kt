package network.marsys.smarthome.shared.feature.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken

@Composable
fun ShimmerCard(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        @OptIn(ExperimentalFoundationStyleApi::class)
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement
                .spacedBy(4.dp),
        ) {
            ShimmerBox(
                modifier = Modifier
                    .size(size = 48.dp)
                    .aspectRatio(1f),
            )

            ShimmerBox(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(width = 96.dp, height = 12.dp),
            )

            ShimmerBox(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(width = 64.dp, height = 10.dp),
                style = ShimmerBoxDefaults.defaultStyle(
                    backgroundColor = SmartHomeTheme.colors[ColorKeyToken.BackgroundTertiaryAlternative],
                ),
            )
        }
    }
}
