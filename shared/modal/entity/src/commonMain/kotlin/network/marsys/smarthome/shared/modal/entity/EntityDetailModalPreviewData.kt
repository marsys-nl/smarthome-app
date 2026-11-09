package network.marsys.smarthome.shared.modal.entity

import network.marsys.smarthome.domain.TranslationKey
import network.marsys.smarthome.domain.capability.Brightness
import network.marsys.smarthome.domain.capability.Capability.Companion.optional
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.MeasureDataSize
import network.marsys.smarthome.domain.capability.MeasureLoad
import network.marsys.smarthome.domain.capability.MeasureTemperature
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.entity.Entity
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.EntityNaming
import network.marsys.smarthome.domain.entity.Light
import network.marsys.smarthome.domain.entity.System
import network.marsys.smarthome.domain.entity.Thermostat
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.domain.unit.gigabytes
import network.marsys.smarthome.domain.unit.percent
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

object EntityDetailModalPreviewData {
    val loading = object : EntityDetailModalState {
        override val isLoading: Boolean = true
        override val entity: Entity? = null
    }

    val loaded = mapOf(
        Light to object : EntityDetailModalState {
            override val isLoading: Boolean = false
            override val entity: Entity = Light(
                identifier = EntityIdentifier("light.kitchen-light"),
                naming = EntityNaming(
                    key = TranslationKey("light.kitchen"),
                    qualifier = TranslationKey("spots"),
                ),
                state = Light.State.Known(
                    onOff = required(OnOff(current = true)),
                    brightness = optional(Brightness(80.percent)),
                ),
            )
        },

        Thermostat to object : EntityDetailModalState {
            override val isLoading: Boolean = false
            override val entity: Entity = Thermostat(
                identifier = EntityIdentifier("thermostat.living-room"),
                naming = EntityNaming(
                    key = TranslationKey("thermostat.living-room"),
                ),
                state = Thermostat.State.Unknown,
            )
        },

        System to object : EntityDetailModalState {
            override val isLoading: Boolean = false
            override val entity: Entity = System(
                identifier = EntityIdentifier("system"),
                naming = EntityNaming(
                    key = TranslationKey("system"),
                ),
                state = System.State.Known(
                    host = System.Host(
                        device = System.Host.Device(
                            manufacturer = "Marsys",
                            model = "SmartHome Hub",
                            architecture = "arm64",
                            physicalCores = 8,
                            logicalCores = 16,
                        ),
                        operatingSystem = System.Host.OperatingSystem(
                            description = "SmartHome OS",
                            family = "Linux",
                            version = "1.0.0",
                            bitness = 64,
                        ),
                    ),
                    processor = System.Processor(
                        load = required(MeasureLoad(current = 43.percent)),
                        temperature = optional(MeasureTemperature(current = 65.celsius)),
                    ),
                    memory = System.Memory(
                        total = required(MeasureDataSize(current = 32.gigabytes)),
                        available = required(MeasureDataSize(current = 8.3453.gigabytes)),
                        swap = System.Memory.Swap(
                            total = required(MeasureDataSize(current = 8.gigabytes)),
                            available = required(MeasureDataSize(current = 2.3453.gigabytes)),
                        ),
                    ),
                    startedAt = System.StartedAt(
                        host = Clock.System.now().minus(1.days),
                        application = Clock.System.now().minus(23.hours + 50.minutes),
                    ),
                ),
            )
        },
    )
}
