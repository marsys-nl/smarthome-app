package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity

data class Light(
    override val identifier: EntityIdentifier,
    override val label: String,
    override val state: State = State.Unknown,
) : Entity<Light.State>, Entity.Activatable, Entity.Toggleable {
    override val active: Boolean
        get() = state is State.Known && state.isOn

    override fun toggle(): Light = when (val current = state) {
        is State.Known -> copy(
            state = current.copy(isOn = !current.isOn),
        )

        is State.Unknown -> this
    }

    sealed interface State : Entity.State {
        data class Known(
            val isOn: Boolean,
            val brightness: Quantity<Dimension.Ratio>? = null,
        ) : State, Entity.State.Known {
            override val description: String
                get() = buildString {
                    append(if (isOn) "On" else "Off")
                    brightness?.let {
                        append(" · ")
                        append(it)
                    }
                }
        }

        data object Unknown : State, Entity.State.Unknown
    }
}
