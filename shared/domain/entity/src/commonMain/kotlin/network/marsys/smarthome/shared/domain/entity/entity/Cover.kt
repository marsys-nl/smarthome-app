package network.marsys.smarthome.shared.domain.entity.entity

import network.marsys.smarthome.domain.unit.percent
import network.marsys.smarthome.shared.domain.entity.capability.Capability
import network.marsys.smarthome.shared.domain.entity.capability.Movement
import network.marsys.smarthome.shared.domain.entity.capability.Opened
import network.marsys.smarthome.shared.domain.entity.capability.Position
import network.marsys.smarthome.shared.domain.entity.capability.updateWith

abstract class Cover<S : Entity.State> : AbstractEntity<S>() {
    abstract val orientation: Orientation

    sealed interface Orientation {
        data object Horizontal : Orientation
        data object Vertical : Orientation
    }

    interface State : Entity.State.Known {
        val control: Control

        override val constraints: Set<Capability.Constraint<*>>
            get() = control.constraints

        override val descriptor: Entity.State.Descriptor
            get() = control.descriptor
    }

    sealed interface Control {
        val constraints: Set<Capability.Constraint<*>>
            get() = setOf(movement, opened, position)

        val descriptor: Entity.State.Descriptor

        val movement: Capability.Constraint<Movement>

        val opened: Capability.Present<Opened>

        val position: Capability.Constraint<Position>
            get() = Capability.NotSupported

        fun with(capability: Capability<*>): Control

        data class WithPosition(
            override val position: Capability.Required<Position>,
            override val movement: Capability.Optional<Movement> = Capability.NotSupported,
        ) : Control {
            override val descriptor: Entity.State.Descriptor
                get() = when {
                    movement is Capability.Present<*> && movement.value.current != Movement.Direction.Idle ->
                        movement.descriptor

                    else -> position.descriptor
                }

            override val opened: Capability.Computed<Opened, WithPosition, Boolean>
                get() = computeOpened.compute(state = this)

            private val computeOpened: Capability.Computed<Opened, WithPosition, Boolean>
                get() = Capability.Computed(
                    value = Opened(false),
                    compute = { state ->
                        updateWith(
                            value = state.position.value.current > 0.percent,
                            instant = state.position.value.since,
                        )
                    },
                )

            override fun with(capability: Capability<*>): Control =
                when (capability) {
                    is Position -> copy(position = position.updateWith(capability))
                    else -> this
                }
        }

        data class WithoutPosition(
            override val opened: Capability.Required<Opened>,
            override val movement: Capability.Optional<Movement> = Capability.NotSupported,
        ) : Control {
            override val descriptor: Entity.State.Descriptor
                get() = when {
                    movement is Capability.Present<*> && movement.value.current != Movement.Direction.Idle ->
                        movement.descriptor

                    else -> opened.descriptor
                }

            override fun with(capability: Capability<*>): Control =
                when (capability) {
                    is Opened -> copy(opened = opened.updateWith(capability))
                    else -> this
                }
        }
    }
}
