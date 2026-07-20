package network.marsys.smarthome.shared.library.design.modifier

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics

@Composable
fun Modifier.instantPressClickable(
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    role: Role = Role.Button,
    onClickLabel: String? = null,
    onClick: () -> Unit,
) = this
    .instantPressClickableSemantics(
        enabled = enabled,
        role = role,
        onClickLabel = onClickLabel,
        onClick = onClick,
    )
    .instantPressClickablePointerInput(
        enabled = enabled,
        interactionSource = interactionSource,
        onClick = onClick,
    )
    .instantPressClickableKeyEvents(
        enabled = enabled,
        onClick = onClick,
    )

private fun Modifier.instantPressClickableSemantics(
    enabled: Boolean,
    role: Role,
    onClickLabel: String?,
    onClick: () -> Unit,
) = semantics(mergeDescendants = true) {
    if (enabled) {
        this.role = role

        this.onClick(label = onClickLabel) {
            onClick.invoke()
            true
        }
    }
}

private fun Modifier.instantPressClickablePointerInput(
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
) = this
    .focusable(
        enabled = enabled,
        interactionSource = interactionSource,
    )
    .hoverable(
        enabled = enabled,
        interactionSource = interactionSource,
    )
    .pointerInput(enabled, interactionSource) {
        if (!enabled) {
            return@pointerInput
        }

        awaitEachGesture {
            val down = awaitFirstDown(requireUnconsumed = false)
            val press = PressInteraction.Press(down.position)
            interactionSource.tryEmit(press)

            @Suppress("VariableMinLength")
            val up = waitForUpOrCancellation()
            if (up != null) {
                interactionSource.tryEmit(PressInteraction.Release(press))
                onClick.invoke()
            } else {
                interactionSource.tryEmit(PressInteraction.Cancel(press))
            }
        }
    }

private fun Modifier.instantPressClickableKeyEvents(
    enabled: Boolean,
    onClick: () -> Unit,
) = onKeyEvent {
    if (!enabled) {
        return@onKeyEvent false
    }

    if (it.type == KeyEventType.KeyUp && it.key in listOf(Key.Enter, Key.NumPadEnter, Key.Spacebar)) {
        onClick.invoke()
        true
    } else {
        false
    }
}
