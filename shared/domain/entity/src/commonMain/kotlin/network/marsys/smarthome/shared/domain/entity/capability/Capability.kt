package network.marsys.smarthome.shared.domain.entity.capability

import network.marsys.smarthome.shared.domain.entity.entity.Entity
import kotlin.time.Instant

interface Capability<T> {
    val current: T
    val since: Instant

    val descriptor: Entity.State.Descriptor
        get() = Entity.State.Descriptor.Empty

    fun updateWith(
        value: T,
        instant: Instant,
    ): Capability<T>

    sealed interface Constraint<out T : Capability<*>> {
        val descriptor: Entity.State.Descriptor?
            get() = null
    }

    sealed interface Present<out T : Capability<*>> : Constraint<T> {
        val value: T

        override val descriptor: Entity.State.Descriptor
            get() = value.descriptor
    }

    data class Computed<C : Capability<T>, S : Entity.State.Known, T>(
        override val value: C,
        internal val compute: C.(S) -> C,
    ) : Present<C>, Constraint<C> {
        fun copy(state: S) = copy(
            value = compute.invoke(value, state),
        )
    }

    data class Required<T : Capability<*>>(
        override val value: T,
    ) : Present<T>, Constraint<T> {
        inline fun <reified V, reified X : Capability<V>> updateWith(
            updatedValue: V,
            instant: Instant,
        ): Required<T> {
            val updatedValue = updatedValue ?: return copy()

            require(value.current is V) {
                mismatchTypeError(
                    expected = value.current::class.simpleName,
                    actual = updatedValue::class.simpleName,
                )
            }

            @Suppress("UNCHECKED_CAST")
            return copy(
                value = (value as Capability<V>)
                    .updateWith(
                        value = updatedValue,
                        instant = instant,
                    ) as T,
            )
        }
    }

    sealed class Optional<out T : Capability<*>> : Constraint<T> {
        inline fun <reified V, reified X : Capability<V>> updateWith(
            updatedValue: V,
            instant: Instant,
        ): Optional<T> = when (this) {
            is NotSupported -> NotSupported

            is Available<T> -> updateValueWith(
                updatedValue = updatedValue,
                instant = instant,
            )
        }
    }

    data object NotSupported : Optional<Nothing>()

    data class Available<T : Capability<*>>(
        override val value: T,
    ) : Optional<T>(), Present<T> {
        @PublishedApi
        internal inline fun <reified V> updateValueWith(
            updatedValue: V,
            instant: Instant,
        ): Available<T> {
            val updatedValue = updatedValue ?: return copy()

            require(value.current is V) {
                mismatchTypeError(
                    expected = value.current::class.simpleName,
                    actual = updatedValue::class.simpleName,
                )
            }

            @Suppress("UNCHECKED_CAST")
            return copy(
                value = (value as Capability<V>)
                    .updateWith(
                        value = updatedValue,
                        instant = instant,
                    ) as T,
            )
        }
    }

    companion object {
        fun <T : Capability<*>> required(value: T) = Required(value)
        fun <T : Capability<*>> optional(value: T?) = when (value) {
            null -> NotSupported
            else -> Available(value)
        }

        @PublishedApi
        internal fun mismatchTypeError(
            expected: String?,
            actual: String?,
        ) = "Mismatched type: '$expected' expected, but '$actual' provided."
    }
}
