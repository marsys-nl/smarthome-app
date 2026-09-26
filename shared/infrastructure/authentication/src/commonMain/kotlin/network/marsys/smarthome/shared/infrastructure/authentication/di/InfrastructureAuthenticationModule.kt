package network.marsys.smarthome.shared.infrastructure.authentication.di

import network.marsys.smarthome.shared.data.authentication.ports.outbound.Authenticator
import network.marsys.smarthome.shared.infrastructure.authentication.OidcAuthenticator
import org.koin.core.module.Module
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.ExperimentalOpenIdConnect

val infrastructureAuthenticationModule = module {
    includes(infrastructureAuthenticationModulePlatformModule)

    single<Authenticator> {
        @OptIn(ExperimentalOpenIdConnect::class)
        OidcAuthenticator(
            store = get(),
            authenticationFlowFactory = get(),
        )
    }
}

internal expect val infrastructureAuthenticationModulePlatformModule: Module
