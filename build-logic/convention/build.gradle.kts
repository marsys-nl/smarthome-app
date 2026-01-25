plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.detekt)
}

val packageName = "network.marsys.conventions"
group = packageName

kotlin {
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("applyIosVersion") {
            id = "${packageName}.apply-ios-version"
            implementationClass = "network.marsys.conventions.ApplyIosVersionPlugin"
        }

        register("applyDetekt") {
            id = "${packageName}.detekt"
            implementationClass = "${packageName}.ApplyDetektPlugin"
        }
    }
}
