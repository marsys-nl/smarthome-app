package network.marsys.smarthome.shared.domain.entity

import network.marsys.smarthome.domain.entity.Blind
import network.marsys.smarthome.domain.entity.Camera
import network.marsys.smarthome.domain.entity.Curtain
import network.marsys.smarthome.domain.entity.Entity
import network.marsys.smarthome.domain.entity.Fan
import network.marsys.smarthome.domain.entity.Light
import network.marsys.smarthome.domain.entity.Shutter
import network.marsys.smarthome.domain.entity.SmartLock
import network.marsys.smarthome.domain.entity.SmartPlug
import network.marsys.smarthome.domain.entity.Speaker
import network.marsys.smarthome.domain.entity.System
import network.marsys.smarthome.domain.entity.Thermostat

inline fun <reified E : Entity> E.update(block: (Entity.State.Known) -> Entity.State): E =
    when (val current = state) {
        is Entity.State.Known -> copyWithState(state = block.invoke(current))
        else -> this
    }

inline fun <reified E : Entity, S : Entity.State> E.copyWithState(state: S): E =
    when (this) {
        is Blind -> copy(state = state as Blind.State)
        is Camera -> copy(state = state)
        is Curtain -> copy(state = state as Curtain.State)
        is Fan -> copy(state = state)
        is Light -> copy(state = state)
        is Shutter -> copy(state = state as Shutter.State)
        is SmartLock -> copy(state = state)
        is SmartPlug -> copy(state = state)
        is Speaker -> copy(state = state)
        is System -> copy(state = state)
        is Thermostat -> copy(state = state)
    } as E
