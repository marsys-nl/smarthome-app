package network.smarthome.shared.library.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun MainScreenView(
    modifier: Modifier = Modifier,
) {
    WithRequireConnection {
        MainScreenNavigation(
            modifier = modifier,
        )
    }
}
