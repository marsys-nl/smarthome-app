package network.marsys.smarthome.shared.library.i18n

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLocale
import network.marsys.smarthome.domain.TranslationKey
import network.marsys.smarthome.shared.library.resources.SmartHomeRes
import network.marsys.smarthome.shared.library.resources.allPluralStringResources
import network.marsys.smarthome.shared.library.resources.allStringResources
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource as composePluralStringResource
import org.jetbrains.compose.resources.stringResource as composeStringResource

private val STRING_FORMAT_REGEX = Regex("""%(\d+)\$[ds]""")
internal fun String.replaceWithArgs(args: List<String>) = STRING_FORMAT_REGEX.replace(this) { matchResult ->
    args[matchResult.groupValues[1].toInt() - 1]
}

@Composable
fun pluralStringResource(resource: PluralStringResource, quantity: Int): String =
    pluralStringResource(resource.key, quantity, quantity)
        ?: composePluralStringResource(resource, quantity, quantity)

@Composable
fun pluralStringResource(resource: PluralStringResource, quantity: Int, vararg formatArgs: Any): String =
    pluralStringResource(resource.key, quantity, *formatArgs)
        ?: composePluralStringResource(resource, quantity, *formatArgs)

@Composable
fun stringResource(resource: StringResource): String {
    val language = LocalLocale.current.language
    return LocalTranslationCatalog.current[resource.key, language]
        ?: composeStringResource(resource)
}

@Composable
fun stringResource(resource: StringResource, vararg formatArgs: Any): String {
    val language = LocalLocale.current.language
    return LocalTranslationCatalog.current[resource.key, language]
        ?.replaceWithArgs(formatArgs.map(Any::toString))
        ?: composeStringResource(resource, *formatArgs)
}

@Composable
fun stringResource(
    key: TranslationKey,
    mode: TranslationMode = TranslationMode.LocationFirst,
): String {
    stringResource(key = key.value)?.let { translation ->
        return translation
    }

    val parts = key.value.split('.')
    val candidates = when (mode) {
        TranslationMode.LocationFirst -> parts.reversed()
        TranslationMode.TypeFirst -> parts
    }

    val translation = candidates
        .firstNotNullOfOrNull { stringResource(key = it) }

    return translation ?: key.value
}

@Composable
private fun stringResource(key: String): String? =
    stringResource(key = key, formatArgs = emptyArray())

@Composable
private fun stringResource(key: String, vararg formatArgs: Any): String? {
    val language = LocalLocale.current.language

    LocalTranslationCatalog.current[key, language]?.let { translation ->
        return translation
            .replaceWithArgs(formatArgs.map(Any::toString))
    }

    val formattedResourceName = key
        .replace('.', '_')
        .replace('-', '_')

    return SmartHomeRes.allStringResources[formattedResourceName]?.let {
        composeStringResource(it, *formatArgs)
    }
}

@Composable
private fun pluralStringResource(key: String, quantity: Int, vararg formatArgs: Any): String? {
    val language = LocalLocale.current.language
    val category = PluralRules.categoryFor(language, quantity).name.lowercase()

    LocalTranslationCatalog.current[key, category, language]?.let { translation ->
        return translation
            .replaceWithArgs(formatArgs.map(Any::toString))
    }

    val formattedResourceName = key
        .replace('.', '_')
        .replace('-', '_')

    return SmartHomeRes.allPluralStringResources[formattedResourceName]?.let {
        composePluralStringResource(it, quantity, *formatArgs)
    }
}

sealed interface TranslationMode {
    data object TypeFirst : TranslationMode
    data object LocationFirst : TranslationMode
}

enum class PluralCategory {
    ZERO,
    ONE,
    TWO,
    FEW,
    MANY,
    OTHER,
}

fun interface PluralRules {
    fun categoryFor(quantity: Int): PluralCategory

    companion object {
        private val oneOrOther = PluralRules { quantity ->
            when (quantity) {
                1 -> PluralCategory.ONE
                else -> PluralCategory.OTHER
            }
        }

        private val rules: Map<String, PluralRules> = mapOf(
            "en" to oneOrOther,
            "nl" to oneOrOther,
        )

        fun categoryFor(language: String, quantity: Int): PluralCategory =
            (rules[language] ?: oneOrOther).categoryFor(quantity = quantity)
    }
}
