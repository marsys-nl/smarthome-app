package network.marsys.smarthome.shared.library.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import network.marsys.smarthome.domain.capability.ThermostatStatus.Status
import network.marsys.smarthome.shared.domain.entity.Descriptor
import network.marsys.smarthome.shared.library.resources.SmartHomeRes
import network.marsys.smarthome.shared.library.resources.entity_state_closed
import network.marsys.smarthome.shared.library.resources.entity_state_closing
import network.marsys.smarthome.shared.library.resources.entity_state_cooling
import network.marsys.smarthome.shared.library.resources.entity_state_heating
import network.marsys.smarthome.shared.library.resources.entity_state_idle
import network.marsys.smarthome.shared.library.resources.entity_state_off
import network.marsys.smarthome.shared.library.resources.entity_state_on
import network.marsys.smarthome.shared.library.resources.entity_state_open_percentage
import network.marsys.smarthome.shared.library.resources.entity_state_opened
import network.marsys.smarthome.shared.library.resources.entity_state_opening
import network.marsys.smarthome.shared.library.resources.entity_state_separator
import kotlin.time.Clock

@Composable
fun Descriptor.localized(): String =
    localized(LocalDescriptorStrings.current)

@Composable
@Suppress("CyclomaticComplexMethod")
fun Descriptor.localized(strings: DescriptorStrings): String =
    when (this) {
        Descriptor.On -> strings.on

        Descriptor.Off -> strings.off

        Descriptor.Open -> strings.open

        Descriptor.Closed -> strings.closed

        is Descriptor.Opened -> strings.opened.replaceWithArgs(listOf(percentage.toString()))

        Descriptor.Opening -> strings.opening

        Descriptor.Closing -> strings.closing

        is Descriptor.Combined ->
            @Suppress("SimplifiableCallChain")
            parts
                .map { it.localized(strings) }
                .joinToString(separator = strings.separator)

        is Descriptor.Enum<*> ->
            when (value) {
                Status.Idle -> strings.idle
                Status.Cooling -> strings.cooling
                Status.Heating -> strings.heating
                else -> value.toString()
            }

        is Descriptor.Date ->
            value.toString()

        is Descriptor.TimeAgo ->
            with(Clock.System.now().minus(value)) {
                timeAgo(
                    duration = this,
                    display = strategy.displayFor(this),
                    kind = kind,
                )
            }

        is Descriptor.Value<*> ->
            value.toString()

        is Descriptor.FormattedValue<*> ->
            value.format(
                scale = scale,
                rounding = rounding,
            )

        Descriptor.Unknown ->
            "-"

        Descriptor.Empty ->
            ""
    }

/*
 * Descriptor strings
 */

@Immutable
data class DescriptorStrings(
    val on: String,
    val off: String,
    val open: String,
    val closed: String,
    val opened: String,
    val opening: String,
    val closing: String,
    val idle: String,
    val cooling: String,
    val heating: String,
    val separator: String,
)

val LocalDescriptorStrings = compositionLocalOf<DescriptorStrings> {
    error("There is no set of descriptor strings provided.")
}

@Composable
@Suppress("VariableMinLength")
fun rememberDescriptorStrings(): DescriptorStrings {
    val on = stringResource(SmartHomeRes.string.entity_state_on)
    val off = stringResource(SmartHomeRes.string.entity_state_off)
    val open = stringResource(SmartHomeRes.string.entity_state_opened)
    val closed = stringResource(SmartHomeRes.string.entity_state_closed)
    val opened = stringResource(SmartHomeRes.string.entity_state_open_percentage)
    val opening = stringResource(SmartHomeRes.string.entity_state_opening)
    val closing = stringResource(SmartHomeRes.string.entity_state_closing)
    val idle = stringResource(SmartHomeRes.string.entity_state_idle)
    val cooling = stringResource(SmartHomeRes.string.entity_state_cooling)
    val heating = stringResource(SmartHomeRes.string.entity_state_heating)

    val separator = stringResource(SmartHomeRes.string.entity_state_separator)

    return remember(on, off, open, closed, opened, opening, closing, separator, idle, cooling, heating) {
        DescriptorStrings(
            on = on,
            off = off,
            open = open,
            closed = closed,
            opened = opened,
            opening = opening,
            closing = closing,
            idle = idle,
            cooling = cooling,
            heating = heating,
            separator = separator,
        )
    }
}
