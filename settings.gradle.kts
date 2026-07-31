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
                includeModuleByRegex("network\\.marsys\\.smarthome", "smarthome-.*")
            }
        }
    }
}

includeBuild("build-logic")

include(
    ":app:android",
    ":shared:data:connection",
    ":shared:data:entity",
    ":shared:domain:connection",
    ":shared:domain:entity",
    ":shared:feature:dashboard",
    ":shared:feature:onboarding",
    ":shared:feature:profile",
    ":shared:feature:zones",
    ":shared:library",
    ":shared:library:core",
    ":shared:library:design",
    ":shared:library:i18n",
    ":shared:library:i18n:memory",
    ":shared:library:navigation",
    ":shared:library:network",
    ":shared:library:resources",
    ":shared:library:store",
    ":shared:library:store:datastore",
    ":shared:modal:appearance",
    ":shared:modal:entity",
)
