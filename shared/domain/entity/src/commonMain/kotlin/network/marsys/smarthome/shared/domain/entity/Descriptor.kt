package network.marsys.smarthome.shared.domain.entity

import network.marsys.smarthome.domain.capability.Brightness
import network.marsys.smarthome.domain.capability.Capability
import network.marsys.smarthome.domain.capability.ChildLock
import network.marsys.smarthome.domain.capability.MeasureDataSize
import network.marsys.smarthome.domain.capability.MeasureLoad
import network.marsys.smarthome.domain.capability.MeasureTemperature
import network.marsys.smarthome.domain.capability.Movement
import network.marsys.smarthome.domain.capability.Movement.Direction
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.capability.Opened
import network.marsys.smarthome.domain.capability.Position
import network.marsys.smarthome.domain.capability.ScheduledMode
import network.marsys.smarthome.domain.capability.TargetTemperature
import network.marsys.smarthome.domain.capability.ThermostatMode
import network.marsys.smarthome.domain.capability.ThermostatStatus
import network.marsys.smarthome.domain.capability.WindowDetection
import network.marsys.smarthome.domain.entity.Camera
import network.marsys.smarthome.domain.entity.Cover
import network.marsys.smarthome.domain.entity.Entity
import network.marsys.smarthome.domain.entity.Fan
import network.marsys.smarthome.domain.entity.Light
import network.marsys.smarthome.domain.entity.SmartLock
import network.marsys.smarthome.domain.entity.SmartPlug
import network.marsys.smarthome.domain.entity.Speaker
import network.marsys.smarthome.domain.entity.System
import network.marsys.smarthome.domain.entity.Thermostat
import network.marsys.smarthome.domain.unit.Dimension
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.domain.unit.Rounding
import network.marsys.smarthome.domain.unit.Scale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.Instant

sealed interface Descriptor {
    data object Empty : Descriptor
    data object Unknown : Descriptor
    data object On : Descriptor
    data object Off : Descriptor

    data object Closed : Descriptor
    data object Closing : Descriptor
    data object Open : Descriptor
    data class Opened(
        val percentage: Quantity<Dimension.Ratio>,
    ) : Descriptor
    data object Opening : Descriptor

    data class Value<T : Dimension>(
        val value: Quantity<T>,
    ) : Descriptor

    data class FormattedValue<T : Dimension>(
        val value: Quantity<T>,
        val scale: Scale<T> = value.unit.scale,
        val rounding: Rounding = value.unit.rounding,
    ) : Descriptor

    data class Date(
        val value: Instant,
    ) : Descriptor

    data class TimeAgo(
        val value: Instant,
        val strategy: TimeAgoStrategy = AdaptiveUnits(),
    ) : Descriptor

    data class Enum<T : Any>(
        val value: T,
    ) : Descriptor

    @ConsistentCopyVisibility
    data class Combined private constructor(
        val parts: Collection<Descriptor>,
    ) : Descriptor {
        constructor(vararg parts: Descriptor?) : this(
            parts = parts
                .filterNotNull()
                .filterNot { it is Empty }
                .toList(),
        )
    }
}

/**
 * Time ago strategy.
 */

sealed interface TimeAgoDisplay {
    data class Components(
        val units: List<DurationUnit>,
    ) : TimeAgoDisplay {
        constructor(vararg units: DurationUnit) : this(units = units.toList())
    }

    data object JustNow : TimeAgoDisplay
}

interface TimeAgoStrategy {
    fun displayFor(duration: Duration): TimeAgoDisplay
}

data class FixedUnits(
    private val units: List<DurationUnit>,
) : TimeAgoStrategy {
    override fun displayFor(duration: Duration): TimeAgoDisplay =
        TimeAgoDisplay.Components(units = units)
}

sealed interface AgeRule {
    val display: TimeAgoDisplay

    fun matches(age: Duration): Boolean

    data class AtLeast(
        val threshold: Duration,
        override val display: TimeAgoDisplay,
    ) : AgeRule {
        override fun matches(age: Duration): Boolean =
            age >= threshold
    }

    data class LessThan(
        val threshold: Duration,
        override val display: TimeAgoDisplay,
    ) : AgeRule {
        override fun matches(age: Duration): Boolean =
            age < threshold
    }
}

data class AdaptiveUnits(
    private val rules: List<AgeRule> = listOf(
        AgeRule.AtLeast(
            threshold = 7.days,
            display = TimeAgoDisplay.Components(
                DurationUnit.DAYS,
                DurationUnit.HOURS,
            ),
        ),
        AgeRule.AtLeast(
            threshold = 1.days,
            display = TimeAgoDisplay.Components(
                DurationUnit.DAYS,
                DurationUnit.HOURS,
                DurationUnit.MINUTES,
            ),
        ),
        AgeRule.LessThan(
            threshold = 10.seconds,
            display = TimeAgoDisplay.JustNow,
        ),
    ),
    private val fallback: TimeAgoDisplay = TimeAgoDisplay.Components(
        DurationUnit.MINUTES,
        DurationUnit.SECONDS,
    ),
) : TimeAgoStrategy {
    override fun displayFor(duration: Duration): TimeAgoDisplay {
        val normalizedDuration = duration.coerceAtLeast(Duration.ZERO)
        val applicableRule = rules.firstOrNull { it.matches(normalizedDuration) }
        return applicableRule?.display ?: fallback
    }
}

/**
 * Capability descriptors.
 */

@Suppress("UnusedReceiverParameter")
val Capability.Constraint<*>.descriptor: Descriptor?
    get() = null

val Capability.Present<*>.descriptor: Descriptor
    get() = value.descriptor

val Capability<*, *>.descriptor: Descriptor
    get() = when (this) {
        is Brightness -> descriptor
        is ChildLock -> descriptor
        is MeasureDataSize -> descriptor
        is MeasureLoad -> descriptor
        is MeasureTemperature -> descriptor
        is Movement -> descriptor
        is OnOff -> descriptor
        is Opened -> descriptor
        is Position -> descriptor
        is ScheduledMode -> descriptor
        is TargetTemperature -> descriptor
        is ThermostatMode -> descriptor
        is ThermostatStatus -> descriptor
        is WindowDetection -> descriptor
    }

val Brightness.descriptor: Descriptor
    get() = Descriptor.Value(current)

val ChildLock.descriptor: Descriptor
    get() = when (current) {
        true -> Descriptor.On
        false -> Descriptor.Off
    }

val MeasureDataSize.descriptor: Descriptor
    get() = Descriptor.Value(current)

val MeasureLoad.descriptor: Descriptor
    get() = Descriptor.Value(current)

val MeasureTemperature.descriptor: Descriptor
    get() = Descriptor.Value(current)

val Movement.descriptor: Descriptor
    get() = when (current) {
        Direction.Idle -> Descriptor.Off
        Direction.Opening -> Descriptor.Opening
        Direction.Closing -> Descriptor.Closing
    }

val OnOff.descriptor: Descriptor
    get() = when (current) {
        true -> Descriptor.On
        false -> Descriptor.Off
    }

val Opened.descriptor: Descriptor
    get() = when (current) {
        true -> Descriptor.On
        false -> Descriptor.Off
    }

val Position.descriptor: Descriptor
    get() = when (current.value) {
        0.0 -> Descriptor.Closed
        1.0 -> Descriptor.Open
        else -> Descriptor.Opened(current)
    }

val ScheduledMode.descriptor: Descriptor
    get() = when (current) {
        true -> Descriptor.On
        false -> Descriptor.Off
    }

val TargetTemperature.descriptor: Descriptor
    get() = Descriptor.Value(current)

@Suppress("UnusedReceiverParameter")
val ThermostatMode.descriptor: Descriptor
    get() = Descriptor.Empty

val ThermostatStatus.descriptor: Descriptor
    get() = Descriptor.Enum(current)

val WindowDetection.descriptor: Descriptor
    get() = when (current) {
        true -> Descriptor.On
        false -> Descriptor.Off
    }

/**
 * Entity descriptors.
 */

val Entity.descriptor: Descriptor
    get() = state.descriptor

val Entity.State.descriptor: Descriptor
    get() = when (this) {
        is Entity.State.Unknown -> Descriptor.Unknown
        is Cover.State.Known -> descriptor
        is Camera.State.Known -> descriptor
        is Fan.State.Known -> descriptor
        is Light.State.Known -> descriptor
        is SmartLock.State.Known -> descriptor
        is SmartPlug.State.Known -> descriptor
        is Speaker.State.Known -> descriptor
        is System.State.Known -> descriptor
        is Thermostat.State.Known -> descriptor
    }

val Cover.State.Known.descriptor: Descriptor
    get() = control.descriptor

val Camera.State.Known.descriptor: Descriptor
    get() = onOff.descriptor

val Fan.State.Known.descriptor: Descriptor
    get() = onOff.descriptor

val Light.State.Known.descriptor: Descriptor
    get() = Descriptor.Combined(
        onOff.descriptor,
        brightness.descriptor,
    )

val SmartLock.State.Known.descriptor: Descriptor
    get() = onOff.descriptor

val SmartPlug.State.Known.descriptor: Descriptor
    get() = onOff.descriptor

val Speaker.State.Known.descriptor: Descriptor
    get() = onOff.descriptor

val System.State.Known.descriptor: Descriptor
    get() = Descriptor.Combined(
        processor.load.descriptor,
        Descriptor.TimeAgo(value = startedAt.application),
    )

val Thermostat.State.Known.descriptor: Descriptor
    get() = when (status.value.current) {
        ThermostatStatus.Status.Off, ThermostatStatus.Status.Idle ->
            temperatures.current.descriptor

        else -> Descriptor.Combined(
            status.descriptor,
            temperatures.target.descriptor,
        )
    }

/**
 * Cover control descriptors.
 */

val Cover.Control.descriptor: Descriptor
    get() = when (this) {
        is Cover.Control.WithPosition -> descriptor
        is Cover.Control.WithoutPosition -> descriptor
    }

val Cover.Control.WithPosition.descriptor: Descriptor
    get() {
        val movement = movement

        return when {
            movement is Capability.Present<*> && movement.value.current != Direction.Idle ->
                movement.descriptor

            else -> position.descriptor
        }
    }

val Cover.Control.WithoutPosition.descriptor: Descriptor
    get() {
        val movement = movement

        return when {
            movement is Capability.Present<*> && movement.value.current != Direction.Idle ->
                movement.descriptor

            else -> opened.descriptor
        }
    }
