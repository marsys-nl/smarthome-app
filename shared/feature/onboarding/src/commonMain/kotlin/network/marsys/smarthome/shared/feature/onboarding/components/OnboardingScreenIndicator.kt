package network.marsys.smarthome.shared.feature.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun OnboardingScreenIndicator(
    screen: Int,
    screens: Int,
    modifier: Modifier = Modifier,
    colors: OnboardingScreenIndicatorColors = OnboardingScreenIndicatorDefaults.colors(),
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(screens) { index ->
            val sizeModifier = if (index + 1 == screen) {
                Modifier
                    .size(32.dp, 6.dp)
            } else {
                Modifier
                    .size(6.dp)
            }

            val backgroundColor = colors.screenIndicatorColor(
                current = screen,
                index = index + 1,
            ).value

            Box(
                modifier = sizeModifier
                    .background(
                        brush = backgroundColor,
                        shape = CircleShape,
                    ),
            )
        }
    }
}

@Immutable
@ConsistentCopyVisibility
data class OnboardingScreenIndicatorColors internal constructor(
    private val activeScreenIndicatorColor: Brush,
    private val currentScreenIndicatorColor: Brush,
    private val inactiveScreenIndicatorColor: Brush,
) {
    @Composable
    fun screenIndicatorColor(current: Int, index: Int): State<Brush> =
        rememberUpdatedState(
            when {
                index < current -> activeScreenIndicatorColor
                index == current -> currentScreenIndicatorColor
                else -> inactiveScreenIndicatorColor
            },
        )
}

object OnboardingScreenIndicatorDefaults {
    @Composable
    fun colors(
        activeScreenIndicatorColor: Brush = SolidColor(LocalColorScheme.current[ColorKeyToken.BackgroundBrandPrimary]),
        currentScreenIndicatorColor: Brush = activeScreenIndicatorColor,
        inactiveScreenIndicatorColor: Brush = SolidColor(LocalColorScheme.current[ColorKeyToken.BackgroundTertiary]),
    ) = OnboardingScreenIndicatorColors(
        activeScreenIndicatorColor = activeScreenIndicatorColor,
        currentScreenIndicatorColor = currentScreenIndicatorColor,
        inactiveScreenIndicatorColor = inactiveScreenIndicatorColor,
    )
}

@Preview
@Composable
private fun OnboardingScreenIndicatorSinglePagePreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        OnboardingScreenIndicator(
            screen = 1,
            screens = 1,
        )
    }
}

@Preview
@Composable
private fun OnboardingScreenIndicatorMultiplePagesPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        OnboardingScreenIndicator(
            screen = 1,
            screens = 3,
        )
    }
}

@Preview
@Composable
private fun OnboardingScreenIndicatorMiddlePagesPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        OnboardingScreenIndicator(
            screen = 2,
            screens = 3,
        )
    }
}
