package network.marsys.smarthome.shared.modal.entity

import androidx.compose.runtime.Stable
import network.marsys.smarthome.shared.domain.entity.entity.Entity

@Stable
interface EntityDetailModalState {
    val isLoading: Boolean
    val entity: Entity<*>?
}

sealed interface EntityDetailModalAction {
    //
}
