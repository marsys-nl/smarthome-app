package network.marsys.smarthome.shared.feature.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.icons.HousePlug
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme
import network.marsys.smarthome.shared.library.design.theme.tokens.ColorKeyToken
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens

private val OnboardingScreenIconShape = RoundedCornerShape(size = 16.dp)
private val OnboardingScreenIconShadow = Shadow(
    radius = 2.dp,
    spread = 0.dp,
    color = PaletteTokens.Base.Black
        .copy(alpha = 0.05f),
    offset = DpOffset(x = 0.dp, y = 1.dp),
)

@Composable
internal fun OnboardingScreenIcon(
    icon: ImageVector,
) = Box(
    modifier = Modifier
        .padding(bottom = 16.dp)
        .dropShadow(
            shape = OnboardingScreenIconShape,
            shadow = OnboardingScreenIconShadow,
        )
        .background(
            color = PaletteTokens.Base.White,
            shape = OnboardingScreenIconShape,
        )
        .padding(all = 16.dp),
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
