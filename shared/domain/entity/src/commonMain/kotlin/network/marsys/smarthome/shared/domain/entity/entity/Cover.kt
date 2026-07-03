package network.marsys.smarthome.shared.domain.entity.entity

abstract class Cover<S : Entity.State> : AbstractEntity<S>() {
    abstract val orientation: Orientation

    sealed interface Orientation {
        data object Horizontal : Orientation
        data object Vertical : Orientation
    }
}
