package network.marsys.smarthome.shared.application.hub.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import network.marsys.smarthome.shared.application.hub.EstablishConnectionUseCaseImpl
import network.marsys.smarthome.shared.application.hub.ObserveConnectionUseCaseImpl
import network.marsys.smarthome.shared.application.hub.SmartHomeSession
import network.marsys.smarthome.shared.domain.connection.EstablishConnectionUseCase
import network.marsys.smarthome.shared.domain.connection.ObserveConnectionUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

val applicationHubModule = module {
    single {
        SmartHomeSession(
            source = get(),
            applicationConfigurationRepository = get(),
            scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
        )
    }

    single {
        EstablishConnectionUseCaseImpl(
            session = get(),
        )
    } binds arrayOf(
        EstablishConnectionUseCase::class,
    )

    single {
        ObserveConnectionUseCaseImpl(
            session = get(),
        )
    } binds arrayOf(
        ObserveConnectionUseCase::class,
    )
}
