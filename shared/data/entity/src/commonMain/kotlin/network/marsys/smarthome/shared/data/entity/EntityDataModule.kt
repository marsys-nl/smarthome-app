package network.marsys.smarthome.shared.data.entity

import network.marsys.smarthome.shared.domain.entity.EntityRepository
import network.marsys.smarthome.shared.domain.entity.ZoneRepository
import org.koin.dsl.binds
import org.koin.dsl.module

val entityDataModule = module {
    single {
        DemoRepository()
    } binds arrayOf(
        EntityRepository::class,
        ZoneRepository::class,
    )
}
