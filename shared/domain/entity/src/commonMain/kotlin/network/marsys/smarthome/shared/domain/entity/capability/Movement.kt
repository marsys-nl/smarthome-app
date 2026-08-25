package network.marsys.smarthome.shared.domain.entity.capability

import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import kotlin.time.Clock
import kotlin.time.Instant

data class Movement(
    override val current: Direction,
    override val since: Instant = Clock.System.now(),
) : WritableCapability<Movement.Direction> {
    override val descriptor: Entity.State.Descriptor
        get() = when (current) {
            Direction.Idle -> Entity.State.Descriptor.Empty
            Direction.Closing -> Entity.State.Descriptor.Closing
            Direction.Opening -> Entity.State.Descriptor.Opening
        }

    override fun updateWith(
        value: Direction,
        instant: Instant,
    ): Movement = copy(
        current = value,
        since = instant,
    )

    enum class Direction {
        Idle,
        Opening,
        Closing,
    }

    sealed interface Move : Entity.Action {
        data class Open(
            override val identifier: EntityIdentifier,
        ) : Move

        data class Close(
            override val identifier: EntityIdentifier,
        ) : Move

        data class Stop(
            override val identifier: EntityIdentifier,
        ) : Move
    }
}
