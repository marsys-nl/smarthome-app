package network.marsys.smarthome.shared.library.design.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.outline
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.icons.Check
import network.marsys.smarthome.shared.library.design.icons.HousePlug
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    shape: Shape = TextFieldDefaults.textInputShape(),
    contentPadding: PaddingValues = TextFieldDefaults.contentPadding(),
    interactionSource: MutableInteractionSource? = null,
    decorationBox: @Composable TextFieldScope.() -> Unit = {
        TextFieldDecorationBox(
            shape = shape,
            contentPadding = contentPadding,
        )
    },
) {
    var isFocused by remember { mutableStateOf(false) }

    val contentColor = colors.contentColor(enabled).value
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
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        lineLimits = TextFieldLineLimits.SingleLine,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(contentColor),
        decorator = { innerTextField ->
            val scope = TextFieldScopeImpl(
                state = state,
                colors = colors,
                textStyle = mergedTextStyle,
                enabled = enabled,
                readOnly = readOnly,
                isFocused = isFocused,
                innerTextField = innerTextField,
            )
            scope.decorationBox()
        },
    )
}

@Stable
interface TextFieldScope {
    val state: TextFieldState
    val colors: TextFieldColors
    val textStyle: TextStyle
    val enabled: Boolean
    val readOnly: Boolean
    val isFocused: Boolean

    @Composable
    fun InnerTextField()
}

private class TextFieldScopeImpl(
    override val state: TextFieldState,
    override val colors: TextFieldColors,
    override val textStyle: TextStyle,
    override val enabled: Boolean,
    override val readOnly: Boolean,
    override val isFocused: Boolean,
    private val innerTextField: @Composable () -> Unit,
) : TextFieldScope {
    @Composable
    override fun InnerTextField() = innerTextField()
}

@Composable
@Suppress("LongMethod")
fun TextFieldScope.TextFieldDecorationBox(
    modifier: Modifier = Modifier,
    shape: Shape = TextFieldDefaults.textInputShape(),
    contentPadding: PaddingValues = TextFieldDefaults.contentPadding(),
    borderWidth: Dp = 1.dp,
    outlineWidth: Dp = 1.dp,
    outlinePadding: PaddingValues = PaddingValues(1.dp),
    placeholder: @Composable (TextFieldScope.() -> Unit)? = null,
    leading: @Composable (TextFieldScope.() -> Unit)? = null,
    trailing: @Composable (TextFieldScope.() -> Unit)? = null,
    supportingText: @Composable (TextFieldScope.() -> Unit)? = null,
) {
    val backgroundColor = colors.backgroundColor(enabled).value
    val borderColor = colors.borderColor(enabled).value
    val contentColor = colors.contentColor(enabled).value

    val outlineModifier = Modifier
        .outline(outlineWidth, PaletteTokens.Base.Black, shape)
        .padding(outlinePadding)

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column(
            modifier = modifier,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .pointerHoverIcon(PointerIcon.Text)
                    .then(if (isFocused) outlineModifier else Modifier)
                    .border(borderWidth, borderColor, shape)
                    .background(backgroundColor, shape)
                    .padding(contentPadding),
            ) {
                leading?.let {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp),
                    ) {
                        it.invoke(this@TextFieldDecorationBox)
                    }
                }

                TextInput(
                    modifier = Modifier
                        .weight(1f),
                    placeholder = placeholder,
                )

                trailing?.let {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp),
                    ) {
                        it.invoke(this@TextFieldDecorationBox)
                    }
                }
            }

            supportingText?.let {
                SupportingText(
                    content = it,
                )
            }
        }
    }
}

@Composable
private fun TextFieldScope.TextInput(
    modifier: Modifier = Modifier,
    placeholder: @Composable (TextFieldScope.() -> Unit)? = null,
) {
    val placeholderColor = colors.placeholderColor(enabled).value

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart,
    ) {
        if (!readOnly && !enabled) {
            InnerTextField()
        } else {
            SelectionContainer {
                Text(state.text.toString())
            }
        }

        if (placeholder != null && state.text.isEmpty()) {
            CompositionLocalProvider(
                LocalContentColor provides placeholderColor,
                LocalTextStyle provides textStyle.copy(color = placeholderColor),
            ) {
                placeholder.invoke(this@TextInput)
            }
        }
    }
}

@Composable
private fun TextFieldScope.SupportingText(
    modifier: Modifier = Modifier,
    content: @Composable TextFieldScope.() -> Unit,
) {
    val supportingTextColor = colors.supportingTextColor(enabled).value

    CompositionLocalProvider(
        LocalContentColor provides supportingTextColor,
        LocalTextStyle provides textStyle.copy(color = supportingTextColor),
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            content.invoke(this@SupportingText)
        }
    }
}

@Immutable
@ConsistentCopyVisibility
data class TextFieldColors internal constructor(
    private val backgroundColor: Brush,
    private val contentColor: Color,
    private val placeholderColor: Color,
    private val supportingTextColor: Color,
    private val borderColor: Color,
    val focusedOutlineColor: Color,
    private val disabledBackgroundColor: Brush,
    private val disabledContentColor: Color,
    private val disabledBorderColor: Color,
    private val disabledPlaceholderColor: Color,
    private val disabledSupportingTextColor: Color,
) {
    @Composable
    internal fun backgroundColor(enabled: Boolean): State<Brush> =
        rememberUpdatedState(if (enabled) backgroundColor else disabledBackgroundColor)

    @Composable
    internal fun contentColor(enabled: Boolean): State<Color> =
        rememberUpdatedState(if (enabled) contentColor else disabledContentColor)

    @Composable
    internal fun borderColor(enabled: Boolean): State<Color> =
        rememberUpdatedState(if (enabled) borderColor else disabledBorderColor)

    @Composable
    internal fun placeholderColor(enabled: Boolean): State<Color> =
        rememberUpdatedState(if (enabled) placeholderColor else disabledPlaceholderColor)

    @Composable
    internal fun supportingTextColor(enabled: Boolean): State<Color> =
        rememberUpdatedState(if (enabled) supportingTextColor else disabledSupportingTextColor)
}

object TextFieldDefaults {
    @Composable
    fun colors(
        backgroundColor: Brush = TextFieldTokens.BackgroundColor,
        contentColor: Color = TextFieldTokens.ContentColor,
        placeholderColor: Color = TextFieldTokens.PlaceholderColor,
        supportingTextColor: Color = TextFieldTokens.SupportingTextColor,
        borderColor: Color = TextFieldTokens.BorderColor,
        focusedOutlineColor: Color = TextFieldTokens.FocusedOutlineColor,
        disabledBackgroundColor: Brush = TextFieldTokens.DisabledBackgroundColor,
        disabledContentColor: Color = TextFieldTokens.DisabledContentColor,
        disabledBorderColor: Color = borderColor,
        disabledPlaceholderColor: Color = placeholderColor,
        disabledSupportingTextColor: Color = supportingTextColor,
    ): TextFieldColors = TextFieldColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        borderColor = borderColor,
        placeholderColor = placeholderColor,
        supportingTextColor = supportingTextColor,
        focusedOutlineColor = focusedOutlineColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
        disabledBorderColor = disabledBorderColor,
        disabledPlaceholderColor = disabledPlaceholderColor,
        disabledSupportingTextColor = disabledSupportingTextColor,
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
private fun TextFieldEmptyPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(theme = theme) {
        val state = remember { TextFieldState() }
        TextField(state = state)
    }
}

@Preview
@Composable
private fun TextFieldFilledPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(theme = theme) {
        val state = remember {
            TextFieldState(
                initialText = "SmartHome",
            )
        }
        TextField(state = state)
    }
}

@Preview
@Composable
private fun TextFieldFilledDisabledPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(theme = theme) {
        val state = remember {
            TextFieldState(
                initialText = "Disabled",
            )
        }
        TextField(
            state = state,
            enabled = false,
        )
    }
}

@Preview
@Composable
private fun TextFieldReadOnlyPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(theme = theme) {
        val state = remember {
            TextFieldState(
                initialText = "Read only text",
            )
        }
        TextField(
            state = state,
            readOnly = true,
        )
    }
}

@Preview
@Composable
private fun TextFieldWithPlaceholderPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(theme = theme) {
        val state = remember { TextFieldState() }
        TextField(
            state = state,
            decorationBox = {
                TextFieldDecorationBox(
                    placeholder = { Text("Enter text…") },
                )
            },
        )
    }
}

@Preview
@Composable
private fun TextFieldWithLeadingIconPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(theme = theme) {
        val state = remember { TextFieldState() }
        TextField(
            state = state,
            decorationBox = {
                TextFieldDecorationBox(
                    leading = {
                        Image(
                            imageVector = Icons.Check,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(LocalContentColor.current),
                        )
                    },
                )
            },
        )
    }
}

@Preview
@Composable
private fun TextFieldWithTrailingIconPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(theme = theme) {
        val state = remember { TextFieldState() }
        TextField(
            state = state,
            decorationBox = {
                TextFieldDecorationBox(
                    trailing = {
                        Image(
                            imageVector = Icons.HousePlug,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(LocalContentColor.current),
                        )
                    },
                )
            },
        )
    }
}

@Preview
@Composable
@Suppress("MagicNumber")
private fun TextFieldWithHintPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(theme = theme) {
        val state = remember {
            TextFieldState(
                initialText = "SmartHome",
            )
        }

        val count = state.text.length
        val max = 120

        TextField(
            state = state,
            decorationBox = {
                TextFieldDecorationBox(
                    supportingText = {
                        Text(
                            text = "$count/$max",
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    },
                )
            },
        )
    }
}

@Preview
@Composable
private fun TextFieldWithErrorPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(theme = theme) {
        val state = remember {
            TextFieldState(
                initialText = "SmartHome",
            )
        }

        TextField(
            state = state,
            decorationBox = {
                TextFieldDecorationBox(
                    supportingText = {
                        Text(
                            text = "Please enter a valid URI (e.g., https://api.example.com)",
                            color = Color(color = 0xFFEF4444),
                        )
                    },
                )
            },
        )
    }
}

@Preview
@Composable
private fun TextFieldWithFullDecorationBoxPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(theme = theme) {
        val state = remember { TextFieldState() }
        TextField(
            state = state,
            decorationBox = {
                TextFieldDecorationBox(
                    placeholder = { Text("Enter text…") },
                    leading = { Text("@") },
                    trailing = { Text("Clear") },
                    supportingText = { Text("This is a hint below the field.") },
                )
            },
        )
    }
}
