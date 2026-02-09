package network.marsys.smarthome.shared.library.design.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.ColorSchemePreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.PaletteTokens
import com.composeunstyled.ToggleSwitch
import network.marsys.smarthome.shared.library.design.theme.tokens.components.SwitchTokens

@Composable
fun Switch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {},
    colors: SwitchColors = SwitchDefaults.colors(),
    enabled: Boolean = true,
) {
    val transition = updateTransition(checked, "switch-animation")

    val trackColor by transition.animateColor(transitionSpec = SwitchDefaults.transition()) {
        colors.trackColor(enabled, it).value
    }

    val thumbColor by transition.animateColor(transitionSpec = SwitchDefaults.transition()) {
        colors.thumbColor(enabled, it).value
    }

    ToggleSwitch(
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
data class SwitchColors internal constructor(
    val checkedTrackColor: Color,
    val uncheckedTrackColor: Color,
    val checkedThumbColor: Color,
    val uncheckedThumbColor: Color,
    val disabledCheckedTrackColor: Color,
    val disabledUncheckedTrackColor: Color,
    val disabledCheckedThumbColor: Color,
    val disabledUncheckedThumbColor: Color,
) {
    @Composable
    internal fun trackColor(enabled: Boolean, checked: Boolean): State<Color> =
        rememberUpdatedState(
            if (enabled) {
                if (checked) checkedTrackColor else uncheckedTrackColor
            } else {
                if (checked) disabledCheckedTrackColor else disabledUncheckedTrackColor
            },
        )

    @Composable
    internal fun thumbColor(enabled: Boolean, checked: Boolean): State<Color> =
        rememberUpdatedState(
            if (enabled) {
                if (checked) checkedThumbColor else uncheckedThumbColor
            } else {
                if (checked) disabledCheckedThumbColor else disabledUncheckedThumbColor
            },
        )
}

object SwitchDefaults {
    private const val ANIMATION_DURATION_MILLIS = 500

    @Composable
    fun colors(
        checkedTrackColor: Color = SwitchTokens.CheckedTrackColor,
        uncheckedTrackColor: Color = SwitchTokens.UncheckedTrackColor,
        checkedThumbColor: Color = SwitchTokens.CheckedThumbColor,
        uncheckedThumbColor: Color = SwitchTokens.UncheckedThumbColor,
        disabledCheckedTrackColor: Color = SwitchTokens.DisabledCheckedTrackColor,
        disabledUncheckedTrackColor: Color = SwitchTokens.DisabledUncheckedTrackColor,
        disabledCheckedThumbColor: Color = SwitchTokens.DisabledCheckedThumbColor,
        disabledUncheckedThumbColor: Color = SwitchTokens.DisabledUncheckedThumbColor,
    ): SwitchColors = SwitchColors(
        checkedTrackColor = checkedTrackColor,
        uncheckedTrackColor = uncheckedTrackColor,
        checkedThumbColor = checkedThumbColor,
        uncheckedThumbColor = uncheckedThumbColor,
        disabledCheckedTrackColor = disabledCheckedTrackColor,
        disabledUncheckedTrackColor = disabledUncheckedTrackColor,
        disabledCheckedThumbColor = disabledCheckedThumbColor,
        disabledUncheckedThumbColor = disabledUncheckedThumbColor,
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
private fun CheckedSwitchPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        Switch(
            checked = true,
            enabled = true,
        )
    }
}

@Preview
@Composable
private fun UncheckedSwitchPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        Switch(
            checked = false,
            enabled = true,
        )
    }
}

@Preview
@Composable
private fun DisabledCheckedSwitchPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        Switch(
            checked = false,
            enabled = false,
        )
    }
}

@Preview
@Composable
private fun DisabledUncheckedSwitchPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        Switch(
            checked = true,
            enabled = false,
        )
    }
}
