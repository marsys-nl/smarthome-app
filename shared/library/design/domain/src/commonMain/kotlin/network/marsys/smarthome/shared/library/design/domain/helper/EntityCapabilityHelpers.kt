@file:Suppress("MethodOverloading")

package network.marsys.smarthome.shared.library.design.domain.helper

import androidx.compose.runtime.Composable
import network.marsys.smarthome.domain.capability.Capability
import network.marsys.smarthome.domain.entity.Entity

@Composable
@Suppress("ComposableNaming")
inline fun <C : Capability<*, *>> Entity.ifPresent(
    query: Capability.Query<C>,
    block: @Composable (C) -> Unit,
): Unit = block.invoke(
    state.get(query = query)
        ?: return,
)

@Composable
@Suppress("ComposableNaming")
inline fun <C1 : Capability<*, *>, C2 : Capability<*, *>> Entity.ifPresent(
    query1: Capability.Query<C1>,
    query2: Capability.Query<C2>,
    block: @Composable (C1, C2) -> Unit,
) {
    val result1 = state.get(query = query1)
    val result2 = state.get(query = query2)

    if (result1 == null || result2 == null) return

    block.invoke(result1, result2)
}

@Composable
@Suppress("ComposableNaming")
inline fun <C1 : Capability<*, *>, C2 : Capability<*, *>> Entity.ifPresent(
    query1: Capability.Query<C1>,
    query2: OptionalQuery<C2>,
    block: @Composable (C1, C2?) -> Unit,
) {
    val result1 = state.get(query = query1)
    val result2 = state.get(query = query2.query)

    if (result1 == null) return

    block.invoke(result1, result2)
}

@Composable
@Suppress("ComposableNaming")
inline fun <C1 : Capability<*, *>, C2 : Capability<*, *>> Entity.ifPresent(
    query1: OptionalQuery<C1>,
    query2: OptionalQuery<C2>,
    block: @Composable (C1?, C2?) -> Unit,
) {
    val result1 = state.get(query = query1.query)
    val result2 = state.get(query = query2.query)

    if (result1 == null && result2 == null) return

    block.invoke(result1, result2)
}

@Composable
@Suppress("ComposableNaming")
inline fun <C1 : Capability<*, *>, C2 : Capability<*, *>, C3 : Capability<*, *>> Entity.ifPresent(
    query1: Capability.Query<C1>,
    query2: Capability.Query<C2>,
    query3: Capability.Query<C3>,
    block: @Composable (C1, C2, C3) -> Unit,
) {
    val result1 = state.get(query = query1)
    val result2 = state.get(query = query2)
    val result3 = state.get(query = query3)

    if (result1 == null || result2 == null || result3 == null) return

    block.invoke(result1, result2, result3)
}

@Composable
@Suppress("ComposableNaming")
inline fun <C1 : Capability<*, *>, C2 : Capability<*, *>, C3 : Capability<*, *>> Entity.ifPresent(
    query1: Capability.Query<C1>,
    query2: Capability.Query<C2>,
    query3: OptionalQuery<C3>,
    block: @Composable (C1, C2, C3?) -> Unit,
) {
    val result1 = state.get(query = query1)
    val result2 = state.get(query = query2)
    val result3 = state.get(query = query3.query)

    if (result1 == null || result2 == null) return

    block.invoke(result1, result2, result3)
}

@Composable
@Suppress("ComposableNaming")
inline fun <C1 : Capability<*, *>, C2 : Capability<*, *>, C3 : Capability<*, *>> Entity.ifPresent(
    query1: Capability.Query<C1>,
    query2: OptionalQuery<C2>,
    query3: OptionalQuery<C3>,
    block: @Composable (C1, C2?, C3?) -> Unit,
) {
    val result1 = state.get(query = query1)
    val result2 = state.get(query = query2.query)
    val result3 = state.get(query = query3.query)

    if (result1 == null) return

    block.invoke(result1, result2, result3)
}

@Composable
@Suppress("ComposableNaming")
inline fun <C1 : Capability<*, *>, C2 : Capability<*, *>, C3 : Capability<*, *>> Entity.ifPresent(
    query1: OptionalQuery<C1>,
    query2: OptionalQuery<C2>,
    query3: OptionalQuery<C3>,
    block: @Composable (C1?, C2?, C3?) -> Unit,
) {
    val result1 = state.get(query = query1.query)
    val result2 = state.get(query = query2.query)
    val result3 = state.get(query = query3.query)

    if (result1 == null && result2 == null && result3 == null) return

    block.invoke(result1, result2, result3)
}

@Composable
@Suppress("ComposableNaming", "MaxLineLength")
inline fun <C1 : Capability<*, *>, C2 : Capability<*, *>, C3 : Capability<*, *>, C4 : Capability<*, *>> Entity.ifPresent(
    query1: Capability.Query<C1>,
    query2: Capability.Query<C2>,
    query3: Capability.Query<C3>,
    query4: Capability.Query<C4>,
    block: @Composable (C1, C2, C3, C4) -> Unit,
) {
    val result1 = state.get(query = query1)
    val result2 = state.get(query = query2)
    val result3 = state.get(query = query3)
    val result4 = state.get(query = query4)

    if (result1 == null || result2 == null || result3 == null || result4 == null) return

    block.invoke(result1, result2, result3, result4)
}

@Composable
@Suppress("ComposableNaming", "MaxLineLength")
inline fun <C1 : Capability<*, *>, C2 : Capability<*, *>, C3 : Capability<*, *>, C4 : Capability<*, *>> Entity.ifPresent(
    query1: Capability.Query<C1>,
    query2: Capability.Query<C2>,
    query3: Capability.Query<C3>,
    query4: OptionalQuery<C4>,
    block: @Composable (C1, C2, C3, C4?) -> Unit,
) {
    val result1 = state.get(query = query1)
    val result2 = state.get(query = query2)
    val result3 = state.get(query = query3)
    val result4 = state.get(query = query4.query)

    if (result1 == null || result2 == null || result3 == null) return

    block.invoke(result1, result2, result3, result4)
}

@Composable
@Suppress("ComposableNaming", "MaxLineLength")
inline fun <C1 : Capability<*, *>, C2 : Capability<*, *>, C3 : Capability<*, *>, C4 : Capability<*, *>> Entity.ifPresent(
    query1: Capability.Query<C1>,
    query2: Capability.Query<C2>,
    query3: OptionalQuery<C3>,
    query4: OptionalQuery<C4>,
    block: @Composable (C1, C2, C3?, C4?) -> Unit,
) {
    val result1 = state.get(query = query1)
    val result2 = state.get(query = query2)
    val result3 = state.get(query = query3.query)
    val result4 = state.get(query = query4.query)

    if (result1 == null || result2 == null) return

    block.invoke(result1, result2, result3, result4)
}

@Composable
@Suppress("ComposableNaming", "MaxLineLength")
inline fun <C1 : Capability<*, *>, C2 : Capability<*, *>, C3 : Capability<*, *>, C4 : Capability<*, *>> Entity.ifPresent(
    query1: Capability.Query<C1>,
    query2: OptionalQuery<C2>,
    query3: OptionalQuery<C3>,
    query4: OptionalQuery<C4>,
    block: @Composable (C1, C2?, C3?, C4?) -> Unit,
) {
    val result1 = state.get(query = query1)
    val result2 = state.get(query = query2.query)
    val result3 = state.get(query = query3.query)
    val result4 = state.get(query = query4.query)

    if (result1 == null) return

    block.invoke(result1, result2, result3, result4)
}

@Composable
@Suppress("ComposableNaming", "MaxLineLength")
inline fun <C1 : Capability<*, *>, C2 : Capability<*, *>, C3 : Capability<*, *>, C4 : Capability<*, *>> Entity.ifPresent(
    query1: OptionalQuery<C1>,
    query2: OptionalQuery<C2>,
    query3: OptionalQuery<C3>,
    query4: OptionalQuery<C4>,
    block: @Composable (C1?, C2?, C3?, C4?) -> Unit,
) {
    val result1 = state.get(query = query1.query)
    val result2 = state.get(query = query2.query)
    val result3 = state.get(query = query3.query)
    val result4 = state.get(query = query4.query)

    if (result1 == null && result2 == null && result3 == null && result4 == null) return

    block.invoke(result1, result2, result3, result4)
}

data class OptionalQuery<C : Capability<*, *>>(
    val query: Capability.Query<C>,
)

val <C : Capability<*, *>> Capability.Query<C>.optional
    get() = OptionalQuery(query = this)
