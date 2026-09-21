package network.smarthome.shared.library.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import network.marsys.smarthome.shared.feature.initialization.InitializationScreenView
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import org.koin.compose.koinInject

@Composable
internal fun WithRequireConnection(
    applicationConfigurationRepository: ApplicationConfigurationRepository = koinInject(),
    content: @Composable () -> Unit,
) {
    val demoMode = applicationConfigurationRepository.isDemoMode
        .collectAsState(initial = false)

    when (demoMode.value) {
        true -> content.invoke()

        false -> InitializationScreenView(
            content = content,
        )
    }
}
