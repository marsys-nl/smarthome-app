package network.marsys.smarthome.shared.library.i18n

import androidx.compose.runtime.Composable
import network.marsys.smarthome.shared.domain.entity.TimeAgoDisplay
import network.marsys.smarthome.shared.domain.entity.TimeAgoKind
import network.marsys.smarthome.shared.library.resources.SmartHomeRes
import network.marsys.smarthome.shared.library.resources.time_ago_append
import network.marsys.smarthome.shared.library.resources.time_ago_days
import network.marsys.smarthome.shared.library.resources.time_ago_final_separator
import network.marsys.smarthome.shared.library.resources.time_ago_hours
import network.marsys.smarthome.shared.library.resources.time_ago_minutes
import network.marsys.smarthome.shared.library.resources.time_ago_recent
import network.marsys.smarthome.shared.library.resources.time_ago_seconds
import network.marsys.smarthome.shared.library.resources.time_ago_started
import network.marsys.smarthome.shared.library.resources.time_ago_updated
import kotlin.time.Duration
import kotlin.time.DurationUnit

@Composable
fun timeAgo(
    duration: Duration,
    display: TimeAgoDisplay,
    kind: TimeAgoKind = TimeAgoKind.Raw,
): String {
    val value = duration.format(display = display)

    val suffixed = when (display) {
        is TimeAgoDisplay.JustNow -> value

        is TimeAgoDisplay.Components ->
            when (kind) {
                TimeAgoKind.Raw -> value

                TimeAgoKind.Started, TimeAgoKind.Updated ->
                    stringResource(SmartHomeRes.string.time_ago_append, value)
            }
    }

    val wrapped = when (kind) {
        TimeAgoKind.Raw ->
            suffixed

        TimeAgoKind.Started ->
            stringResource(
                resource = SmartHomeRes.string.time_ago_started,
                formatArgs = arrayOf(suffixed.replaceFirstChar { it.lowercase() }),
            )

        TimeAgoKind.Updated ->
            stringResource(
                resource = SmartHomeRes.string.time_ago_updated,
                formatArgs = arrayOf(suffixed.replaceFirstChar { it.lowercase() }),
            )
    }

    return wrapped.replaceFirstChar { it.titlecase() }
}

@Composable
private fun Duration.format(display: TimeAgoDisplay): String =
    when (display) {
        is TimeAgoDisplay.JustNow ->
            stringResource(SmartHomeRes.string.time_ago_recent)

        is TimeAgoDisplay.Components ->
            format(units = display.units)
    }

@Composable
@Suppress("UnstableCollections")
private fun Duration.format(units: List<DurationUnit>): String {
    val components = toComponents { days, hours, minutes, seconds, _ ->
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
