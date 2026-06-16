package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier

data class Camera(
    override val identifier: EntityIdentifier,
    override val state: State = State.Unknown,
) : Entity<Camera.State>, Entity.Activatable {
    override val active: Boolean
        get() = state is State.Known && state.isOn

    sealed interface State : Entity.State {
        data class Known(
            val isOn: Boolean,
        ) : State, Entity.State.Known {
            override val description: String
                get() = buildString {
                    append(if (isOn) "On" else "Off")
                }
        }

        data object Unknown : State, Entity.State.Unknown
    }
}
