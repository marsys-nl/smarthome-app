package network.marsys.smarthome.shared.feature.profile

import androidx.compose.runtime.Stable
import network.marsys.smarthome.shared.library.navigation.NavigationDestination

@Stable
interface ProfileScreenState {
    val user: String
    val email: String
    val connectedBackend: String?
}

sealed class ProfileScreenAction(val key: String) {
    data object ChangeAppAppearance : ProfileScreenAction("ChangeAppAppearance")
    data object Logout : ProfileScreenAction("Logout")
    data object ResetOnboarding : ProfileScreenAction("ResetOnboarding")
}

sealed interface ProfileScreenEffect {
    data class Navigate(val target: NavigationDestination) : ProfileScreenEffect
}
