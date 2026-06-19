package network.marsys.smarthome.shared.domain.entity.capability

import kotlin.time.Clock
import kotlin.time.Instant

data class OnOff(
    override val current: Boolean,
    override val since: Instant = Clock.System.now(),
) : Capability<Boolean> {
    override fun updateWith(
        value: Boolean,
        instant: Instant,
    ): OnOff = copy(
        current = value,
        since = instant,
    )
}
