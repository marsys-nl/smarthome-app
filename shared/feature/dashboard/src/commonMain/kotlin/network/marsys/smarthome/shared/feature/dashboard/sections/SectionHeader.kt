package network.marsys.smarthome.shared.feature.dashboard.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.ChevronRight
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    right: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            lineHeight = 28.sp,
            fontSize = 18.sp,
            fontWeight = FontWeight.W600,
        )

        right?.invoke(this)
    }
}

@Preview
@Composable
private fun ScenesSectionHeaderPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        SectionHeader(
            title = "Scenes",
        )
    }
}

@Preview
@Composable
private fun RoomsSectionHeaderPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        SectionHeader(
            title = "Rooms",
        ) {
            Row(
                horizontalArrangement = Arrangement
                    .spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
                ) {
                    Text(
                        text = "See all",
                        lineHeight = 20.sp,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                    )

                    Icon(
                        icon = Icons.ChevronRight,
                        size = 16.dp,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun QuickControlUngroupedSectionHeaderPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        SectionHeader(
            title = "Quick control",
        ) {
            GroupEntitiesButton(
                groupByType = false,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun QuickControlGroupedSectionHeaderPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        SectionHeader(
            title = "Quick control",
        ) {
            GroupEntitiesButton(
                groupByType = true,
                onClick = {},
            )
        }
    }
}
