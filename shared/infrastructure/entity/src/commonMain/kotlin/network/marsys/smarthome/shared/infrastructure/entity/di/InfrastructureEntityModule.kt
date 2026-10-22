package network.marsys.smarthome.shared.infrastructure.entity.di

import network.marsys.smarthome.shared.data.entity.entityDataModule
import network.marsys.smarthome.shared.data.entity.ports.outbound.EntityDataSource
import network.marsys.smarthome.shared.infrastructure.entity.DemoEntityDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val infrastructureEntityModule = module {
    single<EntityDataSource>(named("demoEntityDataSource")) {
        DemoEntityDataSource()
    }

    single<EntityDataSource>(named("backendEntityDataSource")) {
        DemoEntityDataSource() // Temporary
    }

    includes(entityDataModule)
}
