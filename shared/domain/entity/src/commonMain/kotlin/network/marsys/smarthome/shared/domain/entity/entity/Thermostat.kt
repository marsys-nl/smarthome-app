package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.capability.Capability
import network.marsys.smarthome.shared.domain.entity.capability.MeasureTemperature
import network.marsys.smarthome.shared.domain.entity.capability.TargetTemperature
import network.marsys.smarthome.shared.domain.entity.capability.ThermostatMode
import network.marsys.smarthome.shared.domain.entity.capability.ThermostatStatus
import network.marsys.smarthome.shared.domain.entity.capability.WritableCapability
import network.marsys.smarthome.shared.domain.entity.capability.updateWith

data class Thermostat(
    override val identifier: EntityIdentifier,
    override val state: State = State.Unknown,
) : AbstractEntity<Thermostat.State>(), Entity.Activatable {
    override val active: Boolean
        get() = state is State.Known && state.status.value.current !is ThermostatStatus.Status.Idle

    override fun copyWithState(state: State): Entity<State> =
        copy(state = state)

    sealed interface State : Entity.State {
        data class Known(
            val mode: Capability.Required<ThermostatMode>,
            val temperatures: Temperatures,
        ) : State, Entity.State.Known {
            override val constraints: Set<Capability.Constraint<*>>
                get() = setOf(
                    mode,
                    temperatures.target,
                    temperatures.current,
                    temperatures.outdoor,
                    status,
                )

            val status: Capability.Computed<ThermostatStatus, Known, ThermostatStatus.Status>
                get() = computeStatus.compute(state = this)

            override val descriptor: Entity.State.Descriptor
                get() = when (status.value.current) {
                    ThermostatStatus.Status.Idle -> temperatures.current.descriptor

                    else -> Entity.State.Descriptor.Combined(
                        status.descriptor,
                        temperatures.target.descriptor,
                    )
                }

            private val computeStatus =
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

            override fun with(capability: WritableCapability<*>): Entity.State.Known =
                when (capability) {
                    is ThermostatMode -> copy(mode = mode.updateWith(capability))

                    is TargetTemperature -> copy(
                        temperatures = temperatures.copy(
                            target = temperatures.target.updateWith(capability),
                        ),
                    )

                    else -> this
                }
        }

        data object Unknown : State, Entity.State.Unknown
    }

    data class Temperatures(
        val target: Capability.Required<TargetTemperature>,
        val current: Capability.Required<MeasureTemperature>,
        val outdoor: Capability.Optional<MeasureTemperature> = Capability.NotSupported,
    )
}
