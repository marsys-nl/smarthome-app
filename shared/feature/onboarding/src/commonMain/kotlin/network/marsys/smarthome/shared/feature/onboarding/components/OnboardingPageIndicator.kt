package network.marsys.smarthome.shared.feature.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.ColorSchemePreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken

@Composable
fun OnboardingPageIndicator(
    page: Int,
    pages: Int,
    modifier: Modifier = Modifier,
    activePageIndicatorColor: Color = LocalColorScheme.current[ColorKeyToken.BackgroundBrandPrimary],
    inactivePageIndicatorColor: Color = LocalColorScheme.current[ColorKeyToken.BackgroundTertiary],
) {
    Row(
        modifier = modifier
            .padding(top = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pages) { index ->
            val sizeModifier = if (index + 1 == page) {
                Modifier
                    .size(32.dp, 6.dp)
            } else {
                Modifier
                    .size(6.dp)
            }

            val backgroundColor = if (index + 1 == page) {
                activePageIndicatorColor
            } else {
                inactivePageIndicatorColor
            }

            Box(
                modifier = sizeModifier
                    .background(
                        color = backgroundColor,
                        shape = CircleShape,
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingLoadingIndicatorSinglePagePreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        OnboardingPageIndicator(
            page = 1,
            pages = 1,
        )
    }
}

@Preview
@Composable
private fun OnboardingLoadingIndicatorMultiplePagesPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        OnboardingPageIndicator(
            page = 1,
            pages = 3,
        )
    }
}

@Preview
@Composable
private fun OnboardingLoadingIndicatorMiddlePagesPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        OnboardingPageIndicator(
            page = 2,
            pages = 3,
        )
    }
}
