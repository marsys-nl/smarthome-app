plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.marsys.smarthome.detekt)
}

group = "network.marsys.smarthome.shared.infrastructure.authentication"
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
            implementation(projects.shared.library.core)
            implementation(projects.shared.data.authentication)

            implementation(libs.koin.compose)

            implementation(libs.oidc.appsupport)
            implementation(libs.oidc.core)
            implementation(libs.oidc.ktor)
            implementation(libs.oidc.tokenstore)
        }
    }
}
