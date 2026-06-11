package network.marsys.smarthome.shared.library.core.coroutines

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
@Suppress("ComposableNaming")
fun <E : Any> EffectMutator<E>.collectEffectsWithLifecycle(
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minimalActiveLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
    onEffect: suspend (E) -> Unit,
) {
    val currentOnEffect by rememberUpdatedState(onEffect)

    LaunchedEffect(this, lifecycle, minimalActiveLifecycleState, context) {
        lifecycle.repeatOnLifecycle(minimalActiveLifecycleState) {
            when (context) {
                EmptyCoroutineContext -> effect.collect {
                    currentOnEffect(it)
                }

                else -> withContext(context) {
                    effect.collect {
                        currentOnEffect(it)
                    }
                }
            }
        }
    }
}
