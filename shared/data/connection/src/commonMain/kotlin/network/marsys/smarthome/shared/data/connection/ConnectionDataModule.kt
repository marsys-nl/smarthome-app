package network.marsys.smarthome.shared.data.connection

import network.marsys.smarthome.shared.domain.connection.CheckSystemHealthUseCase
import network.marsys.smarthome.shared.domain.connection.DownloadConfigurationUseCase
import network.marsys.smarthome.shared.domain.connection.ValidateBackendUriUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val connectionDataModule = module {
    single<CheckSystemHealthUseCase> {
        CheckSystemHealthUseCaseImpl(
            client = get(named("smarthomeHttpClient")),
        )
    }

    single<DownloadConfigurationUseCase> {
        DownloadConfigurationUseCaseImpl(
            client = get(named("smarthomeHttpClient")),
        )
    }

    single<ValidateBackendUriUseCase> {
        ValidateBackendUriUseCaseImpl(
            client = get(named("genericHttpClient")),
        )
    }
}
