package network.marsys.smarthome.shared.library.design.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpSize
import com.composeunstyled.ToggleSwitch
import network.marsys.smarthome.shared.library.design.SmartHomeComponentPreview
import network.marsys.smarthome.shared.library.design.theme.ColorScheme
import network.marsys.smarthome.shared.library.design.theme.ColorSchemePreviewParameterProvider
import network.marsys.smarthome.shared.library.design.theme.tokens.components.SwitchTokens

@Composable
fun Switch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {},
    colors: SwitchColors = SwitchDefaults.colors(),
    enabled: Boolean = true,
    size: SwitchSize = SwitchSize.Normal,
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
            .size(SwitchDefaults.trackSize(size)),
        enabled = enabled,
        shape = SwitchDefaults.trackShape(),
        backgroundColor = trackColor,
        onToggled = onCheckedChange,
        contentPadding = SwitchDefaults.trackPadding(size),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(1f)
                .aspectRatio(1f)
                .dropShadow(
                    shape = SwitchDefaults.thumbShape(),
                    shadow = SwitchDefaults.thumbShadow(),
                )
                .background(
                    color = thumbColor,
                    shape = SwitchDefaults.thumbShape(),
                ),
        )
    }
}

@Immutable
@ConsistentCopyVisibility
data class SwitchColors internal constructor(
    private val checkedTrackColor: Color,
    private val uncheckedTrackColor: Color,
    private val checkedThumbColor: Color,
    private val uncheckedThumbColor: Color,
    private val disabledCheckedTrackColor: Color,
    private val disabledUncheckedTrackColor: Color,
    private val disabledCheckedThumbColor: Color,
    private val disabledUncheckedThumbColor: Color,
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

sealed interface SwitchSize {
    data object Normal : SwitchSize
    data object Small : SwitchSize
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
    fun thumbShadow(): Shadow = Shadow(
        radius = SwitchTokens.ThumbShadowRadius,
        spread = SwitchTokens.ThumbShadowSpread,
        color = SwitchTokens.ThumbShadowColor,
        offset = SwitchTokens.ThumbShadowOffset,
    )

    @Composable
    fun thumbShape(): Shape = SwitchTokens.ThumbShape

    @Composable
    fun trackPadding(size: SwitchSize = SwitchSize.Normal): PaddingValues = PaddingValues(
        all = when (size) {
            SwitchSize.Normal -> SwitchTokens.ThumbPaddingNormal
            SwitchSize.Small -> SwitchTokens.ThumbPaddingSmall
        },
    )

    @Composable
    fun trackShape(): Shape = SwitchTokens.TrackShape

    @Composable
    fun trackSize(size: SwitchSize = SwitchSize.Normal): DpSize = when (size) {
        SwitchSize.Normal -> DpSize(
            width = SwitchTokens.TrackWidthNormal,
            height = SwitchTokens.TrackHeightNormal,
        )
        SwitchSize.Small -> DpSize(
            width = SwitchTokens.TrackWidthSmall,
            height = SwitchTokens.TrackHeightSmall,
        )
    }

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

@Preview
@Composable
private fun SmallCheckedSwitchPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        Switch(
            checked = true,
            enabled = true,
            size = SwitchSize.Small,
        )
    }
}

@Preview
@Composable
private fun SmallUncheckedSwitchPreview(
    @PreviewParameter(ColorSchemePreviewParameterProvider::class) scheme: ColorScheme,
) {
    SmartHomeComponentPreview(
        scheme = scheme,
    ) {
        Switch(
            checked = false,
            enabled = true,
            size = SwitchSize.Small,
        )
    }
}
