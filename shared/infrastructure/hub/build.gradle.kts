plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.marsys.smarthome.detekt)
    alias(libs.plugins.test.balloon)
}

group = "network.marsys.smarthome.shared.infrastructure.hub"
version = libs.versions.smarthome.app.name.get()

kotlin {
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    android {
        namespace = "$group"
        compileSdk = libs.versions.android.sdk.compile.get().toInt()
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.application.hub)

            implementation(libs.koin.compose)

            implementation(libs.kotlinx.serialization.json)

            implementation(libs.ktor.client.core)

            implementation(libs.marsys.smarthome.api)
            implementation(libs.marsys.smarthome.domain)
        }
    }
}
