package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import network.marsys.smarthome.shared.library.design.theme.LocalColorScheme

enum class ColorKeyToken {

    /**
     * Backgrounds.
     */

    BackgroundPrimary,
    BackgroundSecondary,
    BackgroundSecondarySelected,
    BackgroundTertiary,
    BackgroundTertiaryAlternative,
    BackgroundTertiaryDisabled,
    BackgroundBrandPrimary,
    BackgroundBrandSecondary,

    // Error
    BackgroundErrorPrimary,
    BackgroundErrorSecondary,
    BackgroundErrorSecondaryPressed,
    BackgroundErrorSolid,
    BackgroundErrorSolidPressed,

    // Warning
    BackgroundWarningPrimary,
    BackgroundWarningSecondary,
    BackgroundWarningSolid,
    BackgroundWarningSolidPressed,

    // Info
    BackgroundInfoPrimary,
    BackgroundInfoSecondary,
    BackgroundInfoSolid,
    BackgroundInfoSolidPressed,

    // Success
    BackgroundSuccessPrimary,
    BackgroundSuccessSecondary,
    BackgroundSuccessSolid,
    BackgroundSuccessSolidPressed,

    BackgroundModal,
    BackgroundDimmed,
    BackgroundDisabled,
    BackgroundDisabledAlternative,

    /**
     * Borders.
     */

    BorderPrimary,

    // Brand
    BorderBrandPrimary,
    BorderBrandPrimaryDimmed,

    // Semantics
    BorderErrorPrimary,
    BorderErrorSubtle,
    BorderWarningSubtle,
    BorderInfoSubtle,
    BorderSuccessSubtle,

    /**
     * Foregrounds.
     */

    ForegroundPrimary,
    ForegroundPrimaryAlternative,
    ForegroundSecondary,

    // Brand
    ForegroundBrandPrimary,

    // Semantics
    ForegroundErrorPrimary,
    ForegroundWarningPrimary,
    ForegroundInfoPrimary,
    ForegroundSuccessPrimary,

    ForegroundDisabled,

    /**
     * Texts.
     */

    TextPrimary,
    TextSecondary,
    TextSecondaryAlternative,

    // Brand
    TextBrandOnBrand,

    // Semantics
    TextErrorPrimary,
    TextErrorSecondary,
    TextWarningPrimary,
    TextWarningSecondary,
    TextInfoPrimary,
    TextInfoSecondary,
    TextSuccessPrimary,
    TextSuccessSecondary,

    TextDisabled,
}

@Composable
internal fun color(token: ColorKeyToken): Color =
    LocalColorScheme.current[token]
