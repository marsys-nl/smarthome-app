package network.marsys.smarthome.shared.feature.onboarding.fake

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository

class FakeApplicationConfigurationRepository(
    override val backendUri: MutableStateFlow<String?> =
        MutableStateFlow(value = null),
    override val isDemoMode: MutableStateFlow<Boolean> =
        MutableStateFlow(value = false),
) : ApplicationConfigurationRepository {
    override suspend fun setBackendUri(uri: String) =
        this.backendUri.update {
            uri
        }

    override suspend fun setDemoMode(enabled: Boolean) =
        this.isDemoMode.update {
            enabled
        }
}
