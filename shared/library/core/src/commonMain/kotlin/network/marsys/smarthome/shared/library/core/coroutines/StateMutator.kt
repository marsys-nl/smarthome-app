package network.marsys.smarthome.shared.library.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

typealias SuspendingStateProducer<S> = suspend CoroutineScope.(S) -> Unit
typealias SuspendingActionStateProducer<A, S> = suspend CoroutineScope.(S, Flow<A>) -> Unit
typealias SuspendingActionStateEffectProducer<A, S, E> = suspend CoroutineScope.(S, Flow<A>, EffectEmitter<E>) -> Unit

fun interface EffectEmitter<in E : Any> {
    suspend fun emit(effect: E)
}

interface StateMutator<out S : Any> {
    val state: S
}

interface EffectMutator<out E : Any> {
    val effect: Flow<E>
}

interface ActionStateMutator<in A : Any, out S : Any> : StateMutator<S> {
    val accept: (A) -> Unit
}

interface SuspendingStateMutator<out S : Any> : StateMutator<S> {
    suspend fun collect()
}

fun <S : Any> CoroutineScope.suspendingStateMutator(
    state: S,
    started: SharingStarted = SharingStarted.WhileSubscribed(DEFAULT_STOP_TIMEOUT_MILLIS),
    producer: SuspendingStateProducer<S>,
): SuspendingStateMutator<S> {
    val subscribers = MutableStateFlow(0)
    val mutator = ReferenceCountingSuspendingStateMutator(
        state = state,
        subscribers = subscribers,
    )

    launch {
        var job: Job? = null

        started.command(subscribers).collect { command ->
            when (command) {
                SharingCommand.START,
                -> {
                    if (job == null || job?.isActive == false) {
                        job = launch {
                            producer.invoke(this, state)
                        }
                    }
                }

                SharingCommand.STOP,
                SharingCommand.STOP_AND_RESET_REPLAY_CACHE,
                -> {
                    job?.cancel()
                    job = null
                }
            }
        }
    }

    return mutator
}

private class ReferenceCountingSuspendingStateMutator<S : Any>(
    override val state: S,
    private val subscribers: MutableStateFlow<Int>,
) : SuspendingStateMutator<S> {
    override suspend fun collect() {
        try {
            subscribers.update { it + 1 }
            awaitCancellation()
        } finally {
            subscribers.update { it - 1 }
        }
    }
}

interface SuspendingActionStateMutator<in A : Any, out S : Any> :
    ActionStateMutator<A, S>,
    SuspendingStateMutator<S>

fun <A : Any, S : Any> CoroutineScope.suspendingActionStateMutator(
    state: S,
    started: SharingStarted = SharingStarted.WhileSubscribed(DEFAULT_STOP_TIMEOUT_MILLIS),
    producer: SuspendingActionStateProducer<A, S>,
): SuspendingActionStateMutator<A, S> =
    DelegatedSuspendingActionStateMutator(
        coroutineScope = this,
        state = state,
        started = started,
        producer = producer,
    )

private open class DelegatedSuspendingActionStateMutator<A : Any, S : Any>(
    coroutineScope: CoroutineScope,
    state: S,
    started: SharingStarted = SharingStarted.WhileSubscribed(DEFAULT_STOP_TIMEOUT_MILLIS),
    producer: SuspendingActionStateProducer<A, S>,
) : SuspendingActionStateMutator<A, S> {
    private val actions = Channel<A>()

    private val mutator = coroutineScope.suspendingStateMutator(
        state = state,
        started = started,
    ) { state ->
        producer.invoke(
            this,
            state,
            actions.receiveAsFlow(),
        )
    }

    override val state: S
        get() = mutator.state

    override val accept: (A) -> Unit = { action ->
        coroutineScope.launch {
            actions.send(action)
        }
    }

    override suspend fun collect() =
        mutator.collect()
}

interface SuspendingActionStateEffectMutator<in A : Any, out S : Any, out E : Any> :
    SuspendingActionStateMutator<A, S>,
    EffectMutator<E>

fun <A : Any, S : Any, E : Any> CoroutineScope.suspendingActionStateEffectMutator(
    state: S,
    started: SharingStarted = SharingStarted.WhileSubscribed(DEFAULT_STOP_TIMEOUT_MILLIS),
    producer: SuspendingActionStateEffectProducer<A, S, E>,
): SuspendingActionStateEffectMutator<A, S, E> =
    DelegatedSuspendingActionStateEffectMutator(
        coroutineScope = this,
        state = state,
        started = started,
        producer = producer,
    )

private class DelegatedSuspendingActionStateEffectMutator<A : Any, S : Any, E : Any>(
    coroutineScope: CoroutineScope,
    state: S,
    started: SharingStarted = SharingStarted.WhileSubscribed(DEFAULT_STOP_TIMEOUT_MILLIS),
    producer: SuspendingActionStateEffectProducer<A, S, E>,
) : SuspendingActionStateEffectMutator<A, S, E> {
    private val actions = Channel<A>()
    private val effects = Channel<E>()

    private val emitter = EffectEmitter<E> { effect ->
        effects.send(effect)
    }

    private val mutator = coroutineScope.suspendingStateMutator(
        state = state,
        started = started,
    ) { state ->
        producer.invoke(
            this,
            state,
            actions.receiveAsFlow(),
            emitter,
        )
    }

    override val effect: Flow<E>
        get() = effects.receiveAsFlow()

    override val state: S
        get() = mutator.state

    override val accept: (A) -> Unit = { action ->
        coroutineScope.launch {
            actions.send(action)
        }
    }

    override suspend fun collect() =
        mutator.collect()
}

internal const val DEFAULT_STOP_TIMEOUT_MILLIS = 5000L
