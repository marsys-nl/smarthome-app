package network.marsys.smarthome.shared.feature.onboarding

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isEqualTo

val OnboardingScreensOrderTest by testSuite(
    displayName = "Onboarding screens order tests",
) {
    test(name = "Should return 1 when index of initial screen is requested") {
        expectThat(OnboardingScreens.indexOf(OnboardingScreens.Initial))
            .isEqualTo(1)
    }

    test(name = "Should return 2 when index of entities screen is requested") {
        expectThat(OnboardingScreens.indexOf(OnboardingScreens.Entities))
            .isEqualTo(2)
    }

    test(name = "Should return 3 when index of entities screen is requested") {
        expectThat(OnboardingScreens.indexOf(OnboardingScreens.Appearance))
            .isEqualTo(3)
    }

    test(name = "Should return 4 when index of entities screen is requested") {
        expectThat(OnboardingScreens.indexOf(OnboardingScreens.Configuration))
            .isEqualTo(4)
    }
}
