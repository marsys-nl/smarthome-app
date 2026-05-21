package network.marsys.smarthome.shared.library.design.adaptive

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSValue
import platform.UIKit.CGRectValue
import platform.UIKit.UIKeyboardFrameEndUserInfoKey
import platform.UIKit.UIKeyboardWillHideNotification
import platform.UIKit.UIKeyboardWillShowNotification

@Composable
actual fun rememberScaffoldKeyboard(): ScaffoldKeyboard =
    ScaffoldKeyboard(
        KeyboardSpacer = {
            val height = rememberKeyboardHeight()
            val density = LocalDensity.current

            val safeAreaBottomInset = with(density) {
                WindowInsets.safeContent
                    .getBottom(this)
                    .toDp()
            }

            val target = with(density) {
                height.toDp()
            }

            val animatedHeight by animateDpAsState(target - safeAreaBottomInset)

            Spacer(
                modifier = Modifier
                    .height(animatedHeight),
            )
        },
    )

@Composable
@OptIn(ExperimentalForeignApi::class)
private fun rememberKeyboardHeight(): Float {
    var keyboardHeight by remember { mutableFloatStateOf(0f) }

    DisposableEffect(Unit) {
        val willDisplayKeyboard = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIKeyboardWillShowNotification,
            `object` = null,
            queue = null,
        ) { notification ->
            val keyboardFrame = notification?.userInfo?.get(UIKeyboardFrameEndUserInfoKey) as? NSValue
            keyboardFrame?.CGRectValue()?.useContents {
                keyboardHeight = size.height.toFloat()
            }
        }

        val willHideKeyboard = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIKeyboardWillHideNotification,
            `object` = null,
            queue = null,
        ) { _ ->
            keyboardHeight = 0f
        }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(willDisplayKeyboard)
            NSNotificationCenter.defaultCenter.removeObserver(willHideKeyboard)
        }
    }

    return keyboardHeight
}
