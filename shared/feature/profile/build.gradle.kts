plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.marsys.smarthome.detekt)
    alias(libs.plugins.test.balloon)
}

group = "network.marsys.smarthome.shared.feature.profile"
version = libs.versions.smarthome.app.name.get()
kotlin {
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    android {
        namespace = "$group"
        compileSdk = libs.versions.android.sdk.compile.get().toInt()

        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling)
            implementation(libs.compose.ui.tooling.preview)
        }

        commonMain.dependencies {
            implementation(projects.shared.library.core)
            implementation(projects.shared.library.design)
            implementation(projects.shared.library.i18n)
            implementation(projects.shared.library.navigation)
            implementation(projects.shared.library.resources)
            implementation(projects.shared.library.store)

            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.lifecycle.viewmodel)

            implementation(libs.compose.foundation)
            implementation(libs.compose.resources)
            implementation(libs.compose.unstyled.modal.bottom.sheet)
            implementation(libs.compose.ui.tooling.preview)

            implementation(libs.koin.compose.viewmodel)
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}
