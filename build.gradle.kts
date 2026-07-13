import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.GroupingEntityType

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.multiplatform.library) apply false
    alias(libs.plugins.build.konfig) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kotlin.kover) apply true
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.marsys.smarthome.apply.ios.version) apply false
    alias(libs.plugins.marsys.smarthome.detekt) apply false
    alias(libs.plugins.test.balloon) apply false
}

val ktlint: Configuration by configurations.creating

dependencies {
    ktlint(libs.ktlint) {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

kover {
    merge {
        allProjects {
            it.buildFile.exists()
        }
    }

    reports {
        filters {
            excludes {
                classes.addAll(
                    "*.generated.resources.*",
                    // Ignored as the scaffold is a composable method (it calculates placement, but as it is composable, it should be ui-tested)
                    "network.marsys.smarthome.shared.feature.onboarding.components.OnboardingScreenScaffoldKt",
                    // Ignored as the ColorScheme is a mere data class that conveys colors.
                    "network.marsys.smarthome.shared.library.design.theme.ColorScheme",
                )

                annotatedBy.addAll(
                    "androidx.compose.runtime.Composable",
                )
            }
        }

        verify {
            rule {
                groupBy.set(GroupingEntityType.CLASS)

                minBound(
                    minValue = 100,
                    coverageUnits = CoverageUnit.BRANCH,
                    aggregationForGroup = AggregationType.COVERED_PERCENTAGE,
                )
            }
        }
    }
}

tasks.register("ktlintCheck", JavaExec::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args(
        "**.kt",
        "**.kts",
        "!**/build/**",
    )
}
