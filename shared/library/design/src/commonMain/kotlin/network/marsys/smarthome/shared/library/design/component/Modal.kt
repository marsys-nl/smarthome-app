package network.marsys.smarthome.shared.library.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.SmartHomeModalPreview
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.components.ModalTokens

@Immutable
data class ModalProperties(
    internal val dismissOnClickOutside: Boolean = true,
)

@Composable
fun Modal(
    onDismissRequest: () -> Unit,
    properties: ModalProperties = ModalProperties(),
    colors: ModalColors = ModalDefaults.colors(),
    content: @Composable () -> Unit,
) {
    ModalOverlay(
        onDismissRequest = onDismissRequest,
        properties = properties,
        colors = colors,
    ) {
        ModalContent(
            colors = colors,
            content = content,
        )
    }
}

@Composable
fun ModalOverlay(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    properties: ModalProperties = ModalProperties(),
    colors: ModalColors = ModalDefaults.colors(),
    contentPadding: PaddingValues = ModalDefaults.overlayPadding(),
    content: @Composable () -> Unit,
) {
    val dismissOnClickOutsideModifier = if (properties.dismissOnClickOutside) {
        Modifier
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onDismissRequest,
            )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(dismissOnClickOutsideModifier)
            .background(
                brush = colors.overlayColor,
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
        content = { content.invoke() },
    )
}

@Composable
fun ModalContent(
    modifier: Modifier = Modifier,
    colors: ModalColors = ModalDefaults.colors(),
    contentPadding: PaddingValues = ModalDefaults.modalPadding(),
    modalShape: Shape = ModalDefaults.modalShape(),
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .background(
                brush = colors.containerColor,
                shape = modalShape,
            )
            .padding(contentPadding)
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = { /* Consume clicks to prevent dismissing the modal when clicking inside the content */ },
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content.invoke()
    }
}

@Immutable
@ConsistentCopyVisibility
data class ModalColors internal constructor(
    val overlayColor: Brush,
    val containerColor: Brush,
    val contentColor: Color,
    val borderColor: Color,
)

object ModalDefaults {
    @Composable
    fun colors(
        overlayColor: Brush = ModalTokens.OverlayColor,
        containerColor: Brush = ModalTokens.ContainerColor,
        contentColor: Color = ModalTokens.ContentColor,
        borderColor: Color = ModalTokens.BorderColor,
    ) = ModalColors(
        overlayColor = overlayColor,
        containerColor = containerColor,
        contentColor = contentColor,
        borderColor = borderColor,
    )

    @Composable
    fun modalPadding(): PaddingValues = PaddingValues(
        horizontal = ModalTokens.ModalHorizontalPadding,
        vertical = ModalTokens.ModalVerticalPadding,
    )

    @Composable
    fun modalShape(): Shape = ModalTokens.ModalShape

    @Composable
    fun overlayPadding(): PaddingValues = PaddingValues(
        horizontal = ModalTokens.OverlayHorizontalPadding,
        vertical = ModalTokens.OverlayVerticalPadding,
    )
}

@PreviewScreenSizes
@Composable
private fun ModalPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeModalPreview(
        theme = theme,
    ) {
        Text(text = "This is a modal")
    }
}
