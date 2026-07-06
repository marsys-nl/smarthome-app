package network.marsys.smarthome.shared.domain.entity.capability

import network.marsys.smarthome.shared.domain.entity.entity.Entity
import kotlin.time.Clock
import kotlin.time.Instant

data class Opened(
    override val current: Boolean,
    override val since: Instant = Clock.System.now(),
) : WritableCapability<Boolean> {
    override val descriptor: Entity.State.Descriptor = when (current) {
        true -> Entity.State.Descriptor.Open
        false -> Entity.State.Descriptor.Closed
    }

    override fun updateWith(
        value: Boolean,
        instant: Instant,
    ): Opened = copy(
        current = value,
        since = instant,
    )
}
