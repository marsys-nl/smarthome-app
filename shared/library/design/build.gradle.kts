plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.marsys.smarthome.detekt)
}

group = "network.marsys.smarthome.shared.library.design"
version = libs.versions.smarthome.app.name.get()

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
        androidMain.dependencies {
            api(libs.compose.ui.tooling)
            api(libs.compose.ui.tooling.preview)
        }

        commonMain.dependencies {
            api(libs.compose.foundation)
            api(libs.compose.ui.tooling.preview)
            implementation(libs.compose.unstyled)
            implementation(libs.compose.unstyled.theming)
            implementation(libs.compose.unstyled.primitives)
        }
    }
}
