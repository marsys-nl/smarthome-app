package network.marsys.smarthome.shared.modal.entity

import androidx.compose.runtime.Stable
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.shared.domain.entity.entity.Entity

@Stable
interface EntityDetailModalState {
    val isLoading: Boolean
    val entity: Entity<*>?
}

sealed class EntityDetailModalAction(val key: String) {
    data class ToggleEntity(
        val entity: EntityIdentifier,
        val state: Boolean,
    ) : EntityDetailModalAction("ToggleEntityState[$entity]")

    data class AdjustBrightness(
        val entity: EntityIdentifier,
        val brightness: Quantity<Dimension.Ratio>,
    ) : EntityDetailModalAction("AdjustBrightness[$entity]")
}
