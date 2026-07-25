package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.area.Area
import network.marsys.smarthome.shared.domain.entity.capability.Capability
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import network.marsys.smarthome.shared.domain.entity.capability.updateWith

data class Speaker(
    override val identifier: EntityIdentifier,
    override val state: State = State.Unknown,
    override val area: Area? = null,
) : AbstractEntity<Speaker.State>(), Entity.Activatable {
    override val active: Boolean
        get() = state is State.Known && state.onOff.value.current

    override fun copyWithState(state: State): Entity<State> =
        copy(state = state)

    sealed interface State : Entity.State {
        data class Known(
            val onOff: Capability.Required<OnOff>,
        ) : State, Entity.State.Known {
            override val constraints: Set<Capability.Constraint<*>>
                get() = setOf(onOff)

            override val descriptor: Entity.State.Descriptor
                get() = onOff.descriptor

            override fun with(capability: Capability<*>): Entity.State.Known =
                when (capability) {
                    is OnOff -> copy(onOff = onOff.updateWith(capability))
                    else -> this
                }
        }

        data object Unknown : State, Entity.State.Unknown
    }
}
