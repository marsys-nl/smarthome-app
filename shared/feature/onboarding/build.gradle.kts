plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.marsys.smarthome.detekt)
}

group = "network.marsys.smarthome.shared.feature.onboarding"
version = libs.versions.smarthome.app.name.get()
kotlin {
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    jvm()
    iosArm64()
    iosX64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.foundation)
        }
    }
}
