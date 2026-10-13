package network.marsys.smarthome.shared.data.connection

import network.marsys.smarthome.shared.domain.connection.SynchronizeEntitiesUseCase
import network.marsys.smarthome.shared.domain.entity.EntityRepository
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.mapFailure

class SynchronizeEntitiesUseCaseImpl(
    private val repository: EntityRepository,
) : SynchronizeEntitiesUseCase {
    override suspend fun invoke(): Result<Unit, SynchronizeEntitiesUseCase.Reason> =
        repository.synchronize()
            .mapFailure { reason ->
                when (reason) {
                    is EntityRepository.Reason.InvalidData ->
                        SynchronizeEntitiesUseCase.Reason.InvalidData

                    is EntityRepository.Reason.Unauthenticated ->
                        SynchronizeEntitiesUseCase.Reason.Unauthenticated

                    is EntityRepository.Reason.Unreachable ->
                        SynchronizeEntitiesUseCase.Reason.Unreachable
                }
            }
}
