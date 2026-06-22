package network.marsys.smarthome.shared.modal.entity.helper

import androidx.compose.runtime.Composable
import network.marsys.smarthome.shared.domain.entity.capability.Capability
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.get

@Composable
@Suppress("ComposableNaming")
inline fun <reified C : Capability<*>> Entity<*>.ifPresent(block: @Composable (C) -> Unit): Unit =
    when (val current = state) {
        is Entity.State.Known -> block.invoke(current.get<C>() ?: return)
        is Entity.State.Unknown -> Unit
    }
