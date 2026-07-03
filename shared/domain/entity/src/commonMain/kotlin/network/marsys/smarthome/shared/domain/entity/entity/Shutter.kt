package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.capability.Capability
import network.marsys.smarthome.shared.domain.entity.capability.Position
import network.marsys.smarthome.shared.domain.entity.capability.updateWith

data class Shutter(
    override val identifier: EntityIdentifier,
    override val state: State = State.Unknown,
) : Cover<Shutter.State>() {
    override val orientation: Orientation
        get() = Orientation.Vertical

    override fun copyWithState(state: State): Entity<State> =
        copy(state = state)

    sealed interface State : Entity.State {
        data class Known(
            val position: Capability.Required<Position>,
        ) : State, Entity.State.Known {
            override val constraints: Set<Capability.Constraint<*>>
                get() = setOf(position)

            override val descriptor: Entity.State.Descriptor
                get() = position.descriptor

            override fun with(capability: Capability<*>): Entity.State.Known =
                when (capability) {
                    is Position -> copy(position = position.updateWith(capability))
                    else -> this
                }
        }

        data object Unknown : State, Entity.State.Unknown
    }
}
