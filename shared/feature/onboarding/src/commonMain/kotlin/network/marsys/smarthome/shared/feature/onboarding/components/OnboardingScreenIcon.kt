package network.marsys.smarthome.shared.feature.onboarding.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.component.CardDefaults
import network.marsys.smarthome.shared.library.design.component.Icon
import network.marsys.smarthome.shared.library.design.icons.HousePlug
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.components.CardTokens

@Composable
internal fun OnboardingScreenIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    colors: OnboardingScreenIconColors = OnboardingScreenIconDefaults.colors(),
) = Card(
    modifier = modifier,
    colors = CardDefaults.colors(
        backgroundColor = colors.backgroundColor,
        contentColor = colors.contentColor,
    ),
) {
    Icon(
        icon = icon,
    )
}

@Immutable
@ConsistentCopyVisibility
data class OnboardingScreenIconColors internal constructor(
    internal val backgroundColor: Brush,
    internal val contentColor: Color,
)

object OnboardingScreenIconDefaults {
    @Composable
    fun colors(
        backgroundColor: Brush = CardTokens.BackgroundColor,
        contentColor: Color = SmartHomeTheme.colors[ColorKeyToken.ForegroundBrandPrimary],
    ): OnboardingScreenIconColors = OnboardingScreenIconColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
    )
}

@Preview
@Composable
private fun OnboardingEntitiesScreenIconPreview(
    @PreviewParameter(ThemeSelectionPreviewParameterProvider::class) theme: ThemeSelection,
) {
    SmartHomeComponentPreview(
        theme = theme,
    ) {
        OnboardingScreenIcon(
            icon = Icons.HousePlug,
        )
    }
}
