package network.marsys.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import java.io.File

@Suppress("unused")
class ApplyIosVersionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val extension = extensions.create("applyIosVersion", ApplyIosVersionExtension::class.java, objects)

        afterEvaluate {
            val marketingVersion = extension.marketingVersion.orNull
                ?: throw IllegalStateException("marketingVersion must be provided in applyIosVersion extension.")
            val currentVersion = extension.currentVersion.orNull
                ?: throw IllegalStateException("currentVersion must be provided in applyIosVersion extension.")

            val applyIosVersionTask = tasks.register("applyIosVersion") {
                val directory = rootDir.resolve("app/ios/SmartHome/Configuration")
                generateFile(
                    targetDirectory = directory,
                    file = XCODE_CONFIG_FILE_NAME,
                    content = buildString {
                        appendLine("MARKETING_VERSION=$marketingVersion")
                        appendLine("CURRENT_PROJECT_VERSION=$currentVersion")
                    }
                )
            }

            tasks.matching { task ->
                task.name.contains("ios", ignoreCase = true) && (
                        task.name.contains("Link", ignoreCase = true)
                                || task.name.contains("Sync", ignoreCase = true)
                                || task.name.contains("Pack", ignoreCase = true)
                        )
            }.configureEach {
                dependsOn(applyIosVersionTask)
            }

            tasks.matching { task ->
                task.name.startsWith("compile") && task.name.contains("Kotlin", ignoreCase = true)
            }.configureEach {
                dependsOn(applyIosVersionTask)
            }
        }
    }

    private fun Task.generateFile(
        targetDirectory: File,
        file: String,
        content: String
    ) {
        val outputFile = targetDirectory.resolve(file)
        outputs.file(outputFile)

        doLast {
            targetDirectory.mkdirs()
            outputFile.writeText(content)
        }
    }

    companion object {
        private const val XCODE_CONFIG_FILE_NAME = "Versions.xcconfig"
    }
}
