package network.marsys.smarthome.shared.modal.entity.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.percent
import network.marsys.smarthome.shared.domain.entity.capability.Brightness
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.i18n.localized
import network.marsys.smarthome.shared.library.i18n.stringResource
import network.marsys.smarthome.shared.modal.entity.EntityDetailModalAction
import network.marsys.smarthome.shared.modal.entity.component.QuantityControl
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.Res
import network.marsys.smarthome.shared.modal.entity.entity.generated.resources.entity_capability_brightness

@Composable
internal fun BrightnessSection(
    entity: EntityIdentifier,
    brightness: Brightness,
    onAction: (EntityDetailModalAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement
            .spacedBy(12.dp),
    ) {
        Text(
            text = brightness.descriptor
                .localized(),
            lineHeight = 72.sp,
            fontSize = 72.sp,
            fontWeight = FontWeight.W700,
            color = SmartHomeTheme.colors[ColorKeyToken.ForegroundBrandPrimary],
        )

        Text(
            text = stringResource(Res.string.entity_capability_brightness),
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            color = SmartHomeTheme.colors[ColorKeyToken.TextSecondary],
        )

        BrightnessControl(
            entity = entity,
            brightness = brightness,
            onAction = onAction,
        )
    }
}

@Composable
private fun BrightnessControl(
    entity: EntityIdentifier,
    brightness: Brightness,
    onAction: (EntityDetailModalAction) -> Unit,
) {
    QuantityControl(
        quantity = brightness.current,
        range = 0.percent..100.percent,
        onQuantityChange = {
            onAction.invoke(
                EntityDetailModalAction.AdjustBrightness(
                    entity = entity,
                    brightness = it,
                ),
            )
        },
    )
}

@Preview
@Composable
private fun BrightnessSectionPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        BrightnessSection(
            entity = EntityIdentifier("light.living-room"),
            brightness = BrightnessSectionPreviewData.brightness,
            onAction = {},
        )
    }
}

private object BrightnessSectionPreviewData {
    val brightness = Brightness(
        current = 55.percent,
    )
}
