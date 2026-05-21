package network.marsys.smarthome.shared.library.design.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier

@Immutable
data class ScaffoldKeyboard(
    val scrollModifier: Modifier = Modifier,
    @Suppress("ConstructorParameterNaming", "PropertyName")
    val KeyboardSpacer: @Composable () -> Unit = {},
)

@Composable
expect fun rememberScaffoldKeyboard(): ScaffoldKeyboard

val LocalScaffoldKeyboard = compositionLocalOf<ScaffoldKeyboard> {
    error("Scaffold keyboard not provided")
}
