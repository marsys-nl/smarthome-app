package network.marsys.smarthome.shared.domain.entity.capability

import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import kotlin.time.Clock
import kotlin.time.Instant

data class ThermostatStatus(
    override val current: Status,
    override val since: Instant = Clock.System.now(),
) : Capability<ThermostatStatus.Status> {
    override val descriptor: Entity.State.Descriptor
        get() = Entity.State.Descriptor.Enum(current)

    fun updateWith(
        mode: ThermostatMode,
        targetTemperature: TargetTemperature,
        currentTemperature: MeasureTemperature,
    ) = updateWith(
        value = compute(
            mode = mode,
            targetTemperature = targetTemperature.current,
            currentTemperature = currentTemperature.current,
        ),
        instant = listOf(mode.since, targetTemperature.since, currentTemperature.since)
            .max(),
    )

    override fun updateWith(
        value: Status,
        instant: Instant,
    ): ThermostatStatus = copy(
        current = value,
        since = instant,
    )

    sealed interface Status {
        data object Off : Status
        data object Idle : Status
        data object Heating : Status
        data object Cooling : Status
    }

    companion object {
        fun compute(
            mode: ThermostatMode,
            targetTemperature: Quantity<Dimension.Temperature>,
            currentTemperature: Quantity<Dimension.Temperature>,
        ): Status = when (mode.current) {
            ThermostatMode.Mode.Off -> Status.Off

            ThermostatMode.Mode.Heat -> when {
                currentTemperature < targetTemperature -> Status.Heating
                else -> Status.Idle
            }

            ThermostatMode.Mode.Cool -> when {
                currentTemperature > targetTemperature -> Status.Cooling
                else -> Status.Idle
            }

            ThermostatMode.Mode.Auto -> when {
                currentTemperature < targetTemperature -> Status.Heating
                currentTemperature > targetTemperature -> Status.Cooling
                else -> Status.Idle
            }
        }
    }
}
