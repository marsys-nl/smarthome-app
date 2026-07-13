package network.marsys.smarthome.shared.library.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import network.marsys.smarthome.shared.domain.entity.capability.ThermostatStatus
import network.marsys.smarthome.shared.domain.entity.entity.Entity
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

@Composable
fun Entity.State.Descriptor.localized(): String =
    localized(LocalDescriptorStrings.current)

@Composable
@Suppress("CyclomaticComplexMethod")
fun Entity.State.Descriptor.localized(strings: DescriptorStrings): String = when (this) {
    Entity.State.Descriptor.On -> strings.on

    Entity.State.Descriptor.Off -> strings.off

    Entity.State.Descriptor.Open -> strings.open

    Entity.State.Descriptor.Closed -> strings.closed

    is Entity.State.Descriptor.Opened -> strings.opened.replaceWithArgs(listOf(percentage.toString()))

    Entity.State.Descriptor.Opening -> strings.opening

    Entity.State.Descriptor.Closing -> strings.closing

    is Entity.State.Descriptor.Combined ->
        parts
            .map { it.localized(strings) }
            .joinToString(separator = strings.separator)

    is Entity.State.Descriptor.Enum<*> ->
        when (value) {
            ThermostatStatus.Status.Idle -> strings.idle
            ThermostatStatus.Status.Cooling -> strings.cooling
            ThermostatStatus.Status.Heating -> strings.heating
            else -> value.toString()
        }

    is Entity.State.Descriptor.Value<*> ->
        value.toString()

    Entity.State.Descriptor.Unknown ->
        "-"

    Entity.State.Descriptor.Empty ->
        ""
}

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
