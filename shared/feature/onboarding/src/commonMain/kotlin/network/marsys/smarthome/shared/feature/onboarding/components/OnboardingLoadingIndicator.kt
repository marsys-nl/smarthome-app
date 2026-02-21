package network.marsys.smarthome.shared.feature.onboarding.components

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.ColorSchemePreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken

@Composable
fun OnboardingLoadingIndicator(
    @FloatRange(from = 0.0, to = 1.0)
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = LocalColorScheme.current[ColorKeyToken.BackgroundTertiary],
    foregroundColor: Color = LocalColorScheme.current[ColorKeyToken.BackgroundBrandPrimary],
) = OnboardingLoadingIndicator(
    progress = progress,
    modifier = modifier,
    background = SolidColor(backgroundColor),
    foreground = SolidColor(foregroundColor),
)

@Composable
fun OnboardingLoadingIndicator(
    @FloatRange(from = 0.0, to = 1.0)
    progress: Float,
    background: Brush,
    foreground: Brush,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(CircleShape)
            .background(background),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = progress)
                .background(foreground),
        )
    }
}

@Preview
@Composable
private fun OnboardingLoadingIndicatorNoProgressPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        OnboardingLoadingIndicator(
            progress = 0f,
        )
    }
}

@Preview
@Composable
private fun OnboardingLoadingIndicatorSomeProgressPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        OnboardingLoadingIndicator(
            progress = .5f,
        )
    }
}

@Preview
@Composable
private fun OnboardingLoadingIndicatorFullProgressPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        OnboardingLoadingIndicator(
            progress = 1f,
        )
    }
}
