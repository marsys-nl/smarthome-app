package network.marsys.smarthome.shared.application.hub

import network.marsys.smarthome.shared.domain.connection.ObserveConnectionUseCase
import network.marsys.smarthome.shared.library.core.Result.Companion.succeed

internal class ObserveConnectionUseCaseImpl(
    private val session: SmartHomeSession,
) : ObserveConnectionUseCase {
    override fun invoke() = session.state
}
