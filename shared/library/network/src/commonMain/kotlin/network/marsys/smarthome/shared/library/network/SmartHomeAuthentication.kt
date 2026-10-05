package network.marsys.smarthome.shared.library.network

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.http.HttpHeaders
import io.ktor.util.AttributeKey
import org.publicvalue.multiplatform.oidc.ExperimentalOpenIdConnect
import org.publicvalue.multiplatform.oidc.tokenstore.TokenRefreshHandler
import org.publicvalue.multiplatform.oidc.tokenstore.TokenStore
import org.publicvalue.multiplatform.oidc.types.remote.AccessTokenResponse
import org.publicvalue.multiplatform.oidc.util.accessTokenExpired

@ExperimentalOpenIdConnect
internal val SmartHomeAuthentication = createClientPlugin(
    name = "SmartHomeAuthentication",
    createConfiguration = ::SmartHomeAuthenticationConfiguration,
) {
    val store = requireNotNull(pluginConfig.store) {
        "Token store is not provided."
    }
    val refreshHandler = requireNotNull(pluginConfig.refreshHandler) {
        "Token refresh handler is not provided."
    }
    val provider = requireNotNull(pluginConfig.provider) {
        "Oidc client provider is not provided."
    }

    onRequest { request, _ ->
        if (request.attributes.getOrNull(SmartHomeInitializationRequestAttributeKey) == true) {
            return@onRequest
        }

        val tokens = store.getValidatedTokens(
            provider = provider,
            refreshHandler = refreshHandler,
        ) ?: return@onRequest

        request.headers.append(
            name = HttpHeaders.Authorization,
            value = "Bearer ${tokens.access_token}",
        )
    }
}

@OptIn(ExperimentalOpenIdConnect::class)
private suspend fun TokenStore.getValidatedTokens(
    provider: OidcClientProvider,
    refreshHandler: TokenRefreshHandler,
): AccessTokenResponse? =
    with(
        receiver = getTokenResponse()
            ?: return null,
    ) {
        if (accessTokenExpired()) {
            val client = provider.client ?: return null

            refreshHandler.refreshAndSaveToken(
                client = client,
                oldAccessToken = access_token,
            )

            return getTokenResponse()
        } else {
            this
        }
    }

val SmartHomeInitializationRequestAttributeKey = AttributeKey<Boolean>("SmartHomeInitializationRequest")

@ExperimentalOpenIdConnect
internal class SmartHomeAuthenticationConfiguration {
    var store: TokenStore? = null
    var refreshHandler: TokenRefreshHandler? = null
    var provider: OidcClientProvider? = null
}
