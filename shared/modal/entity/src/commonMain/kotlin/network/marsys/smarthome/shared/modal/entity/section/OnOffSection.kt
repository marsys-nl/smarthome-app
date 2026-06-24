package network.marsys.smarthome.shared.modal.entity.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Switch
import network.marsys.smarthome.shared.library.design.component.SwitchDefaults
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.modal.entity.EntityDetailModalAction
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.Res
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_capability_on_off
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_off
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_state_on

@Composable
internal fun OnOffSection(
    entity: EntityIdentifier,
    onOff: OnOff,
    onAction: (EntityDetailModalAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                shape = CardDefaults.shape(),
                color = SmartHomeTheme.colors[ColorKeyToken.BackgroundTertiaryDisabled],
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.entity_capability_on_off),
            lineHeight = 24.sp,
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
        )

        Row(
            horizontalArrangement = Arrangement
                .spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(
                    resource = when (onOff.current) {
                        true -> Res.string.entity_state_on
                        false -> Res.string.entity_state_off
                    },
                ),
                lineHeight = 20.sp,
                fontSize = 14.sp,
                color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
            )

            Switch(
                checked = onOff.current,
                colors = SwitchDefaults.colors(
                    uncheckedTrackColor = PaletteTokens.Slate.Slate950
                        .copy(alpha = .2f),
                ),
                onCheckedChange = {
                    onAction.invoke(
                        EntityDetailModalAction.ToggleEntity(
                            entity = entity,
                            state = it,
                        ),
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun TurnedOffSectionPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        OnOffSection(
            entity = EntityIdentifier("light.living-room"),
            onOff = OnOffSectionPreviewData.light,
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun TurnedOnSectionPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        OnOffSection(
            entity = EntityIdentifier("smart-plug.office"),
            onOff = OnOffSectionPreviewData.smartPlug,
            onAction = {},
        )
    }
}

private object OnOffSectionPreviewData {
    val light = OnOff(current = true)
    val smartPlug = OnOff(current = false)
}
