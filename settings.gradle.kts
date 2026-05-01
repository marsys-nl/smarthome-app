import java.net.URI

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

        maven {
            name = "Central Portal Snapshots"
            url = URI.create("https://central.sonatype.com/repository/maven-snapshots/")

            mavenContent {
                snapshotsOnly()
            }

            content {
                includeModuleByRegex("dev\\.nmarsman\\.expect", "kotlin-expect-core.*")
            }
        }
    }
}

includeBuild("build-logic")

include(
    ":app:android",
    ":shared:data:connection",
    ":shared:domain:connection",
    ":shared:feature:onboarding",
    ":shared:library",
    ":shared:library:design",
    ":shared:library:network",
    ":shared:library:resources",
    ":shared:library:store",
    ":shared:library:store:datastore",
)
