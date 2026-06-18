package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier

interface Entity<S : Entity.State> {
    val identifier: EntityIdentifier
    val state: S

    val description: String
        get() = state.description

    sealed interface Action {
        val identifier: EntityIdentifier
    }

    sealed interface State {
        val description: String

        sealed interface Known : State {
            override val description: String
                get() = "On"
        }

        sealed interface Unknown : State {
            override val description: String
                get() = "—"
        }
    }

    interface Activatable {
        val active: Boolean
    }

    interface Toggleable {
        fun toggle(): Entity<*>

        sealed interface Toggle : Action {
            data class On(
                override val identifier: EntityIdentifier,
            ) : Toggle

            data class Off(
                override val identifier: EntityIdentifier,
            ) : Toggle
        }
    }
}
