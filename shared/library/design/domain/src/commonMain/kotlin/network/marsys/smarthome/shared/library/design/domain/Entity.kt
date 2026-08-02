package network.marsys.smarthome.shared.library.design.domain

import androidx.compose.ui.graphics.vector.ImageVector
import network.marsys.smarthome.shared.domain.entity.entity.Blind
import network.marsys.smarthome.shared.domain.entity.entity.Camera
import network.marsys.smarthome.shared.domain.entity.entity.Curtain
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.Light
import network.marsys.smarthome.shared.domain.entity.entity.Shutter
import network.marsys.smarthome.shared.domain.entity.entity.SmartPlug
import network.marsys.smarthome.shared.domain.entity.entity.Thermostat
import network.marsys.smarthome.shared.library.design.icons.Blinds
import network.marsys.smarthome.shared.library.design.icons.Component
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Lightbulb
import network.marsys.smarthome.shared.library.design.icons.Monitor
import network.marsys.smarthome.shared.library.design.icons.Plug
import network.marsys.smarthome.shared.library.design.icons.Thermostat
import kotlin.reflect.KClass

fun KClass<out Entity<*>>.icon(): ImageVector = when (this) {
    Light::class -> Icons.Lightbulb
    Thermostat::class -> Icons.Thermostat
    SmartPlug::class -> Icons.Plug
    Blind::class, Curtain::class, Shutter::class -> Icons.Blinds
    Camera::class -> Icons.Monitor
    else -> Icons.Component
}
