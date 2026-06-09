package network.marsys.smarthome.shared.library.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

fun <A : Any> Flow<A>.handle(
    scope: CoroutineScope,
    capacity: Int = Channel.BUFFERED,
    onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND,
    keySelector: (A) -> Any = Any::defaultKeySelector,
    transform: suspend TransformationContext<A>.() -> Unit,
) {
    scope.launch {
        splitByType(
            capacity = capacity,
            onBufferOverflow = onBufferOverflow,
            typeSelector = { it },
            keySelector = keySelector,
            transform = transformation@{
                flow {
                    transform(this@transformation)
                    emit(Unit)
                }
            },
        ).collect()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <I : Any, S : Any, O : Any> Flow<I>.splitByType(
    capacity: Int,
    onBufferOverflow: BufferOverflow,
    typeSelector: (I) -> S,
    keySelector: (S) -> Any = Any::defaultKeySelector,
    transform: suspend TransformationContext<S>.() -> Flow<O>,
): Flow<O> = channelFlow mutationFlow@{
    val keysToFlowHolders = mutableMapOf<Any, FlowHolder<S>>()
    try {
        this@splitByType
            .collect { item ->
                val selected = typeSelector(item)
                val flowKey = keySelector(selected)
                when (val existingHolder = keysToFlowHolders[flowKey]) {
                    null -> {
                        val holder = FlowHolder(
                            capacity = capacity,
                            onBufferOverflow = onBufferOverflow,
                            firstEmission = selected,
                        )
                        keysToFlowHolders[flowKey] = holder
                        val context = TransformationContext(selected, holder.exposedFlow)
                        val mutationFlow = transform(context)
                        channel.send(mutationFlow)
                    }

                    else -> {
                        existingHolder.channel.send(selected)
                    }
                }
            }
    } finally {
        keysToFlowHolders.values.forEach { it.channel.close() }
    }
}.flattenMerge(concurrency = Int.MAX_VALUE)

class TransformationContext<A : Any>(
    private val type: A,
    val backing: Flow<A>,
) {
    @Suppress("UNCHECKED_CAST", "UnusedReceiverParameter")
    inline val <reified S : A> S.flow: Flow<S>
        get() = backing as Flow<S>

    fun type() = type
}

private data class FlowHolder<A>(
    val capacity: Int,
    val onBufferOverflow: BufferOverflow,
    val firstEmission: A,
) {
    val channel: Channel<A> = Channel(
        capacity = capacity,
        onBufferOverflow = onBufferOverflow,
    )
    val exposedFlow: Flow<A> = channel
        .receiveAsFlow()
        .onStart { emit(firstEmission) }
}

private fun Any.defaultKeySelector(): Any = this::class
