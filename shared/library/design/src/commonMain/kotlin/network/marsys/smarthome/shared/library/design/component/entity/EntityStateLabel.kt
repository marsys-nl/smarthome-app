package network.marsys.smarthome.shared.library.design.component.entity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.CircleQuestionMark
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.WifiOff
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens

@Composable
fun EntityStateLabel(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    colors: EntityStateLabelColors = EntityStateLabelDefaults.colors(),
) {
    Row(
        modifier = modifier
            .background(
                brush = colors.backgroundColor,
                shape = CircleShape,
            )
            .padding(
                horizontal = 8.dp,
                vertical = 2.dp,
            ),
        horizontalArrangement = Arrangement
            .spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides colors.contentColor,
        ) {
            Icon(
                icon = icon,
                size = 10.dp,
            )

            Text(
                text = label,
                fontSize = 10.sp,
            )
        }
    }
}

@Immutable
data class EntityStateLabelColors(
    val backgroundColor: Brush,
    val contentColor: Color,
)

object EntityStateLabelDefaults {
    @Composable
    fun colors(
        backgroundColor: Brush = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundTertiary]),
        contentColor: Color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
    ) = EntityStateLabelColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
    )

    val unavailableColors
        @Composable
        get() = colors(
            backgroundColor = SolidColor(PaletteTokens.Red.Red500.copy(alpha = .15f)),
            contentColor = PaletteTokens.Red.Red400,
        )

    val unknownColors
        @Composable
        get() = colors(
            backgroundColor = SolidColor(PaletteTokens.Slate.Slate400.copy(alpha = .2f)),
            contentColor = PaletteTokens.Slate.Slate400,
        )
}

@Preview
@Composable
private fun UnknownEntityStateLabelPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        EntityStateLabel(
            icon = Icons.CircleQuestionMark,
            label = "Unknown",
            colors = EntityStateLabelDefaults.unknownColors
        )
    }
}

@Preview
@Composable
private fun UnavailableEntityStateLabelPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        EntityStateLabel(
            icon = Icons.WifiOff,
            label = "Unavailable",
            colors = EntityStateLabelDefaults.unavailableColors
        )
    }
}
