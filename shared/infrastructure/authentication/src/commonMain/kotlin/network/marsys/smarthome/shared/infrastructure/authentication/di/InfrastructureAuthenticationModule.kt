package network.marsys.smarthome.shared.infrastructure.authentication.di

import network.marsys.smarthome.shared.data.authentication.ports.outbound.Authenticator
import network.marsys.smarthome.shared.infrastructure.authentication.OidcAuthenticator
import network.marsys.smarthome.shared.library.network.OidcClientProvider
import org.koin.core.module.Module
import org.koin.dsl.binds
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.ExperimentalOpenIdConnect
import org.publicvalue.multiplatform.oidc.tokenstore.TokenRefreshHandler

val infrastructureAuthenticationModule = module {
    includes(infrastructureAuthenticationModulePlatformModule)

    @OptIn(ExperimentalOpenIdConnect::class)
    single<TokenRefreshHandler> {
        TokenRefreshHandler(
            tokenStore = get(),
        )
    }

    @OptIn(ExperimentalOpenIdConnect::class)
    single {
        OidcAuthenticator(
            store = get(),
            tokenRefreshHandler = get(),
            authenticationFlowFactory = get(),
        )
    } binds arrayOf(
        Authenticator::class,
        OidcClientProvider::class,
    )
}

internal expect val infrastructureAuthenticationModulePlatformModule: Module
