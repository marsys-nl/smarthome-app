package network.marsys.smarthome.shared.domain.entity.capability

import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import kotlin.time.Clock
import kotlin.time.Instant

data class Position(
    override val current: Quantity<Dimension.Ratio>,
    override val since: Instant = Clock.System.now(),
) : WritableCapability<Quantity<Dimension.Ratio>> {
    override val descriptor: Entity.State.Descriptor
        get() = when (current.value) {
            0.0 -> Entity.State.Descriptor.Closed
            1.0 -> Entity.State.Descriptor.Open
            else -> Entity.State.Descriptor.Opened(current)
        }

    override fun updateWith(
        value: Quantity<Dimension.Ratio>,
        instant: Instant,
    ): Position = copy(
        current = value,
        since = instant,
    )
}
