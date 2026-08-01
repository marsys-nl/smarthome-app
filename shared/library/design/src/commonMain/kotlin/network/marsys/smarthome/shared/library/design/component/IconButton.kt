@file:OptIn(ExperimentalFoundationStyleApi::class)

package network.marsys.smarthome.shared.library.design.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.then
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Reset
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens

@Composable
fun IconButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    /* Temporary solution until there is support for subcomponent styling */
    foregroundColor: Color = PaletteTokens.Base.White,
    style: Style = ButtonStyle.brand(),
) = Button(
    onClick = onClick,
    style = style,
    modifier = modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement
            .spacedBy(8.dp),
    ) {
        Icon(
            icon = icon,
            size = 16.dp,
            tint = foregroundColor,
        )

        Text(
            text = text,
            style = TextDefaults.normal then TextStyles.semiBold,
            color = foregroundColor,
        )
    }
}

@Composable
fun ErrorIconButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = IconButton(
    text = text,
    icon = icon,
    onClick = onClick,
    foregroundColor = SmartHomeTheme.colors[ColorKeyToken.TextErrorPrimary],
    style = ButtonStyle.error(),
    modifier = modifier,
)

@Preview
@Composable
private fun IconButtonPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        IconButton(
            text = "Retry",
            icon = Icons.Reset,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun ErrorIconButtonPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        ErrorIconButton(
            text = "Retry",
            icon = Icons.Reset,
            onClick = {},
        )
    }
}
