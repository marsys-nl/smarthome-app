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

    fun update(transform: (State.Known) -> State.Known): Entity<S>

    interface Action {
        val identifier: EntityIdentifier
    }

    sealed interface State {
        val descriptor: Descriptor

        fun with(capability: Capability<*>): State =
            when (this) {
                is Known -> with(capability)
                is Unknown -> this
            }

        sealed interface Known : State {
            val constraints: Set<Capability.Constraint<*>>
            val capabilities: Set<Capability<*>>
                get() = constraints
                    .filterIsInstance<Capability.Present<*>>()
                    .map { it.value }
                    .toSet()

            override fun with(capability: Capability<*>): Known
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

            data object Closed : Descriptor
            data object Closing : Descriptor
            data object Open : Descriptor
            data class Opened(
                val percentage: Quantity<Dimension.Ratio>,
            ) : Descriptor
            data object Opening : Descriptor

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
}

abstract class AbstractEntity<S : Entity.State> : Entity<S> {
    protected abstract fun copyWithState(state: S): Entity<S>

    @Suppress("UNCHECKED_CAST")
    override fun update(transform: (Entity.State.Known) -> Entity.State.Known): Entity<S> =
        when (val current = state) {
            is Entity.State.Known -> copyWithState(transform(current) as S)
            is Entity.State.Unknown -> this
        }
}

inline fun <reified C : Capability<*>> Entity<*>.get(
    predicate: (C) -> Boolean = { true },
): C? =
    when (val current = state) {
        is Entity.State.Known ->
            current.capabilities
                .filterIsInstance<C>()
                .firstOrNull(predicate)

        else -> null
    }
