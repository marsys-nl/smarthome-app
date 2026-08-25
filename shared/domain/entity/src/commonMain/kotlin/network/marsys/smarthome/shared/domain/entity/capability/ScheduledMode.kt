package network.marsys.smarthome.shared.domain.entity.capability

import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import kotlin.time.Clock
import kotlin.time.Instant

data class ScheduledMode(
    override val current: Boolean,
    override val since: Instant = Clock.System.now(),
) : WritableCapability<Boolean> {
    override val descriptor: Entity.State.Descriptor = when (current) {
        true -> Entity.State.Descriptor.On
        false -> Entity.State.Descriptor.Off
    }

    override fun updateWith(
        value: Boolean,
        instant: Instant,
    ): ScheduledMode = copy(
        current = value,
        since = instant,
    )

    sealed interface Toggle : Entity.Action {
        data class On(
            override val identifier: EntityIdentifier,
        ) : Toggle

        data class Off(
            override val identifier: EntityIdentifier,
        ) : Toggle
    }
}
