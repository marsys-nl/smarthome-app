package network.smarthome.shared.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import network.marsys.smarthome.shared.library.design.component.Button
import network.marsys.smarthome.shared.library.design.component.Text
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.OnboardingRepository
import org.koin.compose.koinInject

@Composable
internal fun MainScreenView(
    modifier: Modifier = Modifier,
    applicationConfigurationRepository: ApplicationConfigurationRepository = koinInject(),
    onboardingRepository: OnboardingRepository = koinInject(),
) {
    val demoMode by applicationConfigurationRepository.isDemoMode
        .collectAsStateWithLifecycle(initialValue = false)

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val coroutineScope = rememberCoroutineScope()

            Text(text = "Dashboard" + if (demoMode) " (demo)" else "")
            Button(
                onClick = {
                    coroutineScope.launch {
                        onboardingRepository.resetOnboarding()
                    }
                },
            ) {
                Text(text = "Reset onboarding")
            }
        }
    }
}
