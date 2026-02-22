package network.marsys.smarthome.shared.feature.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.component.Button
import network.marsys.smarthome.shared.library.design.component.ButtonColors
import network.marsys.smarthome.shared.library.design.component.ButtonDefaults
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.design.icons.ArrowRight
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.ColorSchemePreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.LocalContentColor

@Composable
fun OnboardingNextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.colors(),
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        colors = colors,
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
                text = "Next",
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
                contentDescription = "Next button icon",
                modifier = Modifier
                    .height(textHeight),
                colorFilter = ColorFilter.tint(
                    color = LocalContentColor.current,
                ),
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingNextButtonPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        OnboardingNextButton(
            onClick = {},
        )
    }
}
