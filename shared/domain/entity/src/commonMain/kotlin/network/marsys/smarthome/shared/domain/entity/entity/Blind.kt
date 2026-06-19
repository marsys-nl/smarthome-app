package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity

data class Blind(
    override val identifier: EntityIdentifier,
    override val state: State = State.Unknown,
) : Entity<Blind.State> {
    sealed interface State : Entity.State {
        data class Known(
            val position: Quantity<Dimension.Ratio>,
        ) : State, Entity.State.Known {
            override val descriptor: Entity.State.Descriptor
                get() = when (position.value) {
                    0.0 -> Entity.State.Descriptor.Closed
                    1.0 -> Entity.State.Descriptor.Open
                    else -> Entity.State.Descriptor.Opened(position)
                }
        }

        data object Unknown : State, Entity.State.Unknown
    }
}
