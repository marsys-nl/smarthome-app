package network.marsys.smarthome.shared.library.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import network.marsys.smarthome.domain.capability.ThermostatStatus.Status
import network.marsys.smarthome.shared.domain.entity.AdaptiveUnits
import network.marsys.smarthome.shared.domain.entity.Descriptor
import network.marsys.smarthome.shared.domain.entity.FixedUnits
import network.marsys.smarthome.shared.domain.entity.TimeAgoDisplay
import network.marsys.smarthome.shared.domain.entity.TimeAgoStrategy
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
import network.marsys.smarthome.shared.library.resources.time_ago_append
import network.marsys.smarthome.shared.library.resources.time_ago_days
import network.marsys.smarthome.shared.library.resources.time_ago_final_separator
import network.marsys.smarthome.shared.library.resources.time_ago_hours
import network.marsys.smarthome.shared.library.resources.time_ago_minutes
import network.marsys.smarthome.shared.library.resources.time_ago_recent
import network.marsys.smarthome.shared.library.resources.time_ago_seconds
import network.marsys.smarthome.shared.library.resources.time_ago_updated
import org.jetbrains.compose.resources.PluralStringResource
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.Instant

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
                display(this, strategy.displayFor(this))
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
 * Time ago formatting
 */

@Composable
private fun display(duration: Duration, display: TimeAgoDisplay): String =
    stringResource(
        resource = SmartHomeRes.string.time_ago_updated,
        formatArgs = arrayOf(
            when (display) {
                is TimeAgoDisplay.JustNow -> stringResource(SmartHomeRes.string.time_ago_recent)

                is TimeAgoDisplay.Components ->
                    stringResource(
                        resource = SmartHomeRes.string.time_ago_append,
                        format(
                            duration = duration,
                            units = display.units,
                        ),
                    )
            },
        ),
    )

@Composable
@Suppress("UnstableCollections")
private fun format(duration: Duration, units: List<DurationUnit>): String {
    val components = duration.toComponents { days, hours, minutes, seconds, _ ->
        units.mapNotNull { unit ->
            when (unit) {
                DurationUnit.DAYS ->
                    SmartHomeRes.plurals.time_ago_days to days.toInt()

                DurationUnit.HOURS ->
                    SmartHomeRes.plurals.time_ago_hours to hours

                DurationUnit.MINUTES ->
                    SmartHomeRes.plurals.time_ago_minutes to minutes

                DurationUnit.SECONDS ->
                    SmartHomeRes.plurals.time_ago_seconds to seconds

                else -> null
            }
        }
    }

    val formatted = components
        .filter { it.second > 0 }
        .ifEmpty { components.takeLast(1) }
        .map { (resource, quantity) ->
            pluralStringResource(
                resource = resource,
                quantity = quantity,
            )
        }

    return when (formatted.size) {
        0 -> ""

        1 -> formatted.first()

        else -> buildString {
            append(formatted.dropLast(1).joinToString(separator = ", "))
            append(" ${stringResource(SmartHomeRes.string.time_ago_final_separator)} ")
            append(formatted.last())
        }
    }
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
