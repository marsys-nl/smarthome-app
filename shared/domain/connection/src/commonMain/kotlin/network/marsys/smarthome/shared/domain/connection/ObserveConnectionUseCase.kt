package network.marsys.smarthome.shared.domain.connection

import kotlinx.coroutines.flow.StateFlow
import network.marsys.smarthome.shared.library.core.Result

fun interface ObserveConnectionUseCase {
    operator fun invoke(): StateFlow<ConnectionState>
}
