rootProject.name = "smarthome-app"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
    }
}

includeBuild("build-logic")

include(
    ":app:android",
    ":shared:feature:onboarding",
    ":shared:library",
    ":shared:library:design",
    ":shared:library:resources",
    ":shared:library:store",
    ":shared:library:store:datastore",
)
