package network.marsys.conventions

import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import network.marsys.conventions.util.plugin
import network.marsys.conventions.util.library
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class ApplyDetektPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(plugin("detekt"))

        dependencies {
            detektPlugins(library("detekt.compose.rules"))
        }

        extensions.configure(DetektExtension::class) {
            allRules.set(true)
            buildUponDefaultConfig.set(true)

            config.setFrom(rootProject.projectDir.resolve("config/detekt/detekt.yml"))
        }

        afterEvaluate {
            tasks.withType(Detekt::class.java).configureEach {
                setSource(files(
                    "src/main/kotlin",
                    "src/test/kotlin",
                    "src/commonMain/kotlin",
                    "src/commonTest/kotlin",
                    "src/androidMain/kotlin",
                    "src/androidTest/kotlin",
                    "src/iosMain/kotlin",
                    "src/iosTest/kotlin",
                ))
            }
        }
    }

    private fun DependencyHandlerScope.detektPlugins(name: Provider<MinimalExternalModuleDependency>) {
        add("detektPlugins", name)
    }
}
