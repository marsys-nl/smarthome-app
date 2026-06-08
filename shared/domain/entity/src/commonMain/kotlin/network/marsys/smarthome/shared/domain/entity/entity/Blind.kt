package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity

data class Blind(
    override val identifier: EntityIdentifier,
    override val label: String,
    override val state: State = State.Unknown,
) : Entity<Blind.State> {
    sealed interface State : Entity.State {
        data class Known(
            val position: Quantity<Dimension.Ratio>,
        ) : State, Entity.State.Known {
            override val description: String
                get() = buildString {
                    when (position.value) {
                        0.0 -> append("Closed")
                        1.0 -> append("Open")
                        else -> append("$position open")
                    }
                }
        }

        data object Unknown : State, Entity.State.Unknown
    }
}
