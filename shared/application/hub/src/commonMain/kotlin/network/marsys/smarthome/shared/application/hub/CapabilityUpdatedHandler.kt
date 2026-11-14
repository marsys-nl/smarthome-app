package network.marsys.smarthome.shared.application.hub

import network.marsys.smarthome.domain.platform.PlatformEvent
import network.marsys.smarthome.shared.application.hub.ports.outbound.EntityCapabilityUpdater
import network.marsys.smarthome.shared.domain.connection.SynchronizeEntitiesUseCase
import network.marsys.smarthome.shared.library.core.Result

internal class CapabilityUpdatedHandler(
    private val entityCapabilityUpdater: EntityCapabilityUpdater,
    private val synchronizeEntitiesUseCase: SynchronizeEntitiesUseCase,
) : SmartHomeEventHandler.For<PlatformEvent.CapabilityUpdated>(platform()) {
    override suspend fun on(event: PlatformEvent.CapabilityUpdated) {
        val result = entityCapabilityUpdater.update(
            identifier = event.identifier,
            capability = event.capability,
        )

        when (result) {
            is Result.Failure -> recover(from = result.value)
            else -> Unit
        }
    }

    @Suppress("ktlint:standard:trailing-comma-on-declaration-site")
    private suspend fun recover(from: EntityCapabilityUpdater.Reason) {
        when (from) {
            EntityCapabilityUpdater.Reason.NotSynchronized,
            is EntityCapabilityUpdater.Reason.EntityNotFound,
            is EntityCapabilityUpdater.Reason.EntityStateUnknown ->
                synchronizeEntitiesUseCase.invoke()

            is EntityCapabilityUpdater.Reason.UnsupportedCapability ->
                Unit
        }
    }
}
