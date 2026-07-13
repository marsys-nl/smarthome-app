import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.build.konfig)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.marsys.smarthome.detekt)
}

group = "network.marsys.smarthome.shared.library.core"
version = libs.versions.smarthome.app.name.get()

buildkonfig {
    packageName = "$group"
    exposeObjectWithName = "SmartHomeConfig"

    defaultConfigs {
        buildConfigField(
            type = STRING,
            name = "VERSION_NAME",
            value = "v${libs.versions.smarthome.app.name.get()}",
        )
    }
}

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
            implementation(projects.shared.domain.entity)

            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.runtime)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
