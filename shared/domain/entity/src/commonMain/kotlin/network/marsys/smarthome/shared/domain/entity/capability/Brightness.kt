package network.marsys.smarthome.shared.domain.entity.capability

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import kotlin.time.Clock
import kotlin.time.Instant

data class Brightness(
    override val current: Quantity<Dimension.Ratio>,
    override val since: Instant = Clock.System.now(),
) : WritableCapability<Quantity<Dimension.Ratio>> {
    override val descriptor: Entity.State.Descriptor
        get() = Entity.State.Descriptor.Value(current)

    override fun updateWith(
        value: Quantity<Dimension.Ratio>,
        instant: Instant,
    ): Brightness = copy(
        current = value,
        since = instant,
    )

    data class SetBrightness(
        override val identifier: EntityIdentifier,
        val brightness: Quantity<Dimension.Ratio>,
    ) : Entity.Action
}
