package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.capability.Capability
import network.marsys.smarthome.shared.domain.entity.capability.Movement
import network.marsys.smarthome.shared.domain.entity.capability.Opened
import network.marsys.smarthome.shared.domain.entity.capability.Position
import network.marsys.smarthome.shared.domain.entity.zone.Zone

data class Curtain(
    override val identifier: EntityIdentifier,
    override val state: State = State.Unknown,
    override val zone: Zone? = null,
) : Cover<Curtain.State>() {
    override val orientation: Orientation
        get() = Orientation.Horizontal

    override fun copyWithState(state: State): Entity<State> =
        copy(state = state)

    sealed interface State : Entity.State {
        data class Known(
            override val control: Control,
        ) : State, Cover.State {
            override fun with(capability: Capability<*>): Entity.State.Known =
                when (capability) {
                    is Movement -> copy(control = control.with(capability))
                    is Opened -> copy(control = control.with(capability))
                    is Position -> copy(control = control.with(capability))
                    else -> this
                }
        }

        data object Unknown : State, Entity.State.Unknown
    }
}
