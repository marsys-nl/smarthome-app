package network.marsys.smarthome.shared.library.i18n

import androidx.compose.runtime.Composable
import network.marsys.smarthome.shared.domain.entity.capability.ThermostatStatus
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.Thermostat
import network.marsys.smarthome.shared.library.resources.SmartHomeRes
import network.marsys.smarthome.shared.library.resources.entity_state_closed
import network.marsys.smarthome.shared.library.resources.entity_state_cooling
import network.marsys.smarthome.shared.library.resources.entity_state_heating
import network.marsys.smarthome.shared.library.resources.entity_state_idle
import network.marsys.smarthome.shared.library.resources.entity_state_off
import network.marsys.smarthome.shared.library.resources.entity_state_on
import network.marsys.smarthome.shared.library.resources.entity_state_open_percentage
import network.marsys.smarthome.shared.library.resources.entity_state_opened
import network.marsys.smarthome.shared.library.resources.entity_state_separator

@Composable
@Suppress("CyclomaticComplexMethod")
fun Entity.State.Descriptor.localized(): String = when (this) {
    Entity.State.Descriptor.On ->
        stringResource(SmartHomeRes.string.entity_state_on)

    Entity.State.Descriptor.Off ->
        stringResource(SmartHomeRes.string.entity_state_off)

    Entity.State.Descriptor.Open ->
        stringResource(SmartHomeRes.string.entity_state_opened)

    Entity.State.Descriptor.Closed ->
        stringResource(SmartHomeRes.string.entity_state_closed)

    is Entity.State.Descriptor.Opened ->
        stringResource(SmartHomeRes.string.entity_state_open_percentage, percentage)

    is Entity.State.Descriptor.Combined ->
        parts
            .map { it.localized() }
            .joinToString(
                separator = stringResource(SmartHomeRes.string.entity_state_separator),
            )

    is Entity.State.Descriptor.Enum<*> ->
        when (value) {
            ThermostatStatus.Status.Idle -> stringResource(SmartHomeRes.string.entity_state_idle)
            ThermostatStatus.Status.Cooling -> stringResource(SmartHomeRes.string.entity_state_cooling)
            ThermostatStatus.Status.Heating -> stringResource(SmartHomeRes.string.entity_state_heating)
            else -> value.toString()
        }

    is Entity.State.Descriptor.Value<*> ->
        value.toString()

    Entity.State.Descriptor.Unknown ->
        "-"

    Entity.State.Descriptor.Empty ->
        ""
}
