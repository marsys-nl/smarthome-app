package network.marsys.smarthome.shared.feature.onboarding

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import dev.nmarsman.expect.assertions.isFalse
import dev.nmarsman.expect.assertions.isNotNull
import dev.nmarsman.expect.assertions.isTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import network.marsys.smarthome.shared.domain.connection.ValidateBackendUriUseCase
import network.marsys.smarthome.shared.feature.onboarding.fake.FakeAppearancePreferencesRepository
import network.marsys.smarthome.shared.feature.onboarding.fake.FakeApplicationConfigurationRepository
import network.marsys.smarthome.shared.feature.onboarding.fake.FakeOnboardingRepository
import network.marsys.smarthome.shared.feature.onboarding.screens.configuration.BackendUriError
import network.marsys.smarthome.shared.feature.onboarding.screens.configuration.ConfigurationOnboardingState
import network.marsys.smarthome.shared.library.design.ThemeSelection
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
                    single<ValidateBackendUriUseCase> {
                        ValidateBackendUriUseCase { true }
                    }
                },
            )
        }.koin
    } closeWith {
        stopKoin()
    } asParameterForEach {
        test(name = "Should have Idle configuration state when initialized") {
            val viewModel = OnboardingViewModel(
                appearancePreferencesRepository = it.get(),
                applicationConfigurationRepository = it.get(),
                onboardingRepository = it.get(),
                validateBackendUriUseCase = it.get(),
            )

            expectThat(viewModel.configuration.value)
                .isA<ConfigurationOnboardingState.Idle>()
        }

        test(name = "Should persist theme when theme is selected") {
            val appearancePreferencesRepository = it.get<AppearancePreferencesRepository>()

            val viewModel = OnboardingViewModel(
                appearancePreferencesRepository = appearancePreferencesRepository,
                applicationConfigurationRepository = it.get(),
                onboardingRepository = it.get(),
                validateBackendUriUseCase = it.get(),
                coroutineScope = testScope,
            )

            viewModel.selectTheme(theme = ThemeSelection.DarkMode)
            testScope.advanceUntilIdle()

            expectThat(appearancePreferencesRepository.theme.first())
                .isEqualTo(ThemeSelection.DarkMode)
        }

        test(name = "Should transition to processing when finish onboarding is called") {
            val viewModel = OnboardingViewModel(
                appearancePreferencesRepository = it.get(),
                applicationConfigurationRepository = it.get(),
                onboardingRepository = it.get(),
                validateBackendUriUseCase = it.get(),
                coroutineScope = testScope,
            )

            viewModel.uriTextFieldState.edit { append("https://example.com") }
            viewModel.finishOnboarding()
            testScope.runCurrent()

            expectThat(viewModel.configuration.value)
                .isA<ConfigurationOnboardingState.Processing>()
        }

        test(name = "Should set backend uri when finish onboarding is called with a valid uri") {
            val applicationConfigurationRepository = it.get<ApplicationConfigurationRepository>()
            val viewModel = OnboardingViewModel(
                appearancePreferencesRepository = it.get(),
                applicationConfigurationRepository = applicationConfigurationRepository,
                onboardingRepository = it.get(),
                validateBackendUriUseCase = it.get(),
                coroutineScope = testScope,
            )

            viewModel.uriTextFieldState.edit { append("https://example.com") }
            viewModel.finishOnboarding()
            testScope.advanceUntilIdle()

            expectThat(applicationConfigurationRepository.backendUri.first())
                .isEqualTo("https://example.com")
        }

        test(name = "Should reset demo mode when finish onboarding is called with a valid uri") {
            val applicationConfigurationRepository = it.get<ApplicationConfigurationRepository>()
            val viewModel = OnboardingViewModel(
                appearancePreferencesRepository = it.get(),
                applicationConfigurationRepository = applicationConfigurationRepository,
                onboardingRepository = it.get(),
                validateBackendUriUseCase = it.get(),
                coroutineScope = testScope,
            )

            viewModel.uriTextFieldState.edit { append("https://example.com") }
            viewModel.finishOnboarding()
            testScope.advanceUntilIdle()

            expectThat(applicationConfigurationRepository.isDemoMode.first())
                .isFalse()
        }

        test(name = "Should set onboarding finished when finish onboarding is called with a valid uri") {
            val onboardingRepository = it.get<OnboardingRepository>()
            val viewModel = OnboardingViewModel(
                appearancePreferencesRepository = it.get(),
                applicationConfigurationRepository = it.get(),
                onboardingRepository = onboardingRepository,
                validateBackendUriUseCase = it.get(),
                coroutineScope = testScope,
            )

            viewModel.uriTextFieldState.edit { append("https://example.com") }
            viewModel.finishOnboarding()
            testScope.advanceUntilIdle()

            expectThat(onboardingRepository.isOnboardingFinished.first())
                .isTrue()
        }

        test(name = "Should emit empty error when finishing onboarding with blank uri") {
            val viewModel = OnboardingViewModel(
                appearancePreferencesRepository = it.get(),
                applicationConfigurationRepository = it.get(),
                onboardingRepository = it.get(),
                validateBackendUriUseCase = it.get(),
                coroutineScope = testScope,
            )

            viewModel.finishOnboarding()
            testScope.advanceUntilIdle()

            expectThat(viewModel.configuration.value)
                .isA<ConfigurationOnboardingState.Idle>()
                .get(ConfigurationOnboardingState.Idle::backendUriError)
                .isNotNull()
                .isA<BackendUriError.Empty>()
        }

        test(name = "Should emit invalid uri error when finishing onboarding with invalid uri") {
            val viewModel = OnboardingViewModel(
                appearancePreferencesRepository = it.get(),
                applicationConfigurationRepository = it.get(),
                onboardingRepository = it.get(),
                validateBackendUriUseCase = {
                    false
                },
                coroutineScope = testScope,
            )

            viewModel.uriTextFieldState.edit { append("https://example.com") }
            viewModel.finishOnboarding()
            testScope.advanceUntilIdle()

            expectThat(viewModel.configuration.value)
                .isA<ConfigurationOnboardingState.Idle>()
                .get(ConfigurationOnboardingState.Idle::backendUriError)
                .isNotNull()
                .isA<BackendUriError.Invalid>()
        }

        test(name = "Should ignore finish onboarding call when already processing") {
            val applicationConfigurationRepository = it.get<ApplicationConfigurationRepository>()
            val viewModel = OnboardingViewModel(
                appearancePreferencesRepository = it.get(),
                applicationConfigurationRepository = applicationConfigurationRepository,
                onboardingRepository = it.get(),
                validateBackendUriUseCase = it.get(),
                coroutineScope = testScope,
            )

            viewModel.uriTextFieldState.edit { append("https://example.com") }
            viewModel.finishOnboarding()
            testScope.runCurrent()

            viewModel.uriTextFieldState.edit { append("https://other.com") }
            viewModel.finishOnboarding()
            testScope.advanceUntilIdle()

            expectThat(applicationConfigurationRepository.backendUri.first())
                .isEqualTo("https://example.com")
        }

        test(name = "Should set demo mode when skip to demo is called") {
            val applicationConfigurationRepository = it.get<ApplicationConfigurationRepository>()
            val viewModel = OnboardingViewModel(
                appearancePreferencesRepository = it.get(),
                applicationConfigurationRepository = applicationConfigurationRepository,
                onboardingRepository = it.get(),
                validateBackendUriUseCase = it.get(),
                coroutineScope = testScope,
            )

            viewModel.skipToDemo()
            testScope.advanceUntilIdle()

            expectThat(applicationConfigurationRepository.isDemoMode.first())
                .isTrue()
        }

        test(name = "Should set onboarding finished when skip to demo is called") {
            val onboardingRepository = it.get<OnboardingRepository>()
            val viewModel = OnboardingViewModel(
                appearancePreferencesRepository = it.get(),
                applicationConfigurationRepository = it.get(),
                onboardingRepository = onboardingRepository,
                validateBackendUriUseCase = it.get(),
                coroutineScope = testScope,
            )

            viewModel.skipToDemo()
            testScope.advanceUntilIdle()

            expectThat(onboardingRepository.isOnboardingFinished.first())
                .isTrue()
        }

        test(name = "Should ignore skip to demo call when already processing") {
            val applicationConfigurationRepository = it.get<ApplicationConfigurationRepository>()
            val viewModel = OnboardingViewModel(
                appearancePreferencesRepository = it.get(),
                applicationConfigurationRepository = applicationConfigurationRepository,
                onboardingRepository = it.get(),
                validateBackendUriUseCase = it.get(),
                coroutineScope = testScope,
            )

            viewModel.uriTextFieldState.edit { append("https://example.com") }
            viewModel.finishOnboarding()
            testScope.runCurrent()

            viewModel.skipToDemo()
            testScope.advanceUntilIdle()

            expectThat(applicationConfigurationRepository.isDemoMode.first())
                .isFalse()
        }
    }
}
