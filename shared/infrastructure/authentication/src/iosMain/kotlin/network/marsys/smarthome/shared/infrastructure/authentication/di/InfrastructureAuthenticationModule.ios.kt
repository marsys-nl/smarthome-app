package network.marsys.smarthome.shared.infrastructure.authentication.di

import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.ExperimentalOpenIdConnect
import org.publicvalue.multiplatform.oidc.appsupport.IosCodeAuthFlowFactory
import org.publicvalue.multiplatform.oidc.flows.CodeAuthFlowFactory
import org.publicvalue.multiplatform.oidc.tokenstore.IosKeychainTokenStore
import org.publicvalue.multiplatform.oidc.tokenstore.TokenStore

@OptIn(ExperimentalOpenIdConnect::class)
internal actual val infrastructureAuthenticationModulePlatformModule = module {
    single<CodeAuthFlowFactory> {
        IosCodeAuthFlowFactory()
    }

    single<TokenStore> {
        IosKeychainTokenStore()
    }
}
