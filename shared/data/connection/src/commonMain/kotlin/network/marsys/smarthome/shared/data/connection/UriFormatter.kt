package network.marsys.smarthome.shared.data.connection

import io.ktor.http.Url
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.Result.Companion.fail
import network.marsys.smarthome.shared.library.core.Result.Companion.succeed

internal fun formatUri(uri: String?): Result<Url, Unit> = try {
    val uri = requireNotNull(uri) {
        "URI cannot be null"
    }

    require(uri.isNotBlank()) {
        "URI cannot be blank"
    }

    val url = Url(
        urlString = uri.trim()
            .let { trimmed ->
                if (!trimmed.contains("://")) "https://$trimmed" else trimmed
            }
            .trimEnd('/'),
    )

    succeed(with = url)
} catch (_: IllegalArgumentException) {
    fail(with = Unit)
}
