plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.marsys.smarthome.detekt)
}

group = "network.marsys.smarthome.shared.library.navigation"
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
            implementation(libs.marsys.smarthome.domain)
        }
    }
}
