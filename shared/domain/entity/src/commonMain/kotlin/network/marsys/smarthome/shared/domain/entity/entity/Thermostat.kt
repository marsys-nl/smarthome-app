package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.shared.domain.entity.capability.Capability
import network.marsys.smarthome.shared.domain.entity.capability.MeasureTemperature
import network.marsys.smarthome.shared.domain.entity.capability.TargetTemperature
import network.marsys.smarthome.shared.domain.entity.capability.ThermostatMode
import network.marsys.smarthome.shared.domain.entity.capability.ThermostatStatus

data class Thermostat(
    override val identifier: EntityIdentifier,
    override val state: State = State.Unknown,
) : Entity<Thermostat.State>, Entity.Activatable {
    override val active: Boolean
        get() = state is State.Known && state.status.value.current !is ThermostatStatus.Status.Idle

    sealed interface State : Entity.State {
        data class Known(
            val mode: Capability.Required<ThermostatMode>,
            val temperatures: Temperatures,
        ) : State, Entity.State.Known {
            val status: Capability.Computed<ThermostatStatus, Known, ThermostatStatus.Status>
                get() = computedStatus.copy(state = this)

            override val descriptor: Entity.State.Descriptor
                get() = when (status.value.current) {
                    ThermostatStatus.Status.Idle -> temperatures.current.descriptor

                    else -> Entity.State.Descriptor.Combined(
                        status.descriptor,
                        temperatures.target.descriptor,
                    )
                }

            private val computedStatus =
                Capability.Computed<ThermostatStatus, Known, ThermostatStatus.Status>(
                    value = ThermostatStatus(ThermostatStatus.Status.Idle),
                    compute = { state ->
                        updateWith(
                            mode = state.mode.value,
                            targetTemperature = state.temperatures.target.value,
                            currentTemperature = state.temperatures.current.value,
                        )
                    },
                )
        }

        data object Unknown : State, Entity.State.Unknown
    }

    data class Temperatures(
        val target: Capability.Required<TargetTemperature>,
        val current: Capability.Required<MeasureTemperature>,
        val outdoor: Capability.Optional<MeasureTemperature> = Capability.NotSupported,
    )
}
