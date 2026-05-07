package network.smarthome.shared.library.main

import androidx.compose.runtime.Composable

@Composable
internal fun WithRequireConnection(
    content: @Composable () -> Unit,
) {
    // Placeholder for connection check. In the future, this will check if the app is connected
    // to the backend and show a message if not. For now, it just shows the content.
    content.invoke()
}
