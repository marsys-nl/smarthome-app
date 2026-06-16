package network.marsys.smarthome.shared.modal.entity

import network.marsys.smarthome.shared.domain.entity.entity.Entity
import kotlin.time.Clock
import kotlin.time.Instant

sealed interface EntityDetailModalState {
    data object Loading : EntityDetailModalState

    data class Loaded(
        val entity: Entity<*>,
        val lastUpdate: Instant = Clock.System.now(),
    ) : EntityDetailModalState
}
