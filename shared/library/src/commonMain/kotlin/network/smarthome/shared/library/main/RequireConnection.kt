package network.smarthome.shared.library.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import network.marsys.smarthome.shared.feature.initialization.InitializationScreenView
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.model.ConnectionConfiguration
import org.koin.compose.koinInject

@Composable
internal fun WithRequireConnection(
    applicationConfigurationRepository: ApplicationConfigurationRepository = koinInject(),
    content: @Composable () -> Unit,
) {
    val connection = applicationConfigurationRepository.connection
        .collectAsState(
            initial = ConnectionConfiguration.Unconfigured,
        )

    when (connection.value) {
        is ConnectionConfiguration.Demo -> content.invoke()

        else -> InitializationScreenView(
            content = content,
        )
    }
}
