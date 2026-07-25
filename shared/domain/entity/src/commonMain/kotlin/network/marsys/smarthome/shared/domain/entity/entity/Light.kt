package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.area.Area
import network.marsys.smarthome.shared.domain.entity.capability.Brightness
import network.marsys.smarthome.shared.domain.entity.capability.Capability
import network.marsys.smarthome.shared.domain.entity.capability.OnOff
import network.marsys.smarthome.shared.domain.entity.capability.updateWith

data class Light(
    override val identifier: EntityIdentifier,
    override val state: State = State.Unknown,
    override val area: Area? = null,
) : AbstractEntity<Light.State>(), Entity.Activatable {
    override val active: Boolean
        get() = state is State.Known && state.onOff.value.current

    override fun copyWithState(state: State): Entity<State> =
        copy(state = state)

    sealed interface State : Entity.State {
        data class Known(
            val onOff: Capability.Required<OnOff>,
            val brightness: Capability.Optional<Brightness> = Capability.NotSupported,
        ) : State, Entity.State.Known {
            override val constraints: Set<Capability.Constraint<*>>
                get() = setOf(onOff, brightness)

            override val descriptor: Entity.State.Descriptor
                get() = Entity.State.Descriptor.Combined(
                    onOff.descriptor,
                    brightness.descriptor,
                )

            override fun with(capability: Capability<*>): Entity.State.Known =
                when (capability) {
                    is OnOff -> copy(onOff = onOff.updateWith(capability))
                    is Brightness -> copy(brightness = brightness.updateWith(capability))
                    else -> this
                }
        }

        data object Unknown : State, Entity.State.Unknown
    }
}
