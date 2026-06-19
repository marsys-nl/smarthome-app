package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.capability.Capability
import network.marsys.smarthome.shared.domain.entity.capability.OnOff

data class Camera(
    override val identifier: EntityIdentifier,
    override val state: State = State.Unknown,
) : Entity<Camera.State>, Entity.Activatable {
    override val active: Boolean
        get() = state is State.Known && state.onOff.value.current

    sealed interface State : Entity.State {
        data class Known(
            val onOff: Capability.Required<OnOff>,
        ) : State, Entity.State.Known {
            override val descriptor: Entity.State.Descriptor
                get() = onOff.descriptor
        }

        data object Unknown : State, Entity.State.Unknown
    }
}
