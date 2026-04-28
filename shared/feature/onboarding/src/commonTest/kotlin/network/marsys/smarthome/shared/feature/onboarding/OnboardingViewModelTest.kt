package network.marsys.smarthome.shared.feature.onboarding

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import kotlinx.coroutines.ExperimentalCoroutinesApi
import network.marsys.smarthome.shared.feature.onboarding.fake.FakeAppearancePreferencesRepository
import network.marsys.smarthome.shared.feature.onboarding.fake.FakeApplicationConfigurationRepository
import network.marsys.smarthome.shared.feature.onboarding.fake.FakeOnboardingRepository
import network.marsys.smarthome.shared.feature.onboarding.screens.configuration.ConfigurationOnboardingState
import network.marsys.smarthome.shared.library.store.AppearancePreferencesRepository
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.OnboardingRepository
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val OnboardingViewModelTest by testSuite {
    testFixture {
        startKoin {
            modules(
                modules = module {
                    single<AppearancePreferencesRepository> { FakeAppearancePreferencesRepository() }
                    single<ApplicationConfigurationRepository> { FakeApplicationConfigurationRepository() }
                    single<OnboardingRepository> { FakeOnboardingRepository() }
                },
            )
        }.koin
    } closeWith {
        stopKoin()
    } asParameterForEach {
        test("initial configuration state is Idle") {
            val viewModel = OnboardingViewModel(
                appearancePreferencesRepository = it.get(),
                applicationConfigurationRepository = it.get(),
                onboardingRepository = it.get(),
                coroutineScope = testScope,
            )

            expectThat(viewModel.configuration.value)
                .isA<ConfigurationOnboardingState.Idle>()
        }
    }
}
