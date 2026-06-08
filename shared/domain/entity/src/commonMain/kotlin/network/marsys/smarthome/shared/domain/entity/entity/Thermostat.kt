package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity

data class Thermostat(
    override val identifier: EntityIdentifier,
    override val label: String,
    override val state: State = State.Unknown,
) : Entity<Thermostat.State>, Entity.Activatable {
    override val active: Boolean
        get() = state is State.Known && state.isOn

    sealed interface State : Entity.State {
        data class Known(
            val mode: ThermostatMode,
            val temperatures: Temperatures,
        ) : State, Entity.State.Known {
            override val description: String
                get() = buildString {
                    if (status != Status.Idle) {
                        append(status)
                        append(" · ")
                        append(temperatures.target)
                    } else {
                        append(temperatures.current)
                    }
                }

            val isOn: Boolean
                get() = status != Status.Idle

            val status: Status
                get() = when (mode) {
                    ThermostatMode.Off -> Status.Idle

                    ThermostatMode.Heat -> when {
                        temperatures.current < temperatures.target -> Status.Heating
                        else -> Status.Idle
                    }

                    ThermostatMode.Cool -> when {
                        temperatures.current > temperatures.target -> Status.Cooling
                        else -> Status.Idle
                    }

                    ThermostatMode.Auto -> when {
                        temperatures.current < temperatures.target -> Status.Heating
                        temperatures.current > temperatures.target -> Status.Cooling
                        else -> Status.Idle
                    }
                }
        }

        data object Unknown : State, Entity.State.Unknown
    }

    sealed interface Status {
        data object Idle : Status
        data object Heating : Status
        data object Cooling : Status
    }

    data class Temperatures(
        val target: Quantity<Dimension.Temperature>,
        val current: Quantity<Dimension.Temperature>,
    )

    enum class ThermostatMode {
        Off,
        Heat,
        Cool,
        Auto,
    }
}
