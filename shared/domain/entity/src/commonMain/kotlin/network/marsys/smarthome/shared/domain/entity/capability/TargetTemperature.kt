package network.marsys.smarthome.shared.domain.entity.capability

import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import kotlin.time.Clock
import kotlin.time.Instant

data class TargetTemperature(
    override val current: Quantity<Dimension.Temperature>,
    override val since: Instant = Clock.System.now(),
    val range: ClosedRange<Quantity<Dimension.Temperature>> = defaultTemperatureRange,
) : WritableCapability<Quantity<Dimension.Temperature>> {
    override val descriptor: Entity.State.Descriptor
        get() = Entity.State.Descriptor.Value(current)

    init {
        require(current in range) {
            "Current temperature $current is out of range. Given range: $range."
        }
    }

    override fun updateWith(
        value: Quantity<Dimension.Temperature>,
        instant: Instant,
    ): TargetTemperature = copy(
        current = value,
        since = instant,
    )

    data class SetTargetTemperature(
        override val identifier: EntityIdentifier,
        val targetTemperature: Quantity<Dimension.Temperature>,
    ) : Entity.Action

    companion object {
        private val MIN_TARGET_TEMPERATURE = 5.celsius
        private val MAX_TARGET_TEMPERATURE = 35.celsius

        private val defaultTemperatureRange = MIN_TARGET_TEMPERATURE..MAX_TARGET_TEMPERATURE
    }
}
