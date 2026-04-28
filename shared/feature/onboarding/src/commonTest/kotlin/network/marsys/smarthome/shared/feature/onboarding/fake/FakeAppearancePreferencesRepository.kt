package network.marsys.smarthome.shared.feature.onboarding.fake

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import network.marsys.smarthome.shared.library.design.ThemeSelection
import network.marsys.smarthome.shared.library.store.AppearancePreferencesRepository

class FakeAppearancePreferencesRepository(
    override val theme: MutableStateFlow<ThemeSelection> =
        MutableStateFlow(ThemeSelection.SystemDefault),
) : AppearancePreferencesRepository {
    override suspend fun setTheme(theme: ThemeSelection) =
        this.theme.update {
            theme
        }
}
