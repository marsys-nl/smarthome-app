package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.capability.Brightness
import network.marsys.smarthome.shared.domain.entity.capability.Capability
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import kotlin.time.Clock

data class Light(
    override val identifier: EntityIdentifier,
    override val state: State = State.Unknown,
) : Entity<Light.State>, Entity.Activatable, Entity.Toggleable {
    override val active: Boolean
        get() = state is State.Known && state.onOff.value.current

    override fun toggle(): Light = when (val current = state) {
        is State.Known -> copy(
            state = current.copy(
                onOff = current.onOff
                    .updateWith(
                        updatedValue = !current.onOff.value.current,
                        instant = Clock.System.now(),
                    ),
            ),
        )

        is State.Unknown -> this
    }

    sealed interface State : Entity.State {
        data class Known(
            val onOff: Capability.Required<OnOff>,
            val brightness: Capability.Optional<Brightness> = Capability.NotSupported,
        ) : State, Entity.State.Known {
            override val descriptor: Entity.State.Descriptor
                get() = Entity.State.Descriptor.Combined(
                    onOff.descriptor,
                    brightness.descriptor,
                )
        }

        data object Unknown : State, Entity.State.Unknown
    }
}
