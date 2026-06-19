package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.shared.domain.entity.capability.Capability
import network.marsys.smarthome.shared.domain.entity.capability.Position

data class Blind(
    override val identifier: EntityIdentifier,
    override val state: State = State.Unknown,
) : Entity<Blind.State> {
    sealed interface State : Entity.State {
        data class Known(
            val position: Capability.Required<Position>,
        ) : State, Entity.State.Known {
            override val descriptor: Entity.State.Descriptor
                get() = position.descriptor
        }

        data object Unknown : State, Entity.State.Unknown
    }
}
