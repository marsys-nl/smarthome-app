package network.marsys.smarthome.shared.domain.entity.capability

import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import kotlin.time.Clock
import kotlin.time.Instant

data class MeasureTemperature(
    override val current: Quantity<Dimension.Temperature>,
    override val since: Instant = Clock.System.now(),
) : Capability<Quantity<Dimension.Temperature>> {
    override val descriptor: Entity.State.Descriptor
        get() = Entity.State.Descriptor.Value(current)

    override fun updateWith(
        value: Quantity<Dimension.Temperature>,
        instant: Instant,
    ): MeasureTemperature = copy(
        current = value,
        since = instant,
    )
}
