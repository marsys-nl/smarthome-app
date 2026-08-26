package network.marsys.smarthome.shared.modal.entity

import androidx.compose.runtime.Stable
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.shared.domain.entity.entity.Entity

@Stable
interface EntityDetailModalState {
    val isLoading: Boolean
    val entity: Entity<*>?
}

sealed class EntityDetailModalAction(val key: String) {
    data class AdjustBrightness(
        val entity: EntityIdentifier,
        val brightness: Quantity<Dimension.Ratio>,
    ) : EntityDetailModalAction("AdjustBrightness[$entity]")

    data class AdjustTargetTemperature(
        val entity: EntityIdentifier,
        val temperature: Quantity<Dimension.Temperature>,
    ) : EntityDetailModalAction("AdjustTargetTemperature[$entity]")

    sealed class MoveCover(key: String) : EntityDetailModalAction(key) {
        data class Open(
            val entity: EntityIdentifier,
        ) : MoveCover("MoveCover.Open[$entity]")

        data class Close(
            val entity: EntityIdentifier,
        ) : MoveCover("MoveCover.Close[$entity]")

        data class Stop(
            val entity: EntityIdentifier,
        ) : MoveCover("MoveCover.Stop[$entity]")
    }

    data class ToggleChildLock(
        val entity: EntityIdentifier,
        val state: Boolean,
    ) : EntityDetailModalAction("ToggleChildLock[$entity]")

    data class ToggleEntity(
        val entity: EntityIdentifier,
        val state: Boolean,
    ) : EntityDetailModalAction("ToggleEntityState[$entity]")

    data class ToggleScheduledMode(
        val entity: EntityIdentifier,
        val state: Boolean,
    ) : EntityDetailModalAction("ToggleScheduledMode[$entity]")

    data class ToggleWindowDetection(
        val entity: EntityIdentifier,
        val state: Boolean,
    ) : EntityDetailModalAction("ToggleWindowDetection[$entity]")
}
