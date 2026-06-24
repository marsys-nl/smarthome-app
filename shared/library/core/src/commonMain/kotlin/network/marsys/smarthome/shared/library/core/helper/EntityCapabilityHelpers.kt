package network.marsys.smarthome.shared.library.core.helper

import androidx.compose.runtime.Composable
import network.marsys.smarthome.shared.domain.entity.capability.Capability
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.entity.get

@Composable
@Suppress("ComposableNaming")
inline fun <reified C : Capability<*>> Entity<*>.ifPresent(block: @Composable (C) -> Unit): Unit =
    block.invoke(get<C>() ?: return)

@Composable
@Suppress("ComposableNaming")
inline fun <reified C : Capability<*>> Entity<*>.ifPresent(
    predicate: (C) -> Boolean,
    block: @Composable (C) -> Unit,
): Unit = block.invoke(
    get<C>(predicate = predicate) ?: return,
)

@Composable
@Suppress("ComposableNaming")
inline fun <reified C1 : Capability<*>, reified C2 : Capability<*>> Entity<*>.ifPresent(
    block: @Composable (C1?, C2?) -> Unit,
): Unit = block.invoke(
    get<C1>(),
    get<C2>(),
)

@Composable
@Suppress("ComposableNaming")
inline fun <reified C1 : Capability<*>, reified C2 : Capability<*>, reified C3 : Capability<*>> Entity<*>.ifPresent(
    block: @Composable (C1?, C2?, C3?) -> Unit,
): Unit = block.invoke(
    get<C1>(),
    get<C2>(),
    get<C3>(),
)
