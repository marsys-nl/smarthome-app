package network.marsys.smarthome.shared.infrastructure.entity.di

import network.marsys.smarthome.shared.data.entity.entityDataModule
import network.marsys.smarthome.shared.data.entity.ports.outbound.EntityDataSource
import network.marsys.smarthome.shared.domain.entity.EntitySnapshotUseCase
import network.marsys.smarthome.shared.infrastructure.entity.SmartHomeEntityDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val infrastructureEntityModule = module {
    single<EntitySnapshotUseCase> {
        EntitySnapshotUseCaseImpl(
            client = get(named("smarthomeHttpClient")),
        )
    }

    single<EntityDataSource>(named("smartHomeEntityDataSource")) {
        SmartHomeEntityDataSource(
            entitySnapshotUseCase = get()
        )
    }

    includes(entityDataModule)
}
