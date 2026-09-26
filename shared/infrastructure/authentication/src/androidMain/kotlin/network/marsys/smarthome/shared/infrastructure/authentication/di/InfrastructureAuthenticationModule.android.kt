package network.marsys.smarthome.shared.infrastructure.authentication.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.ExperimentalOpenIdConnect
import org.publicvalue.multiplatform.oidc.tokenstore.AndroidSettingsTokenStore
import org.publicvalue.multiplatform.oidc.tokenstore.TokenStore

@OptIn(ExperimentalOpenIdConnect::class)
internal actual val infrastructureAuthenticationModulePlatformModule = module {
    single<TokenStore> {
        AndroidSettingsTokenStore(
            context = androidContext(),
        )
    }
}
