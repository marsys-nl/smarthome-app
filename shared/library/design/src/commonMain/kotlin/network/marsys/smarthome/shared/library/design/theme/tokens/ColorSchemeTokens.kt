@file:Suppress("PropertyName")

package network.marsys.smarthome.shared.library.design.theme.tokens

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Color naming scheme
 *
 * Naming a color scheme token is based on the following pattern:
 * Property-(Semantics)-(Hierarchy)-(State)
 *
 * Property: The type of color (e.g., Background, Border, Text)
 * Semantics: The purpose or meaning of the color (e.g., Brand, Error, Warning, Success, Info, Modal)
 * Hierarchy: The level of importance or prominence (e.g., Primary, Secondary, Tertiary)
 * State: The state of the element (e.g., Hover, Selected, Disabled, Subtle, Alternative)
 *
 * Semantics, hierarchy and state are optional, as long as either one of them is present.
 * The property is always required.
 */
@Suppress("ComplexInterface")
internal interface ColorSchemeTokens :
    ErrorColorSchemeTokens,
    WarningColorSchemeTokens,
    InfoColorSchemeTokens,
    SuccessColorSchemeTokens {

    val BackgroundPrimary: Color
    val BackgroundSecondary: Color
    val BackgroundSecondarySelected: Color
    val BackgroundTertiary: Color
    val BackgroundTertiaryAlternative: Color
    val BackgroundTertiaryDisabled: Color
    val BackgroundBrandPrimary: Color
    val BackgroundBrandSecondary: Color
    val BackgroundModal: Color
    val BackgroundDimmed: Color get() =
        PaletteTokens.Base.White.copy(alpha = .2f)
    val BackgroundDisabled: Color
    val BackgroundDisabledAlternative: Color
    val BorderPrimary: Color
    val BorderBrandPrimary: Color
    val BorderBrandPrimaryDimmed: Color
    val ForegroundPrimary: Color
    val ForegroundPrimaryAlternative: Color
    val ForegroundSecondary: Color
    val ForegroundBrandPrimary: Color
    val ForegroundDisabled: Color
    val GradientBrandPrimaryToSecondary: Brush get() =
        GradientTokens.Amber.Amber400.ToEmerald400
    val GradientDimmedPrimaryToSecondary: Brush get() =
        GradientTokens.Amber.Amber400.ToEmerald400(alpha = .2f)
    val TextPrimary: Color
    val TextSecondary: Color
    val TextSecondaryAlternative: Color
    val TextBrandOnBrand: Color
    val TextDisabled: Color
}

internal interface ErrorColorSchemeTokens {
    /**
     * Primary error state background color for components such as buttons and cards.
     */
    val BackgroundErrorPrimary: Color

    /**
     * Secondary error state background color for components such (featured) icons.
     */
    val BackgroundErrorSecondary: Color

    /**
     * Default solid error state background color for components such as buttons, cards and featured icons.
     */
    val BackgroundErrorSolid: Color

    /**
     * Default solid error state background color for components such as buttons, cards and featured icons when pressed.
     */
    val BackgroundErrorSolidPressed: Color

    /**
     * Primary error state color for non-text foreground elements such as icons.
     */
    val ForegroundErrorPrimary: Color

    /**
     * Default error state semantic border color for error states in input fields and such.
     */
    val BorderErrorPrimary: Color

    /**
     * Subtle error state semantic border color for components such as cards or notifications.
     */
    val BorderErrorSubtle: Color

    /**
     * Default error state semantic text color.
     */
    val TextErrorPrimary: Color

    /**
     * Default error state semantic text color for secondary elements.
     */
    val TextErrorSecondary: Color
}

internal interface WarningColorSchemeTokens {
    /**
     * Primary warning state background color for components such as buttons and cards.
     */
    val BackgroundWarningPrimary: Color

    /**
     * Secondary warning state background color for components such (featured) icons.
     */
    val BackgroundWarningSecondary: Color

    /**
     * Default solid warning state background color for components such as buttons, cards and featured icons.
     */
    val BackgroundWarningSolid: Color

    /**
     * Default solid warning state background color for components such as buttons,
     * cards and featured icons when pressed.
     */
    val BackgroundWarningSolidPressed: Color

    /**
     * Primary warning state color for non-text foreground elements such as icons.
     */
    val ForegroundWarningPrimary: Color

    /**
     * Subtle warning state semantic border color for components such as cards or notifications.
     */
    val BorderWarningSubtle: Color

    /**
     * Default warning state semantic text color.
     */
    val TextWarningPrimary: Color

    /**
     * Default warning state semantic text color for secondary elements.
     */
    val TextWarningSecondary: Color
}

internal interface InfoColorSchemeTokens {
    /**
     * Primary info state background color for components such as buttons and cards.
     */
    val BackgroundInfoPrimary: Color

    /**
     * Secondary info state background color for components such (featured) icons.
     */
    val BackgroundInfoSecondary: Color

    /**
     * Default solid info state background color for components such as buttons, cards and featured icons.
     */
    val BackgroundInfoSolid: Color

    /**
     * Default solid info state background color for components such as buttons,
     * cards and featured icons when pressed.
     */
    val BackgroundInfoSolidPressed: Color

    /**
     * Primary info state color for non-text foreground elements such as icons.
     */
    val ForegroundInfoPrimary: Color

    /**
     * Subtle info state semantic border color for components such as cards or notifications.
     */
    val BorderInfoSubtle: Color

    /**
     * Default info state semantic text color.
     */
    val TextInfoPrimary: Color

    /**
     * Default info state semantic text color for secondary elements.
     */
    val TextInfoSecondary: Color
}

internal interface SuccessColorSchemeTokens {
    /**
     * Primary success state background color for components such as buttons and cards.
     */
    val BackgroundSuccessPrimary: Color

    /**
     * Secondary success state background color for components such (featured) icons.
     */
    val BackgroundSuccessSecondary: Color

    /**
     * Default solid success state background color for components such as buttons, cards and featured icons.
     */
    val BackgroundSuccessSolid: Color

    /**
     * Default solid success state background color for components such as buttons,
     * cards and featured icons when pressed.
     */
    val BackgroundSuccessSolidPressed: Color

    /**
     * Primary success state color for non-text foreground elements such as icons.
     */
    val ForegroundSuccessPrimary: Color

    /**
     * Subtle success state semantic border color for components such as cards or notifications.
     */
    val BorderSuccessSubtle: Color

    /**
     * Default success state semantic text color.
     */
    val TextSuccessPrimary: Color

    /**
     * Default success state semantic text color for secondary elements.
     */
    val TextSuccessSecondary: Color
}
