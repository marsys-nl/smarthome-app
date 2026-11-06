package network.marsys.smarthome.shared.library.design.domain

import androidx.compose.ui.graphics.vector.ImageVector
import network.marsys.smarthome.domain.entity.Blind
import network.marsys.smarthome.domain.entity.Camera
import network.marsys.smarthome.domain.entity.Curtain
import network.marsys.smarthome.domain.entity.Entity
import network.marsys.smarthome.domain.entity.Light
import network.marsys.smarthome.domain.entity.Shutter
import network.marsys.smarthome.domain.entity.SmartPlug
import network.marsys.smarthome.domain.entity.System
import network.marsys.smarthome.domain.entity.Thermostat
import network.marsys.smarthome.shared.library.design.icons.Blinds
import network.marsys.smarthome.shared.library.design.icons.Component
import network.marsys.smarthome.shared.library.design.icons.Icons
import network.marsys.smarthome.shared.library.design.icons.Lightbulb
import network.marsys.smarthome.shared.library.design.icons.Monitor
import network.marsys.smarthome.shared.library.design.icons.Plug
import network.marsys.smarthome.shared.library.design.icons.Server
import network.marsys.smarthome.shared.library.design.icons.Thermostat

fun Entity.Type<*>.icon(): ImageVector = when (this) {
    Light -> Icons.Lightbulb
    Thermostat -> Icons.Thermostat
    SmartPlug -> Icons.Plug
    Blind, Curtain, Shutter -> Icons.Blinds
    Camera -> Icons.Monitor
    System -> Icons.Server
    else -> Icons.Component
}
