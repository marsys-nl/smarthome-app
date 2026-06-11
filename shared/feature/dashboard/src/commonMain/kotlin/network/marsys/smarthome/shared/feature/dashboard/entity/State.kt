package network.marsys.smarthome.shared.feature.dashboard.entity

import androidx.compose.runtime.Stable
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.domain.entity.entity.Entity

@Stable
interface State {
    val quickControlState: QuickControlState
    val user: String

    @Stable
    interface QuickControlState {
        val entities: Map<EntityIdentifier, Entity<*>>
        val groupedEntitiesByType: Boolean
    }
}

sealed class Action(val key: String) {
    data object ChangeAppAppearance : Action("ChangeAppAppearance")

    data class ToggleEntityState(
        val entity: EntityIdentifier,
    ) : Action("ToggleEntityState[$entity]")

    data object ToggleGroupEntitiesByType : Action("ToggleGroupEntitiesByType")
}

sealed interface Effect {
    data object OpenAppearanceModal : Effect
}
