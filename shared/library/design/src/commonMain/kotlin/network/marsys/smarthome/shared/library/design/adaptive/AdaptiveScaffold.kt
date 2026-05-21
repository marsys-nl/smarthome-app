package network.marsys.smarthome.shared.library.design.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
@Suppress("ComposableParamOrder")
fun AdaptiveScaffold(
    modifier: Modifier = Modifier,
    panePolicy: PanePolicy = PanePolicy.AllowSplit(),
    singlePane: @Composable () -> Unit,
    splitPane: @Composable () -> Unit,
) {
    val adaptiveInfo = rememberAdaptiveInfo()
    val layoutMode by remember(adaptiveInfo.windowInfo.widthClass) {
        derivedStateOf {
            adaptiveInfo.layoutMode(policy = panePolicy)
        }
    }

    val safeDrawingExcludingIme = WindowInsets.safeDrawing
        .exclude(WindowInsets.ime)

    val safeAreaModifier = remember(safeDrawingExcludingIme) {
        modifier
            .fillMaxSize()
            .windowInsetsPadding(safeDrawingExcludingIme)
    }

    val scaffoldKeyboard = rememberScaffoldKeyboard()
    CompositionLocalProvider(
        LocalScaffoldKeyboard provides scaffoldKeyboard,
    ) {
        when (layoutMode) {
            LayoutMode.SinglePane ->
                Box(
                    modifier = safeAreaModifier,
                    content = {
                        singlePane.invoke()
                    },
                )

            LayoutMode.SplitPane ->
                Box(
                    modifier = safeAreaModifier,
                    content = {
                        splitPane.invoke()
                    },
                )
        }
    }
}
