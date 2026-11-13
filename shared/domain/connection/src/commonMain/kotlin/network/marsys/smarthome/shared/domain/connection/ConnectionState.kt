package network.marsys.smarthome.shared.domain.connection

interface ConnectionState {
    data object Disconnected : ConnectionState
    data object Connecting : ConnectionState

    sealed interface Connected : ConnectionState {
        data object Synchronizing : Connected
        data object Idle : Connected
    }

    data object Lost : ConnectionState
}
