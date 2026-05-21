package network.marsys.smarthome.shared.library.design.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation
import platform.UIKit.UIDeviceOrientationDidChangeNotification
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UIScreen
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal actual fun rememberAdaptiveInfo(): AdaptiveInfo {
    val rotationFlow = remember { MutableStateFlow(0) }
    var windowSize by remember { mutableStateOf(readWindowSize()) }

    DisposableEffect(Unit) {
        val observer = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIDeviceOrientationDidChangeNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue,
        ) {
            val orientation = UIDevice.currentDevice.orientation
            val isValid = orientation == UIDeviceOrientation.UIDeviceOrientationPortrait ||
                orientation == UIDeviceOrientation.UIDeviceOrientationPortraitUpsideDown ||
                orientation == UIDeviceOrientation.UIDeviceOrientationLandscapeLeft ||
                orientation == UIDeviceOrientation.UIDeviceOrientationLandscapeRight

            if (isValid) rotationFlow.value++
        }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        rotationFlow.collectLatest { tick ->
            if (tick == 0) {
                windowSize = readWindowSize()
                return@collectLatest
            }

            delay(350.milliseconds)
            windowSize = readWindowSize()
        }
    }

    val (widthPoints, heightPoints) = windowSize
    val widthClass = remember(widthPoints) {
        when {
            widthPoints < Breakpoints.MEDIUM -> WindowWidthClass.Compact
            widthPoints < Breakpoints.EXPANDED -> WindowWidthClass.Medium
            else -> WindowWidthClass.Expanded
        }
    }

    return remember(widthClass, heightPoints) {
        AdaptiveInfo(
            windowInfo = WindowInfo(
                widthDp = widthPoints,
                heightDp = heightPoints,
                widthClass = widthClass,
            ),
            // iOS doesn't have foldable devices, so we can assume Normal posture
            posture = DevicePosture.Normal,
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun readWindowSize(): Pair<Int, Int> {
    val activeScene = UIApplication.sharedApplication
        .connectedScenes
        .filterIsInstance<UIWindowScene>()
        .firstOrNull { it.activationState == UISceneActivationStateForegroundActive }

    val bounds = activeScene?.let {
        it.windows
            .filterIsInstance<UIWindow>()
            .firstOrNull { it.isKeyWindow() }
            ?.bounds
    } ?: UIScreen.mainScreen.bounds

    return bounds.useContents {
        size.width.toInt() to size.height.toInt()
    }
}
