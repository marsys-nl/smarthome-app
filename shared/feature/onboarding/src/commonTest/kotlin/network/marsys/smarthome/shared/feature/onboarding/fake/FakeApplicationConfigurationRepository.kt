package network.marsys.smarthome.shared.feature.onboarding.fake

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.model.ConnectionConfiguration

class FakeApplicationConfigurationRepository(
    override val connection: MutableStateFlow<ConnectionConfiguration> =
        MutableStateFlow(value = ConnectionConfiguration.Unconfigured),
) : ApplicationConfigurationRepository {
    override suspend fun setConnectionConfiguration(configuration: ConnectionConfiguration) =
        this.connection.update { configuration }
}
