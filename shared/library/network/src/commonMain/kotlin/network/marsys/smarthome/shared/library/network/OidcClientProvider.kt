package network.marsys.smarthome.shared.library.network

import org.publicvalue.multiplatform.oidc.ExperimentalOpenIdConnect
import org.publicvalue.multiplatform.oidc.OpenIdConnectClient

@ExperimentalOpenIdConnect
interface OidcClientProvider {
    val client: OpenIdConnectClient?
}
