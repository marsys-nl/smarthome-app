package network.marsys.smarthome.shared.feature.dashboard.sections

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.then
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.feature.dashboard.DashboardScreenAction
import network.marsys.smarthome.shared.feature.dashboard.DashboardScreenState
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Button
import network.marsys.smarthome.shared.library.design.component.ButtonStyle
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.component.TextDefaults
import network.marsys.smarthome.shared.library.design.icons.ChevronRight
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken

@Composable
fun AreasSection(
    state: DashboardScreenState.AreasState,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(16.dp),
    ) {
        AreasSectionHeader(
            condition = state.condition,
            onAction = onAction,
        )
    }
}

@Composable
private fun AreasSectionHeader(
    condition: DashboardScreenState.Condition,
    onAction: (DashboardScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionHeader(
        title = "Areas",
        modifier = modifier,
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered = interactionSource.collectIsHoveredAsState().value
        val isPressed = interactionSource.collectIsPressedAsState().value

        if (condition is DashboardScreenState.Condition.Success) {
            CompositionLocalProvider(
                LocalContentColor provides SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
            ) {
                @OptIn(ExperimentalFoundationStyleApi::class)
                Button(
                    onClick = {
                        onAction.invoke(DashboardScreenAction.NavigateToAreas)
                    },
                    style = ButtonStyle.text(
                        pressedContent = SmartHomeTheme.colors[ColorKeyToken.TextPrimary],
                    ),
                    interactionSource = interactionSource,
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement
                            .spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "See all",
                            style = TextDefaults.normal then Style {
                                fontWeight(FontWeight.W500)
                            },
                        )

                        Icon(
                            icon = Icons.ChevronRight,
                            size = 16.dp,
                            tint = when {
                                isPressed || isHovered -> SmartHomeTheme.colors[ColorKeyToken.TextPrimary]
                                else -> SmartHomeTheme.colors[ColorKeyToken.TextSecondary]
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AreasSectionEmptyPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        AreasSection(
            state = AreasSectionPreviewData.empty(),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun AreasSectionErrorPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        AreasSection(
            state = AreasSectionPreviewData.error(),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun AreasSectionLoadedPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        AreasSection(
            state = AreasSectionPreviewData.loaded(),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun AreasSectionLoadingPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        AreasSection(
            state = AreasSectionPreviewData.loading(),
            onAction = {},
        )
    }
}

internal object AreasSectionPreviewData {
    fun empty() = object : DashboardScreenState.AreasState {
        override val condition: DashboardScreenState.Condition = DashboardScreenState.Condition.Empty
    }

    fun error() = object : DashboardScreenState.AreasState {
        override val condition: DashboardScreenState.Condition = DashboardScreenState.Condition.Error
    }

    fun loaded() = object : DashboardScreenState.AreasState {
        override val condition: DashboardScreenState.Condition = DashboardScreenState.Condition.Success
    }

    fun loading() = object : DashboardScreenState.AreasState {
        override val condition: DashboardScreenState.Condition = DashboardScreenState.Condition.Loading
    }
}
