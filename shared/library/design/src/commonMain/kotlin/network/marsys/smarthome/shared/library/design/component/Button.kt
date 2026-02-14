package network.marsys.smarthome.shared.library.design.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import com.composeunstyled.UnstyledButton
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.ColorSchemePreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.components.ButtonTokens

@Composable
fun Button(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.buttonShape(),
    colors: ButtonColors = ButtonDefaults.colors(),
    contentPadding: PaddingValues = ButtonDefaults.contentPadding(),
    content: @Composable RowScope.() -> Unit,
) {
    val transition = updateTransition(enabled, "button-animation")

    val backgroundColor by transition.animateColor(transitionSpec = ButtonDefaults.transition()) {
        colors.backgroundColor(it).value
    }

    val contentColor by transition.animateColor(transitionSpec = ButtonDefaults.transition()) {
        colors.contentColor(it).value
    }

    UnstyledButton(
        onClick = {},
        enabled = enabled,
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        content.invoke(this)
    }
}

@Immutable
@ConsistentCopyVisibility
data class ButtonColors internal constructor(
    private val backgroundColor: Color,
    private val contentColor: Color,
    private val disabledBackgroundColor: Color,
    private val disabledContentColor: Color,
) {
    @Composable
    internal fun backgroundColor(enabled: Boolean): State<Color> =
        rememberUpdatedState(
            if (enabled) backgroundColor else disabledBackgroundColor,
        )

    @Composable
    internal fun contentColor(enabled: Boolean): State<Color> =
        rememberUpdatedState(
            if (enabled) contentColor else disabledContentColor,
        )
}

object ButtonDefaults {
    private const val ANIMATION_DURATION_MILLIS = 300

    @Composable
    fun buttonShape(): Shape = ButtonTokens.ButtonShape

    @Composable
    fun colors(
        backgroundColor: Color = ButtonTokens.BackgroundColor,
        contentColor: Color = ButtonTokens.ContentColor,
        disabledBackgroundColor: Color = ButtonTokens.DisabledBackgroundColor,
        disabledContentColor: Color = ButtonTokens.DisabledContentColor,
    ): ButtonColors = ButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    fun contentPadding(
        horizontal: Dp = ButtonTokens.ButtonHorizontalPadding,
        vertical: Dp = ButtonTokens.ButtonVerticalPadding,
    ): PaddingValues = PaddingValues(
        horizontal = horizontal,
        vertical = vertical,
    )

    @Composable
    internal fun <T> transition(
        duration: Int = ANIMATION_DURATION_MILLIS,
    ): @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<T> = {
        tween(duration)
    }
}

@Preview
@Composable
private fun ButtonPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        Button(
            enabled = true,
        ) {
            Text("Simple button")
        }
    }
}

@Preview
@Composable
private fun DisabledButtonPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        Button(
            enabled = false,
        ) {
            Text("Simple button")
        }
    }
}
