plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.marsys.smarthome.apply.ios.version)
}

group = "network.marsys.smarthome.shared.library"
version = libs.versions.smarthome.app.name.get()

applyIosVersion {
    marketingVersion = libs.versions.smarthome.app.name.get()
    currentVersion = libs.versions.smarthome.app.code.get().toInt()
}

kotlin {
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    jvm()

    listOf(
        iosArm64(),
        iosX64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "SmartHomeApp"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.foundation)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.runtime)
        }
    }
}
