package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier

data class Speaker(
    override val identifier: EntityIdentifier,
    override val label: String,
    override val state: State = State.Unknown,
) : Entity<Speaker.State>, Entity.Activatable, Entity.Toggleable {
    override val active: Boolean
        get() = state is State.Known && state.isOn

    override fun toggle(): Speaker = when (val current = state) {
        is State.Known -> copy(
            state = current.copy(isOn = !current.isOn),
        )

        is State.Unknown -> this
    }

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
