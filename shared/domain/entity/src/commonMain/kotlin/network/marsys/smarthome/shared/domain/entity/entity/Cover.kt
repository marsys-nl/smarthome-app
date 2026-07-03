package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.capability.Capability
import network.marsys.smarthome.shared.domain.entity.capability.Position
import network.marsys.smarthome.shared.domain.entity.capability.updateWith

data class Cover(
    override val identifier: EntityIdentifier,
    val type: Type,
    override val state: State = State.Unknown,
) : AbstractEntity<Cover.State>() {
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

    sealed interface Orientation {
        data object Horizontal : Orientation
        data object Vertical : Orientation
    }

    sealed interface Type {
        val orientation: Orientation
            get() = Orientation.Horizontal

        data object Blind : Type {
            override val orientation: Orientation
                get() = Orientation.Vertical
        }

        data object Curtain : Type

        data class GarageDoor(
            override val orientation: Orientation = Orientation.Vertical,
        ) : Type

        data object Gate : Type

        data object Shutter : Type {
            override val orientation: Orientation
                get() = Orientation.Vertical
        }
    }


}
