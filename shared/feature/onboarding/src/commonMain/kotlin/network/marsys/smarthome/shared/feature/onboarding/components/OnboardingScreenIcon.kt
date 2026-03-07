package network.marsys.smarthome.shared.feature.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.design.component.Card
import network.marsys.smarthome.shared.library.design.icons.HousePlug
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.ThemeSelectionPreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken

@Composable
internal fun OnboardingScreenIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
) = Card(
    modifier = modifier,
) {
    Image(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier
            .size(size = 32.dp),
        colorFilter = ColorFilter.tint(
            color = LocalColorScheme.current[ColorKeyToken.ForegroundBrandPrimary],
        ),
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
