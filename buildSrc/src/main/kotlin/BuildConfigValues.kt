import org.gradle.api.Project
import java.util.Properties

val propertiesFactory: Project.() -> Properties = {
    Properties().apply {
        rootProject.file("local.properties")
            .takeIf { it.exists() }
            ?.inputStream()
            ?.use { load(it) }
    }
}

fun Project.valueOf(key: String, properties: Properties = propertiesFactory.invoke(this)): String? =
    properties.getProperty(key) ?: providers.environmentVariable(key).orNull
