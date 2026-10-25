package network.marsys.smarthome.shared.infrastructure.entity.di

import network.marsys.smarthome.shared.data.entity.entityDataModule
import network.marsys.smarthome.shared.data.entity.ports.inbound.EntitySynchronization
import network.marsys.smarthome.shared.data.entity.ports.outbound.EntityDataSource
import network.marsys.smarthome.shared.infrastructure.entity.DemoEntityDataSource
import network.marsys.smarthome.shared.infrastructure.entity.SmartHomeEntityDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val infrastructureEntityModule = module {
    single(named("demoEntityDataSource")) {
        DemoEntityDataSource()
    } bind EntityDataSource::class

    single(named("smartHomeEntityDataSource")) {
        SmartHomeEntityDataSource(
            client = get(named("smarthomeHttpClient")),
        )
    } binds arrayOf(
        EntityDataSource::class,
        SmartHomeEntityDataSource::class,
    )

    single {
        get<SmartHomeEntityDataSource>(named("smartHomeEntityDataSource"))
    } bind EntitySynchronization::class

    includes(entityDataModule)
}
