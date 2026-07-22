package network.marsys.smarthome.shared.library.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.then
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.ModalBottomSheetOverlayScope
import com.composeunstyled.ModalBottomSheetProperties
import com.composeunstyled.ModalBottomSheetState
import com.composeunstyled.Scrim
import com.composeunstyled.Sheet
import com.composeunstyled.SheetDetent
import com.composeunstyled.UnstyledModalBottomSheet
import com.composeunstyled.rememberModalBottomSheetState
import kotlinx.coroutines.launch
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.icons.Close
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.design.theme.tokens.components.ModalTokens

@Composable
@Suppress("ModifierMissing")
fun ModalBottomSheet(
    state: ModalBottomSheetState,
    properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
    onDismiss: () -> Unit = {},
    overlay: (@Composable ModalBottomSheetOverlayScope.() -> Unit)? = {
        Scrim(
            scrimColor = PaletteTokens.Base.Black
                .copy(alpha = 0.5f),
        )
    },
    closeButton: (@Composable (Modifier) -> Unit)? = {
        val coroutineScope = rememberCoroutineScope()

        CloseModalBottomSheetIconButton(
            onClick = {
                coroutineScope.launch {
                    state.animateTo(value = SheetDetent.Hidden)
                }
            },
            modifier = it,
        )
    },
    content: @Composable () -> Unit,
) {
    UnstyledModalBottomSheet(
        state = state,
        properties = properties,
        onDismiss = onDismiss,
        overlay = overlay,
    ) {
        Sheet(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = SmartHomeTheme.colors[ColorKeyToken.BackgroundModal],
                    shape = RoundedCornerShape(24.dp),
                )
                .border(
                    width = 1.dp,
                    color = SmartHomeTheme.colors[ColorKeyToken.BorderPrimary],
                    shape = RoundedCornerShape(24.dp),
                )
                .fillMaxWidth(),
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                ) {
                    content.invoke()
                }

                closeButton?.invoke(
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-16).dp, y = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun CloseModalBottomSheetIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val pressed by interactionSource.collectIsPressedAsState()

    @OptIn(ExperimentalFoundationStyleApi::class)
    Button(
        onClick = onClick,
        style = ButtonStyle.modalClose() then Style {
            contentPadding(8.dp)
            shape(RoundedCornerShape(8.dp))
        },
        modifier = modifier,
        interactionSource = interactionSource,
    ) {
        Icon(
            icon = Icons.Close,
            size = 16.dp,
            tint = when {
                pressed || hovered -> SmartHomeTheme.colors[ColorKeyToken.TextPrimary]
                else -> SmartHomeTheme.colors[ColorKeyToken.TextSecondary]
            },
        )
    }
}

@PreviewScreenSizes
@Composable
private fun HiddenModalBottomSheetPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        val state = rememberModalBottomSheetState(
            initialDetent = SheetDetent.Hidden,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SmartHomeTheme.colors[ColorKeyToken.BackgroundPrimary]),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                repeat(REPEAT_BACKGROUND_CONTENT) {
                    Text("Lorem ipsum dolor sit amet")
                }
            }

            ModalBottomSheet(
                state = state,
                overlay = {
                    Scrim()
                },
            ) {
                Text("Are you sure?")
            }
        }
    }
}

@PreviewScreenSizes
@Composable
private fun FullyExpandedModalBottomSheetPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeTheme(
        theme = theme,
    ) {
        val state = rememberModalBottomSheetState(
            initialDetent = SheetDetent.FullyExpanded,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SmartHomeTheme.colors[ColorKeyToken.BackgroundPrimary]),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                repeat(REPEAT_BACKGROUND_CONTENT) {
                    Text("Lorem ipsum dolor sit amet")
                }
            }

            ModalBottomSheet(
                state = state,
                overlay = {
                    Scrim()
                },
            ) {
                Text("Are you sure?")
            }
        }
    }
}

private const val REPEAT_BACKGROUND_CONTENT = 100
