package network.marsys.smarthome.shared.library.design.adaptive

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalLayoutApi::class)
@Composable
actual fun rememberScaffoldKeyboard(): ScaffoldKeyboard =
    ScaffoldKeyboard(
        scrollModifier = Modifier
            .imePadding()
            .imeNestedScroll(),
    )
