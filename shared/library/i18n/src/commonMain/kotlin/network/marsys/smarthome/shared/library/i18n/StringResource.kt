package network.marsys.smarthome.shared.library.i18n

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLocale
import network.marsys.smarthome.domain.EntityIdentifier
import network.marsys.smarthome.shared.library.resources.SmartHomeRes
import network.marsys.smarthome.shared.library.resources.unknown_entity
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
    composePluralStringResource(resource, quantity, quantity)

@Composable
fun stringResource(identifier: EntityIdentifier): String {
    val language = LocalLocale.current.language
    return LocalTranslationCache.current[identifier.value, language]
        ?: stringResource(SmartHomeRes.string.unknown_entity)
}

@Composable
fun stringResource(resource: StringResource): String {
    val language = LocalLocale.current.language
    return LocalTranslationCache.current[resource.key, language]
        ?: composeStringResource(resource)
}

@Composable
fun stringResource(resource: StringResource, vararg formatArgs: Any): String {
    val language = LocalLocale.current.language
    return LocalTranslationCache.current[resource.key, language]
        ?.replaceWithArgs(formatArgs.map(Any::toString))
        ?: composeStringResource(resource, *formatArgs)
}
