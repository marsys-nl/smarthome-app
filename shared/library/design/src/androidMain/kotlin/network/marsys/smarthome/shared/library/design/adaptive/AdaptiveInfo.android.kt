package network.marsys.smarthome.shared.library.design.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker

@Composable
internal actual fun rememberAdaptiveInfo(): AdaptiveInfo {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    val widthDp = configuration.screenWidthDp
    val heightDp = configuration.screenHeightDp

    val posture by produceState<DevicePosture>(
        initialValue = DevicePosture.Normal,
        key1 = context,
    ) {
        WindowInfoTracker
            .getOrCreate(context)
            .windowLayoutInfo(context)
            .collect { layoutInfo ->
                value = layoutInfo.displayFeatures
                    .filterIsInstance<FoldingFeature>()
                    .firstOrNull()
                    ?.toDevicePosture() ?: DevicePosture.Normal
            }
    }

    return remember(widthDp, heightDp, posture) {
        val widthClass = when {
            widthDp < Breakpoints.MEDIUM -> WindowWidthClass.Compact
            widthDp < Breakpoints.EXPANDED -> WindowWidthClass.Medium
            else -> WindowWidthClass.Expanded
        }

        AdaptiveInfo(
            windowInfo = WindowInfo(
                widthDp = widthDp,
                heightDp = heightDp,
                widthClass = widthClass,
            ),
            posture = posture,
        )
    }
}

private fun FoldingFeature.toDevicePosture(): DevicePosture {
    if (!isSeparating) return DevicePosture.Normal

    return when (orientation) {
        FoldingFeature.Orientation.VERTICAL ->
            DevicePosture.Book(hingeWidthDp = bounds.width())

        FoldingFeature.Orientation.HORIZONTAL ->
            DevicePosture.TableTop(hingeHeightDp = bounds.height())

        else ->
            DevicePosture.Normal
    }
}
