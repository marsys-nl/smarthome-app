package network.marsys.smarthome.shared.domain.entity.capability

import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity
import kotlin.time.Clock
import kotlin.time.Instant

data class Brightness(
    override val current: Quantity<Dimension.Ratio>,
    override val since: Instant = Clock.System.now(),
) : Capability<Quantity<Dimension.Ratio>> {
    override fun updateWith(
        value: Quantity<Dimension.Ratio>,
        instant: Instant,
    ): Brightness = copy(
        current = value,
        since = instant,
    )
}
