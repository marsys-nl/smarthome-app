package network.marsys.smarthome.shared.library.core.helper

import androidx.compose.runtime.Composable
import network.marsys.smarthome.shared.domain.entity.capability.Capability
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.get

@Composable
@Suppress("ComposableNaming")
inline fun <reified C : Capability<*>> Entity<*>.ifPresent(block: @Composable (C) -> Unit): Unit =
    block.invoke(get<C>() ?: return)
