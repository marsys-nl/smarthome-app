package network.marsys.smarthome.shared.application.hub

import network.marsys.smarthome.shared.domain.connection.EstablishConnectionUseCase
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.mapFailure

internal class EstablishConnectionUseCaseImpl(
    private val session: SmartHomeSession,
) : EstablishConnectionUseCase {
    override suspend fun invoke(): Result<Unit, EstablishConnectionUseCase.Reason> =
        session.connect().mapFailure {
            when (it) {
                SmartHomeSession.Reason.Unreachable -> EstablishConnectionUseCase.Reason.Unreachable
                SmartHomeSession.Reason.Unauthorized -> EstablishConnectionUseCase.Reason.Unauthorized
                SmartHomeSession.Reason.Timeout -> EstablishConnectionUseCase.Reason.Timeout
                SmartHomeSession.Reason.Unknown -> EstablishConnectionUseCase.Reason.Unknown
            }
        }
}
