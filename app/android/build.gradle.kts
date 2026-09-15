plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.marsys.smarthome.detekt)
}

group = "network.marsys.smarthome.app"
version = libs.versions.smarthome.app.name.get()

kotlin {
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())
}

android {
    namespace = "$group"

    compileSdk = libs.versions.android.sdk.compile.get().toInt()
    defaultConfig {
        applicationId = "$group"

        minSdk = libs.versions.android.sdk.min.get().toInt()
        targetSdk = libs.versions.android.sdk.target.get().toInt()

        versionCode = libs.versions.smarthome.app.code.get().toInt()
        versionName = "$version"
    }

    signingConfigs {
        create("release") {
            val keystorePath = requireNotNull(project.valueOf("KEYSTORE_PATH")) {
                "The keystore path isn't defined and should be defined in the local.properties or in the environment variables. Please define the KEYSTORE_PATH variable."
            }

            storeFile = file(keystorePath)
            storePassword = project.valueOf("KEYSTORE_PASSWORD")
            keyAlias = project.valueOf("KEY_ALIAS")
            keyPassword = project.valueOf("KEY_PASSWORD")
        }
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {
    implementation(projects.shared.library)
    implementation(projects.shared.library.store)
    implementation(projects.shared.library.store.datastore)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.datastore.preferences.core)

    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
}
