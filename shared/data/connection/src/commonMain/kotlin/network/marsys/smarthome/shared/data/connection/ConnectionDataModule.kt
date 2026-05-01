package network.marsys.smarthome.shared.data.connection

import network.marsys.smarthome.shared.domain.connection.ValidateBackendUriUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val connectionDataModule = module {
    singleOf(::ValidateBackendUriUseCaseImpl) bind ValidateBackendUriUseCase::class
}
