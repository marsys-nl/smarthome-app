plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.marsys.smarthome.apply.ios.version)
    alias(libs.plugins.marsys.smarthome.detekt)
}

group = "network.marsys.smarthome.shared.library"
version = libs.versions.smarthome.app.name.get()

applyIosVersion {
    marketingVersion = libs.versions.smarthome.app.name.get()
    currentVersion = libs.versions.smarthome.app.code.get().toInt()
}

kotlin {
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    android {
        namespace = "$group"
        compileSdk = libs.versions.android.sdk.compile.get().toInt()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "SmartHomeApp"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.data.connection)
            implementation(projects.shared.data.entity)
            implementation(projects.shared.domain.connection)
            implementation(projects.shared.domain.entity)
            implementation(projects.shared.feature.dashboard)
            implementation(projects.shared.feature.onboarding)
            implementation(projects.shared.feature.profile)
            implementation(projects.shared.library.core)
            implementation(projects.shared.library.design)
            implementation(projects.shared.library.i18n)
            implementation(projects.shared.library.i18n.memory)
            implementation(projects.shared.library.navigation)
            implementation(projects.shared.library.network)
            implementation(projects.shared.library.resources)
            implementation(projects.shared.library.store)
            implementation(projects.shared.library.store.datastore)
            implementation(projects.shared.modal.appearance)
            implementation(projects.shared.modal.entity)

            implementation(libs.androidx.datastore.preferences.core)
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.navigation.ui)

            implementation(libs.compose.foundation)
            implementation(libs.compose.resources)
            implementation(libs.compose.runtime)

            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.kotlinx.serialization.json)

            implementation(libs.marsys.smarthome.domain)
        }
    }
}
