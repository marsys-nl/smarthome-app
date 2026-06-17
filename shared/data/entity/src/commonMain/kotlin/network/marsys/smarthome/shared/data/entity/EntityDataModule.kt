package network.marsys.smarthome.shared.data.entity

import network.marsys.smarthome.shared.domain.entity.EntityRepository
import org.koin.dsl.binds
import org.koin.dsl.module

val entityDataModule = module {
    single {
        DemoEntityRepository()
    } binds arrayOf(
        EntityRepository::class,
    )
}
