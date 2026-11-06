package network.marsys.smarthome.shared.data.entity

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import network.marsys.smarthome.domain.entity.Entity
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.zone.Zone
import network.marsys.smarthome.domain.zone.ZoneIdentifier
import network.marsys.smarthome.shared.data.demo.DemoData
import network.marsys.smarthome.shared.data.entity.ports.inbound.EntityRepository
import network.marsys.smarthome.shared.data.entity.ports.inbound.EntitySynchronization
import network.marsys.smarthome.shared.data.entity.ports.inbound.ZoneRepository
import network.marsys.smarthome.shared.data.entity.ports.outbound.EntityDataSource
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.model.ConnectionConfiguration

internal class DefaultRepository(
    private val applicationConfigurationRepository: ApplicationConfigurationRepository,
    private val demoEntityDataSource: EntityDataSource,
    private val backendEntityDataSource: EntityDataSource,
) : EntityRepository, ZoneRepository {
    private val source: Flow<EntityDataSource> =
        applicationConfigurationRepository.connection
            .map(::determineEntityDataSource)
            .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val entities: Flow<Collection<Entity>> =
        source.flatMapLatest { it.entities }

    override val zones: Flow<Collection<Zone>> = flowOf(DemoData.zones)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun entity(identifier: EntityIdentifier): Flow<Entity?> =
        source
            .flatMapLatest { it.entity(identifier) }

    override fun zone(identifier: ZoneIdentifier): Flow<Zone?> =
        zones
            .map { collection ->
                collection
                    .firstOrNull { zone -> zone.identifier == identifier }
            }

    override suspend fun execute(action: Entity.Action) {
        determineEntityDataSource(
            configuration = applicationConfigurationRepository.connection.first(),
        ).execute(action)
    }

    override suspend fun synchronize(): Result<Unit, EntitySynchronization.Reason> {
        val dataSource = determineEntityDataSource(
            configuration = applicationConfigurationRepository.connection.first(),
        )

        return (dataSource as? EntitySynchronization)
            ?.synchronize() ?: Result.succeed(with = Unit)
    }

    private fun determineEntityDataSource(configuration: ConnectionConfiguration): EntityDataSource =
        when (configuration) {
            is ConnectionConfiguration.Demo -> demoEntityDataSource
            else -> backendEntityDataSource
        }
}
