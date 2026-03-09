package network.marsys.smarthome.shared.library.design.component

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledButton
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.components.ButtonTokens

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.buttonShape(),
    colors: ButtonColors = ButtonDefaults.colors(),
    contentPadding: PaddingValues = ButtonDefaults.contentPadding(),
    borderWidth: Dp = 0.dp,
    indication: Indication? = LocalIndication.current,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val background = colors.backgroundColor(enabled).value

    val backgroundColor = when (background) {
        is SolidColor -> background.value
        else -> Color.Unspecified
    }

    val contentColor = colors.contentColor(enabled).value
    val borderColor = colors.borderColor(enabled).value

    val gradientModifier = when (background) {
        is ShaderBrush -> Modifier.background(
            brush = background,
            shape = shape,
        )

        else -> Modifier
    }

    UnstyledButton(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        contentPadding = contentPadding,
        borderColor = borderColor,
        borderWidth = borderWidth,
        indication = indication,
        interactionSource = interactionSource,
        modifier = modifier
            .then(gradientModifier),
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
        ) {
            content.invoke(this)
        }
    }
}

@Immutable
@ConsistentCopyVisibility
data class ButtonColors internal constructor(
    private val backgroundColor: Brush,
    private val contentColor: Color,
    private val borderColor: Color,
    private val disabledBackgroundColor: Brush,
    private val disabledContentColor: Color,
    private val disabledBorderColor: Color,
) {
    @Composable
    internal fun backgroundColor(enabled: Boolean): State<Brush> =
        rememberUpdatedState(
            if (enabled) backgroundColor else disabledBackgroundColor,
        )

    @Composable
    internal fun contentColor(enabled: Boolean): State<Color> =
        rememberUpdatedState(
            if (enabled) contentColor else disabledContentColor,
        )

    @Composable
    internal fun borderColor(enabled: Boolean): State<Color> =
        rememberUpdatedState(
            if (enabled) borderColor else disabledBorderColor,
        )
}

object ButtonDefaults {
    private const val ANIMATION_DURATION_MILLIS = 300

    @Composable
    fun buttonShape(): Shape = ButtonTokens.ButtonShape

    @Composable
    fun colors(
        backgroundColor: Brush = ButtonTokens.BackgroundColor,
        contentColor: Color = ButtonTokens.ContentColor,
        borderColor: Color = ButtonTokens.BorderColor,
        disabledBackgroundColor: Brush = ButtonTokens.DisabledBackgroundColor,
        disabledContentColor: Color = ButtonTokens.DisabledContentColor,
        disabledBorderColor: Color = borderColor,
    ): ButtonColors = ButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        borderColor = borderColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
        disabledBorderColor = disabledBorderColor,
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
    @Suppress("unused")
    internal fun <T> transition(
        duration: Int = ANIMATION_DURATION_MILLIS,
    ): @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<T> = {
        tween(duration)
    }
}

@Preview
@Composable
private fun ButtonPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        Button(
            onClick = {},
            enabled = true,
        ) {
            Text("Button")
        }
    }
}

@Preview
@Composable
private fun DisabledButtonPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        Button(
            onClick = {},
            enabled = false,
        ) {
            Text("Disabled button")
        }
    }
}

@Preview
@Composable
private fun BorderedButtonPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        Button(
            onClick = {},
            colors = ButtonDefaults.colors(
                backgroundColor = SolidColor(Color.Unspecified),
                borderColor = LocalColorScheme.current[ColorKeyToken.BorderPrimary],
            ),
            borderWidth = 1.dp,
        ) {
            Text("Bordered button")
        }
    }
}

@Preview
@Composable
private fun BorderedDisabledButtonPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        Button(
            onClick = {},
            enabled = false,
            colors = ButtonDefaults.colors(
                backgroundColor = SolidColor(Color.Unspecified),
                borderColor = LocalColorScheme.current[ColorKeyToken.BorderPrimary],
            ),
            borderWidth = 1.dp,
        ) {
            Text("Bordered disabled button")
        }
    }
}
