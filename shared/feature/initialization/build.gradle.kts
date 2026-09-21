plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.marsys.smarthome.detekt)
    alias(libs.plugins.test.balloon)
}

group = "network.marsys.smarthome.shared.feature.initialization"
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
            implementation(projects.shared.library.design)
            implementation(projects.shared.library.design.domain)

            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.lifecycle.viewmodel)

            implementation(libs.compose.foundation)
            implementation(libs.compose.ui.tooling.preview)
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}
