package network.marsys.smarthome.shared.data.authentication.di

import network.marsys.smarthome.shared.data.authentication.AuthenticateUseCaseImpl
import network.marsys.smarthome.shared.domain.authentication.AuthenticateUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val applicationAuthenticationModule = module {
    singleOf(::AuthenticateUseCaseImpl) bind AuthenticateUseCase::class
}
