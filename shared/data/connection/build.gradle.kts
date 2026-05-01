plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.marsys.smarthome.detekt)
    alias(libs.plugins.test.balloon)
}

group = "network.marsys.smarthome.shared.data.connection"
version = libs.versions.smarthome.app.name.get()

kotlin {
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    android {
        namespace = "$group"
        compileSdk = libs.versions.android.sdk.compile.get().toInt()
    }

    jvm()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.domain.connection)
            implementation(projects.shared.library.network)

            implementation(libs.koin.compose)

            implementation(libs.ktor.client.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.expect.core)
            implementation(libs.kotlin.test)

            implementation(libs.ktor.client.mock)

            implementation(libs.test.balloon.core)
        }
    }
}
