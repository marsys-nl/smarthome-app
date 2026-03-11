package network.marsys.smarthome.shared.library.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.outline
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.theme.LocalTextStyle
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.components.TextFieldTokens

@Composable
fun TextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    shape: Shape = TextFieldDefaults.textInputShape(),
    contentPadding: PaddingValues = TextFieldDefaults.contentPadding(),
    borderWidth: Dp = 1.dp,
    outlineWidth: Dp = 1.dp,
    outlinePadding: PaddingValues = PaddingValues(1.dp),
    interactionSource: MutableInteractionSource? = null,
) {
    var isFocused by remember { mutableStateOf(false) }

    val backgroundColor = colors.backgroundColor(enabled).value
    val borderColor = colors.borderColor(enabled).value
    val contentColor = colors.contentColor(enabled).value

    val outlineModifier = Modifier
        .outline(outlineWidth, PaletteTokens.Base.Black, shape)
        .padding(outlinePadding)

    val mergedTextStyle = textStyle.copy(color = contentColor)

    BasicTextField(
        state = state,
        modifier = modifier
            .semantics(mergeDescendants = true) {}
            .onFocusChanged {
                isFocused = it.hasFocus
            },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        lineLimits = TextFieldLineLimits.SingleLine,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(contentColor),
        decorator = { innerTextField ->
            Row(
                modifier = Modifier
                    .pointerHoverIcon(PointerIcon.Text)
                    .then(if (isFocused) outlineModifier else Modifier)
                    .border(borderWidth, borderColor, shape)
                    .background(backgroundColor, shape)
                    .padding(contentPadding)
            ) {
                innerTextField.invoke()
            }
        },
    )
}

@Immutable
@ConsistentCopyVisibility
data class TextFieldColors internal constructor(
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

object TextFieldDefaults {
    @Composable
    fun colors(
        backgroundColor: Brush = TextFieldTokens.BackgroundColor,
        contentColor: Color = TextFieldTokens.ContentColor,
        borderColor: Color = TextFieldTokens.BorderColor,
        disabledBackgroundColor: Brush = TextFieldTokens.DisabledBackgroundColor,
        disabledContentColor: Color = TextFieldTokens.DisabledContentColor,
        disabledBorderColor: Color = borderColor,
    ): TextFieldColors = TextFieldColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        borderColor = borderColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
        disabledBorderColor = disabledBorderColor,
    )

    @Composable
    fun contentPadding(
        horizontal: Dp = TextFieldTokens.TextInputHorizontalPadding,
        vertical: Dp = TextFieldTokens.TextInputVerticalPadding,
    ): PaddingValues = PaddingValues(
        horizontal = horizontal,
        vertical = vertical,
    )

    @Composable
    fun textInputShape() = TextFieldTokens.TextInputShape
}

@Preview
@Composable
private fun TextFieldPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        val state = remember { TextFieldState() }

        TextField(
            state = state,
        )
    }
}
