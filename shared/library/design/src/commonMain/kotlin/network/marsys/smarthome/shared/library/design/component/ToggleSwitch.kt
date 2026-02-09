package network.marsys.smarthome.shared.library.design.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.SmartHomeTheme
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.ColorSchemePreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import com.composeunstyled.ToggleSwitch as UnstyledToggleSwitch

@Composable
fun ToggleSwitch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {},
    colors: ToggleSwitchColors = ToggleSwitchDefaults.colors(),
    enabled: Boolean = true,
) {
    val transition = updateTransition(checked, "switch-animation")

    val trackColor by transition.animateColor(transitionSpec = ToggleSwitchDefaults.transition()) {
        colors.trackColor(enabled, it).value
    }

    val thumbColor by transition.animateColor(transitionSpec = ToggleSwitchDefaults.transition()) {
        colors.thumbColor(enabled, it).value
    }

    UnstyledToggleSwitch(
        toggled = checked,
        modifier = modifier
            .size(width = 36.dp, height = 20.dp),
        enabled = enabled,
        shape = CircleShape,
        backgroundColor = trackColor,
        onToggled = onCheckedChange,
        contentPadding = PaddingValues(all = 2.dp),
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .dropShadow(
                    shape = CircleShape,
                    shadow = Shadow(
                        radius = 6.dp,
                        spread = -4.dp,
                        color = PaletteTokens.Base.Black.copy(alpha = 1f),
                        offset = DpOffset(0.dp, 4.dp),
                    ),
                )
                .background(thumbColor, CircleShape),
        )
    }
}

@Immutable
@ConsistentCopyVisibility
data class ToggleSwitchColors internal constructor(
    val activeTrackColor: Color,
    val inactiveTrackColor: Color,
    val activeThumbColor: Color,
    val inactiveThumbColor: Color,
    val activeDisabledTrackColor: Color,
    val inactiveDisabledTrackColor: Color,
    val activeDisabledThumbColor: Color,
    val inactiveDisabledThumbColor: Color,
) {
    @Composable
    internal fun trackColor(enabled: Boolean, active: Boolean): State<Color> =
        rememberUpdatedState(
            if (enabled) {
                if (active) activeTrackColor else inactiveTrackColor
            } else {
                if (active) activeDisabledTrackColor else inactiveDisabledTrackColor
            },
        )

    @Composable
    internal fun thumbColor(enabled: Boolean, active: Boolean): State<Color> =
        rememberUpdatedState(
            if (enabled) {
                if (active) activeThumbColor else inactiveThumbColor
            } else {
                if (active) activeDisabledThumbColor else inactiveDisabledThumbColor
            },
        )
}

object ToggleSwitchDefaults {
    private const val ANIMATION_DURATION_MILLIS = 500

    @Composable
    fun colors(
        activeTrackColor: Color = PaletteTokens.Emerald.Emerald500,
        inactiveTrackColor: Color = PaletteTokens.Neutral.Neutral200,
        activeThumbColor: Color = Color.White,
        inactiveThumbColor: Color = activeThumbColor,
        activeDisabledTrackColor: Color = PaletteTokens.Neutral.Neutral100,
        inactiveDisabledTrackColor: Color = activeDisabledTrackColor,
        activeDisabledThumbColor: Color = activeThumbColor,
        inactiveDisabledThumbColor: Color = activeThumbColor,
    ): ToggleSwitchColors = ToggleSwitchColors(
        activeTrackColor = activeTrackColor,
        inactiveTrackColor = inactiveTrackColor,
        activeThumbColor = activeThumbColor,
        inactiveThumbColor = inactiveThumbColor,
        activeDisabledTrackColor = activeDisabledTrackColor,
        inactiveDisabledTrackColor = inactiveDisabledTrackColor,
        activeDisabledThumbColor = activeDisabledThumbColor,
        inactiveDisabledThumbColor = inactiveDisabledThumbColor,
    )

    @Composable
    internal fun <T> transition(
        duration: Int = ANIMATION_DURATION_MILLIS,
    ): @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<T> = {
        tween(duration)
    }
}

@Preview
@Composable
private fun CheckedToggleSwitchPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        ToggleSwitch(
            checked = true,
            enabled = true,
        )
    }
}

@Preview
@Composable
private fun UncheckedToggleSwitchPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        ToggleSwitch(
            checked = false,
            enabled = true,
        )
    }
}

@Preview
@Composable
private fun DisabledUncheckedToggleSwitchPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        ToggleSwitch(
            checked = true,
            enabled = false,
        )
    }
}

@Preview
@Composable
private fun DisabledCheckedToggleSwitchPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        ToggleSwitch(
            checked = false,
            enabled = false,
        )
    }
}
