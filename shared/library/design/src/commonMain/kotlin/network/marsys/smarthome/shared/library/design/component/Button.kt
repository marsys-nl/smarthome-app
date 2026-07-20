@file:OptIn(ExperimentalFoundationStyleApi::class)

package network.marsys.smarthome.shared.library.design.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.disabled
import androidx.compose.foundation.style.hovered
import androidx.compose.foundation.style.pressed
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.style.then
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.modifier.instantPressClickable
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.GradientKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.components.ButtonColorTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.components.ButtonTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.components.ErrorButtonTokens

@Composable
fun Button(
    onClick: () -> Unit,
    style: Style,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    role: Role = Role.Button,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    val styleState = rememberUpdatedStyleState(interactionSource) {
        it.isEnabled = enabled
    }

    val mergedStyle = ButtonDefaults.base then style

    val pointerModifier = if (enabled) {
        Modifier.pointerHoverIcon(PointerIcon.Default)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .instantPressClickable(
                enabled = enabled,
                interactionSource = interactionSource,
                role = role,
                onClick = onClick,
            )
            .styleable(styleState, mergedStyle)
            .then(pointerModifier),
        contentAlignment = Alignment.Center,
        content = {
            content.invoke()
        },
    )
}

object ButtonDefaults {
    internal val base = Style {
        contentPadding(
            horizontal = ButtonTokens.ButtonHorizontalPadding,
            vertical = ButtonTokens.ButtonVerticalPadding,
        )

        textAlign(value = TextAlign.Center)

        shape(value = ButtonTokens.ButtonShape)
        clip(value = true)
    }
}

object ButtonStyle {
    @Composable
    fun brand() = base(
        background = ButtonColorTokens.BackgroundColor,
        content = ButtonColorTokens.ContentColor,
        border = ButtonColorTokens.BorderColor,
        disabledBackground = ButtonColorTokens.DisabledBackgroundColor,
        disabledContent = ButtonColorTokens.DisabledContentColor,
        disabledBorder = ButtonColorTokens.BorderColor,
        pressedBackground = ButtonColorTokens.PressedBackgroundColor,
        pressedContent = ButtonColorTokens.PressedContentColor,
        pressedBorder = ButtonColorTokens.BorderColor,
    )

    @Composable
    fun error() = base(
        background = ErrorButtonTokens.BackgroundColor,
        content = ErrorButtonTokens.ContentColor,
        border = ErrorButtonTokens.BorderColor,
        disabledBackground = ErrorButtonTokens.DisabledBackgroundColor,
        disabledContent = ErrorButtonTokens.DisabledContentColor,
        disabledBorder = ErrorButtonTokens.BorderColor,
        pressedBackground = ErrorButtonTokens.PressedBackgroundColor,
        pressedContent = ErrorButtonTokens.PressedContentColor,
        pressedBorder = ErrorButtonTokens.BorderColor,
    )

    @Composable
    fun outlined() = base(
        background = SolidColor(Color.Unspecified),
        content = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
        border = SmartHomeTheme.colors[ColorKeyToken.BorderPrimary],
        disabledBackground = ButtonColorTokens.DisabledBackgroundColor,
        disabledContent = ButtonColorTokens.DisabledContentColor,
        disabledBorder = SmartHomeTheme.colors[ColorKeyToken.BorderPrimary],
        pressedBackground = ButtonColorTokens.PressedBackgroundColor,
        pressedContent = ButtonColorTokens.PressedContentColor,
        pressedBorder = SmartHomeTheme.colors[ColorKeyToken.BorderPrimary],
    ) then {
        borderWidth(1.dp)
    }

    @Composable
    fun onBrand() = base(
        background = SolidColor(value = PaletteTokens.Slate.Slate800),
        content = PaletteTokens.Base.White,
        border = ButtonColorTokens.BorderColor,
        disabledBackground = ButtonColorTokens.DisabledBackgroundColor,
        disabledContent = ButtonColorTokens.DisabledContentColor,
        disabledBorder = ButtonColorTokens.BorderColor,
        pressedBackground = SolidColor(value = PaletteTokens.Slate.Slate800),
        pressedContent = PaletteTokens.Base.White,
        pressedBorder = ButtonColorTokens.BorderColor,
    )

    @Composable
    fun text() = base(
        background = SolidColor(Color.Unspecified),
        content = LocalContentColor.current,
        border = ButtonColorTokens.BorderColor,
        disabledBackground = ButtonColorTokens.DisabledBackgroundColor,
        disabledContent = ButtonColorTokens.DisabledContentColor,
        disabledBorder = ButtonColorTokens.BorderColor,
        pressedBackground = ButtonColorTokens.PressedBackgroundColor,
        pressedContent = LocalContentColor.current,
        pressedBorder = ButtonColorTokens.BorderColor,
    )

    @Composable
    fun primary() = base(
        background = SmartHomeTheme.colors[GradientKeyToken.BrandPrimaryToSecondary],
        content = ButtonColorTokens.ContentColor,
        border = ButtonColorTokens.BorderColor,
        disabledBackground = ButtonColorTokens.DisabledBackgroundColor,
        disabledContent = ButtonColorTokens.DisabledContentColor,
        disabledBorder = ButtonColorTokens.BorderColor,
        pressedBackground = ButtonColorTokens.PressedBackgroundColor,
        pressedContent = ButtonColorTokens.ContentColor,
        pressedBorder = ButtonColorTokens.BorderColor,
    )

    @Composable
    fun secondary() = base(
        background = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundSecondary]),
        content = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
        border = SmartHomeTheme.colors[ColorKeyToken.BorderPrimary],
        disabledBackground = ButtonColorTokens.DisabledBackgroundColor,
        disabledContent = ButtonColorTokens.DisabledContentColor,
        disabledBorder = ButtonColorTokens.BorderColor,
        pressedBackground = ButtonColorTokens.PressedBackgroundColor,
        pressedContent = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
        pressedBorder = SmartHomeTheme.colors[ColorKeyToken.BorderPrimary],
    )

    internal fun base(
        background: Brush,
        content: Color,
        border: Color,
        disabledBackground: Brush,
        disabledContent: Color,
        disabledBorder: Color,
        pressedBackground: Brush,
        pressedContent: Color,
        pressedBorder: Color,
        hoveredBackground: Brush = pressedBackground,
        hoveredContent: Color = pressedContent,
        hoveredBorder: Color = pressedBorder,
    ) = Style {
        background(value = background)
        contentColor(value = content)
        borderColor(value = border)

        disabled {
            animate {
                background(value = disabledBackground)
                contentColor(value = disabledContent)
                borderColor(value = disabledBorder)
            }
        }

        hovered {
            animate {
                background(value = hoveredBackground)
                contentColor(value = hoveredContent)
                borderColor(value = hoveredBorder)
            }
        }

        pressed {
            animate {
                background(value = pressedBackground)
                contentColor(value = pressedContent)
                borderColor(value = pressedBorder)
            }
        }
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
            style = ButtonStyle.primary(),
            enabled = true,
        ) {
            Text("Button")
        }
    }
}

@Preview
@Composable
private fun SecondaryButtonPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        Button(
            onClick = {},
            style = ButtonStyle.secondary(),
            enabled = true,
        ) {
            Text("Secondary button")
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
            style = ButtonStyle.primary(),
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
            style = ButtonStyle.outlined(),
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
            style = ButtonStyle.outlined(),
        ) {
            Text("Bordered disabled button")
        }
    }
}

@Preview
@Composable
private fun ErrorButtonPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        Button(
            onClick = {},
            style = ButtonStyle.error(),
        ) {
            Text("Error button")
        }
    }
}
