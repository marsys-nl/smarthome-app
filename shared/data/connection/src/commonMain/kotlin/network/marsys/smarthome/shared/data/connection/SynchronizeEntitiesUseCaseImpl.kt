package network.marsys.smarthome.shared.data.connection

import network.marsys.smarthome.shared.data.entity.ports.inbound.EntitySynchronization
import network.marsys.smarthome.shared.domain.connection.SynchronizeEntitiesUseCase
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.Result.Companion.fail
import network.marsys.smarthome.shared.library.core.Result.Companion.succeed

class SynchronizeEntitiesUseCaseImpl(
    private val synchronizer: EntitySynchronization,
) : SynchronizeEntitiesUseCase {
    override suspend fun invoke(): Result<Unit, SynchronizeEntitiesUseCase.Reason> =
        when (val result = synchronizer.synchronize()) {
            is Result.Success ->
                succeed(with = Unit)

            is Result.Failure ->
                fail(
                    with = when (result.value) {
                        EntitySynchronization.Reason.Unauthenticated ->
                            SynchronizeEntitiesUseCase.Reason.Unauthenticated

                        EntitySynchronization.Reason.Unreachable ->
                            SynchronizeEntitiesUseCase.Reason.Unreachable
                    },
                )
        }
}
