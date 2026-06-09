package network.marsys.smarthome.shared.library.core.coroutines

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun <T : Any> SuspendingStateMutator<T>.produceStateWithLifecycle(
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minimalActiveLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
): T {
    val scope = rememberCoroutineScope()

    DisposableEffect(this, scope, lifecycle, minimalActiveLifecycleState, context) {
        val job = scope.launch {
            lifecycle.repeatOnLifecycle(minimalActiveLifecycleState) {
                when (context) {
                    EmptyCoroutineContext -> collect()

                    else -> withContext(context) {
                        collect()
                    }
                }
            }
        }

        onDispose(job::cancel)
    }

    return state
}
