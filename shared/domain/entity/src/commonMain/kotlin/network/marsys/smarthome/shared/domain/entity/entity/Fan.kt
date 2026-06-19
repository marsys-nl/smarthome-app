package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier

data class Fan(
    override val identifier: EntityIdentifier,
    override val state: State = State.Unknown,
) : Entity<Fan.State>, Entity.Activatable, Entity.Toggleable {
    override val active: Boolean
        get() = state is State.Known && state.isOn

    override fun toggle(): Fan = when (val current = state) {
        is State.Known -> copy(
            state = current.copy(isOn = !current.isOn),
        )

        is State.Unknown -> this
    }

    sealed interface State : Entity.State {
        data class Known(
            val isOn: Boolean,
        ) : State, Entity.State.Known {
            override val descriptor: Entity.State.Descriptor
                get() = when (isOn) {
                    true -> Entity.State.Descriptor.On
                    false -> Entity.State.Descriptor.Off
                }
        }

        data object Unknown : State, Entity.State.Unknown
    }
}
