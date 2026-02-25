package network.marsys.smarthome.shared.feature.onboarding.components

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
internal fun OnboardingProgressIndicator(
    numberOfScreens: Int,
    indexOfScreen: Int,
    modifier: Modifier = Modifier,
    colors: OnboardingProgressIndicatorColors = OnboardingProgressIndicatorDefaults.colors(),
) = OnboardingProgressIndicator(
    progress = 1f / (numberOfScreens + 1f) * indexOfScreen,
    modifier = modifier,
    colors = colors,
)

@Composable
private fun OnboardingProgressIndicator(
    @FloatRange(from = 0.0, to = 1.0)
    progress: Float,
    modifier: Modifier = Modifier,
    colors: OnboardingProgressIndicatorColors = OnboardingProgressIndicatorDefaults.colors(),
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(CircleShape)
            .background(colors.background),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = progress)
                .background(colors.foreground),
        )
    }
}

@Immutable
@ConsistentCopyVisibility
data class OnboardingProgressIndicatorColors internal constructor(
    internal val background: Brush,
    internal val foreground: Brush,
)

object OnboardingProgressIndicatorDefaults {
    @Composable
    fun colors(
        background: Brush = SolidColor(LocalColorScheme.current[ColorKeyToken.BackgroundTertiary]),
        foreground: Brush = SolidColor(LocalColorScheme.current[ColorKeyToken.BackgroundBrandPrimary]),
    ) = OnboardingProgressIndicatorColors(
        background = background,
        foreground = foreground,
    )
}

@Preview
@Composable
private fun OnboardingProgressIndicatorNoProgressPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        OnboardingProgressIndicator(
            progress = 0f,
        )
    }
}

@Preview
@Composable
private fun OnboardingProgressIndicatorSomeProgressPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        OnboardingProgressIndicator(
            progress = .5f,
        )
    }
}

@Preview
@Composable
private fun OnboardingProgressIndicatorFullProgressPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        OnboardingProgressIndicator(
            progress = 1f,
        )
    }
}
