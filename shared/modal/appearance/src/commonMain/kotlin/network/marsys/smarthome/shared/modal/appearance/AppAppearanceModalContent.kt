package network.marsys.smarthome.shared.modal.appearance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.library.design.SmartHomeModalPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.annotation.PreviewScreenSizes
import network.marsys.smarthome.shared.library.design.component.Border
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Check
import network.marsys.smarthome.shared.library.design.icons.Close
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Monitor
import network.marsys.smarthome.shared.library.design.icons.Moon
import network.marsys.smarthome.shared.library.design.icons.Sun
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.library.resources.SmartHomeRes
import network.marsys.smarthome.shared.library.resources.app_appearance_mode_dark_description
import network.marsys.smarthome.shared.library.resources.app_appearance_mode_dark_title
import network.marsys.smarthome.shared.library.resources.app_appearance_mode_light_description
import network.marsys.smarthome.shared.library.resources.app_appearance_mode_light_title
import network.marsys.smarthome.shared.library.resources.app_appearance_mode_system_description
import network.marsys.smarthome.shared.library.resources.app_appearance_mode_system_title

@Composable
fun AppAppearanceModalContent(
    onDismissRequest: () -> Unit,
    onSelectTheme: (ThemeSelection) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement
            .spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Appearance",
                lineHeight = 28.sp,
                fontSize = 18.sp,
                fontWeight = FontWeight.W800,
            )

            CloseModalButton(
                onDismissRequest = onDismissRequest,
            )
        }

        SelectableThemeOption(
            title = stringResource(SmartHomeRes.string.app_appearance_mode_system_title),
            subtitle = stringResource(SmartHomeRes.string.app_appearance_mode_system_description),
            icon = Icons.Monitor,
            state = SmartHomeTheme.current == ThemeSelection.SystemDefault,
            onSelectTheme = {
                onSelectTheme(ThemeSelection.SystemDefault)
            },
        )

        SelectableThemeOption(
            title = stringResource(SmartHomeRes.string.app_appearance_mode_light_title),
            subtitle = stringResource(SmartHomeRes.string.app_appearance_mode_light_description),
            icon = Icons.Sun,
            state = SmartHomeTheme.current == ThemeSelection.LightMode,
            onSelectTheme = {
                onSelectTheme(ThemeSelection.LightMode)
            },
        )

        SelectableThemeOption(
            title = stringResource(SmartHomeRes.string.app_appearance_mode_dark_title),
            subtitle = stringResource(SmartHomeRes.string.app_appearance_mode_dark_description),
            icon = Icons.Moon,
            state = SmartHomeTheme.current == ThemeSelection.DarkMode,
            onSelectTheme = {
                onSelectTheme(ThemeSelection.DarkMode)
            },
        )
    }
}

@Composable
fun CloseModalButton(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onDismissRequest,
            ),
        colors = CardDefaults.colors(
            backgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundSecondary]),
        ),
        contentPadding = CardDefaults.contentPadding(
            horizontal = 8.dp,
            vertical = 8.dp,
        ),
    ) {
        Icon(
            icon = Icons.Close,
            size = 20.dp,
        )
    }
}

@Composable
private fun SelectableThemeOption(
    title: String,
    subtitle: String,
    icon: ImageVector,
    state: Boolean,
    onSelectTheme: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardColors = if (state) {
        CardDefaults.colors(
            backgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundSecondarySelected]),
            borderColor = LocalColorScheme.current[ColorKeyToken.BorderBrandPrimaryDimmed],
        )
    } else {
        CardDefaults.colors(
            backgroundColor = SolidColor(SmartHomeTheme.colors[ColorKeyToken.BackgroundSecondary]),
        )
    }

    val borderWidth = if (state) 1.dp else 0.dp

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = !state,
                onClickLabel = "Select $title",
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelectTheme,
            ),
        colors = cardColors,
        border = Border.Solid(borderWidth),
    ) {
        Row(
            horizontalArrangement = Arrangement
                .spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SelectableThemeIcon(
                icon = icon,
                state = state,
            )

            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    lineHeight = 24.sp,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )

                Text(
                    text = subtitle,
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                )
            }

            if (state) {
                SelectedThemeIcon()
            }
        }
    }
}

@Composable
private fun SelectableThemeIcon(
    icon: ImageVector,
    state: Boolean,
    modifier: Modifier = Modifier,
) {
    val iconBackgroundColorKeyToken =
        if (state) {
            ColorKeyToken.BackgroundBrandPrimary
        } else {
            ColorKeyToken.BackgroundTertiary
        }

    val iconForegroundColorKeyToken =
        if (state) {
            ColorKeyToken.ForegroundPrimaryAlternative
        } else {
            ColorKeyToken.ForegroundPrimary
        }

    Image(
        imageVector = icon,
        contentDescription = null,
        modifier = modifier
            .background(
                shape = RoundedCornerShape(16.dp),
                color = LocalColorScheme.current[iconBackgroundColorKeyToken],
            )
            .padding(12.dp),
        colorFilter = ColorFilter.tint(
            color = LocalColorScheme.current[iconForegroundColorKeyToken],
        ),
    )
}

@Composable
private fun SelectedThemeIcon() {
    Image(
        imageVector = Icons.Check,
        contentDescription = null,
        modifier = Modifier
            .background(
                shape = CircleShape,
                color = PaletteTokens.Emerald.Emerald500,
            )
            .size(size = 18.dp)
            .padding(all = 4.dp),
        colorFilter = ColorFilter.tint(
            color = PaletteTokens.Base.White,
        ),
    )
}

@PreviewScreenSizes
@Composable
private fun AppAppearanceModalContentPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeModalPreview(
        theme = theme,
    ) {
        AppAppearanceModalContent(
            onDismissRequest = {},
            onSelectTheme = {},
        )
    }
}
