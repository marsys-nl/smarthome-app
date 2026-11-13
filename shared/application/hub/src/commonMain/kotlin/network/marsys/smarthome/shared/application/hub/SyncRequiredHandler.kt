package network.marsys.smarthome.shared.application.hub

import network.marsys.smarthome.shared.domain.connection.SynchronizeEntitiesUseCase

internal class SyncRequiredHandler(
    private val synchronizeEntitiesUseCase: SynchronizeEntitiesUseCase,
) : SmartHomeEventHandler.For<SmartHomeEvent.SyncRequired>(event()) {
    override suspend fun on(event: SmartHomeEvent.SyncRequired) {
        synchronizeEntitiesUseCase.invoke()
    }
}
