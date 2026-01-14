plugins {
    `kotlin-dsl`
}

group = "network.marsys.conventions"

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
            id = "network.marsys.conventions.apply-ios-version"
            implementationClass = "network.marsys.conventions.ApplyIosVersionPlugin"
        }
    }
}
