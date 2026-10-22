package network.marsys.smarthome.shared.infrastructure.entity.di

import network.marsys.smarthome.shared.data.entity.entityDataModule
import network.marsys.smarthome.shared.data.entity.ports.outbound.EntityDataSource
import network.marsys.smarthome.shared.infrastructure.entity.DemoEntityDataSource
import network.marsys.smarthome.shared.infrastructure.entity.SmartHomeEntityDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val infrastructureEntityModule = module {
    single<EntityDataSource>(named("demoEntityDataSource")) {
        DemoEntityDataSource()
    }

    single<EntityDataSource>(named("smartHomeEntityDataSource")) {
        SmartHomeEntityDataSource(
            client = get(named("smarthomeHttpClient")),
        )
    }

    includes(entityDataModule)
}
