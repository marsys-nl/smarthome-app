package network.marsys.smarthome.shared.infrastructure.authentication

import kotlinx.coroutines.CancellationException
import network.marsys.smarthome.shared.data.authentication.ports.outbound.Authenticator
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.Result.Companion.fail
import network.marsys.smarthome.shared.library.core.Result.Companion.succeed
import network.marsys.smarthome.shared.library.network.OidcClientProvider
import org.publicvalue.multiplatform.oidc.ExperimentalOpenIdConnect
import org.publicvalue.multiplatform.oidc.OpenIdConnectClient
import org.publicvalue.multiplatform.oidc.OpenIdConnectException
import org.publicvalue.multiplatform.oidc.flows.CodeAuthFlowFactory
import org.publicvalue.multiplatform.oidc.tokenstore.TokenRefreshHandler
import org.publicvalue.multiplatform.oidc.tokenstore.TokenStore
import org.publicvalue.multiplatform.oidc.types.remote.AccessTokenResponse
import org.publicvalue.multiplatform.oidc.util.accessTokenExpired
import org.publicvalue.multiplatform.oidc.util.refreshTokenExpired

@OptIn(ExperimentalOpenIdConnect::class)
internal class OidcAuthenticator(
    private val store: TokenStore,
    private val tokenRefreshHandler: TokenRefreshHandler,
    private val authenticationFlowFactory: CodeAuthFlowFactory,
) : Authenticator, OidcClientProvider {
    private var activeClientKey: OidcClientKey? = null
    private val clients = mutableMapOf<OidcClientKey, OpenIdConnectClient>()
    override val client: OpenIdConnectClient?
        get() = clients[activeClientKey]

    override suspend fun authenticate(
        issuer: String,
        clientIdentifier: String,
    ): Result<Unit, Authenticator.Reason> =
        try {
            authenticate(
                client = getOrCreateClient(
                    issuer = issuer,
                    clientIdentifier = clientIdentifier,
                ),
            )
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: OpenIdConnectException.AuthenticationCancelled) {
            fail(with = Authenticator.Reason.Cancelled)
        } catch (_: OpenIdConnectException.DiscoveryFailure) {
            fail(with = Authenticator.Reason.Unavailable)
        } catch (_: OpenIdConnectException.TechnicalFailure) {
            fail(with = Authenticator.Reason.Unavailable)
        } catch (_: OpenIdConnectException) {
            fail(with = Authenticator.Reason.Failed)
        }

    private suspend fun authenticate(
        client: OpenIdConnectClient,
    ): Result<Unit, Authenticator.Reason> {
        val tokens = store.getTokenResponse()
            ?: return authenticateByUser(client = client)

        return when {
            !tokens.accessTokenExpired() ->
                succeed(with = Unit)

            tokens.refresh_token == null || tokens.refreshTokenExpired() ->
                authenticateByUser(client = client)

            else ->
                authenticateByRefreshToken(
                    client = client,
                    tokens = tokens,
                )
        }
    }

    private suspend fun authenticateByRefreshToken(
        client: OpenIdConnectClient,
        tokens: AccessTokenResponse,
    ): Result<Unit, Authenticator.Reason> =
        try {
            tokenRefreshHandler.refreshAndSaveToken(
                client = client,
                oldAccessToken = tokens.access_token,
            )

            succeed(with = Unit)
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: OpenIdConnectException.TokenExpired) {
            authenticateByUser(client = client)
        } catch (_: OpenIdConnectException.UnsuccessfulTokenRequest) {
            authenticateByUser(client = client)
        }

    private suspend fun authenticateByUser(
        client: OpenIdConnectClient,
    ): Result<Unit, Authenticator.Reason> {
        val flow = authenticationFlowFactory.createAuthFlow(client = client)

        val tokens = if (flow.canContinueLogin()) {
            flow.continueLogin()
        } else {
            flow.getAccessToken()
        }

        store.saveTokens(tokens = tokens)

        return succeed(with = Unit)
    }

    override suspend fun invalidate() {
        clients.remove(activeClientKey)
        store.removeTokens()
    }

    private suspend fun getOrCreateClient(
        issuer: String,
        clientIdentifier: String,
    ): OpenIdConnectClient = OidcClientKey(
        issuer = issuer,
        clientIdentifier = clientIdentifier,
    ).let { key ->
        clients.getOrPut(key) {
            createClient(
                issuer = issuer,
                clientIdentifier = clientIdentifier,
            )
        }
    }

    private suspend fun createClient(
        issuer: String,
        clientIdentifier: String,
    ): OpenIdConnectClient =
        OpenIdConnectClient(
            discoveryUri = "${issuer.trimEnd('/')}/" + DISCOVERY_URI,
        ) {
            clientId = clientIdentifier
            scope = "openid profile email offline_access"
            redirectUri = REDIRECT_URI
        }.also {
            it.discover()
        }

    companion object {
        private const val REDIRECT_URI = "network.marsys.smarthome://oauth/callback"
        private const val DISCOVERY_URI = ".well-known/openid-configuration"
    }
}

private data class OidcClientKey(
    val issuer: String,
    val clientIdentifier: String,
)
