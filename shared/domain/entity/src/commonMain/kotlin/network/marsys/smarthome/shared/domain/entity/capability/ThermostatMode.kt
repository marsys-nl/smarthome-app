package network.marsys.smarthome.shared.domain.entity.capability

import kotlin.time.Clock
import kotlin.time.Instant

data class ThermostatMode(
    override val current: Mode,
    override val since: Instant = Clock.System.now(),
    private val supported: Set<Mode> = Mode.entries.toSet(),
) : WritableCapability<ThermostatMode.Mode> {
    init {
        require(current in supported) {
            "Current mode '$current' is not supported. Supported modes are: $supported."
        }
    }

    override fun updateWith(
        value: Mode,
        instant: Instant,
    ): ThermostatMode = copy(
        current = value,
        since = instant,
    )

    enum class Mode {
        Heat,
        Cool,
        Auto,
    }
}
