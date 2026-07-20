@file:OptIn(ExperimentalFoundationStyleApi::class)

package network.marsys.smarthome.shared.feature.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.Res
import network.marsys.smarthome.shared.feature.onboarding.onboarding.generated.resources.onboarding_button_next
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.annotation.PreviewLocales
import network.marsys.smarthome.shared.library.design.component.Button
import network.marsys.smarthome.shared.library.design.component.ButtonStyle
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.ArrowRight
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.components.ButtonColorTokens
import network.marsys.smarthome.shared.library.i18n.stringResource

@Composable
fun OnboardingNextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = ButtonStyle.primary(),
) {
    Button(
        onClick = onClick,
        style = style,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement
                .spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val density = LocalDensity.current
            var textHeight by remember { mutableStateOf(0.dp) }

            Text(
                text = stringResource(Res.string.onboarding_button_next),
                modifier = Modifier
                    .onGloballyPositioned {
                        textHeight = with(density) {
                            it.size.height.toDp()
                        }
                    },
                fontWeight = FontWeight.Bold,
            )

            Image(
                imageVector = Icons.ArrowRight,
                contentDescription = null,
                modifier = Modifier
                    .height(textHeight),
                colorFilter = ColorFilter.tint(
                    color = ButtonColorTokens.ContentColor,
                ),
            )
        }
    }
}

@PreviewLocales
@Composable
private fun OnboardingNextButtonPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        OnboardingNextButton(
            onClick = {},
        )
    }
}
