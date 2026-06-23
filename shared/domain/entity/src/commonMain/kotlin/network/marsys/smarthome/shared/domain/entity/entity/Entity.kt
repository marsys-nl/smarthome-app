package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.shared.domain.entity.capability.Capability

interface Entity<S : Entity.State> {
    val identifier: EntityIdentifier
    val state: S

    val descriptor: State.Descriptor
        get() = state.descriptor

    sealed interface Action {
        val identifier: EntityIdentifier
    }

    sealed interface State {
        val descriptor: Descriptor

        sealed interface Known : State {
            val constraints: Set<Capability.Constraint<*>>
            val capabilities: Set<Capability<*>>
                get() = constraints
                    .filterIsInstance<Capability.Present<*>>()
                    .map { it.value }
                    .toSet()
        }

        sealed interface Unknown : State {
            override val descriptor: Descriptor
                get() = Descriptor.Unknown
        }

        sealed interface Descriptor {
            data object Empty : Descriptor
            data object Unknown : Descriptor
            data object On : Descriptor
            data object Off : Descriptor

            data object Open : Descriptor
            data object Closed : Descriptor
            data class Opened(
                val percentage: Quantity<Dimension.Ratio>,
            ) : Descriptor

            data class Value<T : Dimension>(
                val value: Quantity<T>,
            ) : Descriptor

            data class Enum<T : Any>(
                val value: T,
            ) : Descriptor

            @ConsistentCopyVisibility
            data class Combined private constructor(
                val parts: Collection<Descriptor>,
            ) : Descriptor {
                constructor(vararg parts: Descriptor?) : this(
                    parts = parts
                        .filterNotNull()
                        .filterNot { it is Empty }
                        .toList(),
                )
            }
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

    interface Dimmable {
        fun dim(brightness: Quantity<Dimension.Ratio>): Entity<*>

        data class SetBrightness(
            override val identifier: EntityIdentifier,
            val brightness: Quantity<Dimension.Ratio>,
        ) : Action
    }
}

inline fun <reified C : Capability<*>> Entity.State.Known.get(): C? =
    capabilities.firstOrNull { it is C } as? C
