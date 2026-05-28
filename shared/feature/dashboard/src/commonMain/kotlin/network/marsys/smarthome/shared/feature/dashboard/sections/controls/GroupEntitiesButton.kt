package network.marsys.smarthome.shared.feature.dashboard.sections.controls

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.Res
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.quick_control_group_button_title
import network.marsys.smarthome.shared.feature.dashboard.dashboard.generated.resources.quick_control_ungroup_button_title
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.Grid
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Layers
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import org.jetbrains.compose.resources.stringResource

@Composable
fun GroupEntitiesButton(
    groupByType: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = GroupEntitiesButtonDefaults.contentPadding(),
) {
    Card(
        modifier = modifier
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onClick,
            ),
        colors = CardDefaults.colors(
            backgroundColor = GroupEntitiesButtonDefaults.backgroundColor(groupByType),
            borderColor = GroupEntitiesButtonDefaults.borderColor(groupByType),
        ),
        contentPadding = contentPadding,
        borderWidth = 1.dp,
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement
                .spacedBy(6.dp),
        ) {
            CompositionLocalProvider(
                LocalContentColor provides GroupEntitiesButtonDefaults.foregroundColor(groupByType),
            ) {
                Icon(
                    icon = GroupEntitiesButtonDefaults.buttonIcon(groupByType),
                    size = 14.dp,
                )

                Text(
                    modifier = Modifier,
                    text = GroupEntitiesButtonDefaults.buttonText(groupByType),
                    lineHeight = 16.sp,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600,
                )
            }
        }
    }
}

private object GroupEntitiesButtonDefaults {
    @Composable
    fun contentPadding() = PaddingValues(
        horizontal = 12.dp,
        vertical = 6.dp,
    )

    @Composable
    fun backgroundColor(groupByType: Boolean) =
        SolidColor(
            value = SmartHomeTheme.colors.get(
                token = if (groupByType) {
                    ColorKeyToken.BackgroundSecondarySelected
                } else {
                    ColorKeyToken.BackgroundSecondary
                },
            ),
        )

    @Composable
    fun borderColor(groupByType: Boolean) =
        SmartHomeTheme.colors.get(
            token = if (groupByType) {
                ColorKeyToken.BorderBrandPrimary
            } else {
                ColorKeyToken.BorderPrimary
            },
        )

    @Composable
    fun buttonText(groupByType: Boolean): String =
        stringResource(
            resource = if (groupByType) {
                Res.string.quick_control_ungroup_button_title
            } else {
                Res.string.quick_control_group_button_title
            },
        )

    @Composable
    fun buttonIcon(groupByType: Boolean): ImageVector =
        if (groupByType) {
            Icons.Grid
        } else {
            Icons.Layers
        }

    @Composable
    fun foregroundColor(groupByType: Boolean) =
        SmartHomeTheme.colors.get(
            token = if (groupByType) {
                ColorKeyToken.TextBrandOnBrand
            } else {
                ColorKeyToken.TextSecondary
            },
        )
}
