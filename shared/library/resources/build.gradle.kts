plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.marsys.smarthome.detekt)
}

group = "network.marsys.smarthome.shared.library.resources"
version = libs.versions.smarthome.app.name.get()

compose.resources {
    nameOfResClass = "SmartHomeRes"
    packageOfResClass = "network.marsys.smarthome.shared.library.resources"
    publicResClass = true
}

kotlin {
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    android {
        namespace = "$group"
        compileSdk = libs.versions.android.sdk.compile.get().toInt()

        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    jvm()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.foundation)
            implementation(libs.compose.resources)
        }
    }
}
