plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.marsys.smarthome.apply.ios.version) apply false
    alias(libs.plugins.marsys.smarthome.detekt) apply false
}

val ktlint: Configuration by configurations.creating

dependencies {
    ktlint(libs.ktlint) {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

tasks.register("ktlintCheck", JavaExec::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args(
        "**.kt",
        "**.kts",
        "!**/build/**",
    )
}
