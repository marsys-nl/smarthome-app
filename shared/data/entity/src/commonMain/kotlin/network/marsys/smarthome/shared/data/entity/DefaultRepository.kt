package network.marsys.smarthome.shared.data.entity

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.data.entity.ports.outbound.EntityDataSource
import network.marsys.smarthome.shared.domain.entity.EntityRepository
import network.marsys.smarthome.shared.domain.entity.ZoneRepository
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.zone.Zone
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
    override val entities: Flow<Collection<Entity<*>>> =
        source.flatMapLatest { it.entities }

    override val zones: Flow<Collection<Zone>> =
        entities
            .map { collection ->
                collection
                    .mapNotNull { entity -> entity.zone }
                    .distinctBy { it.identifier }
            }
            .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun entity(identifier: EntityIdentifier): Flow<Entity<*>?> =
        source
            .flatMapLatest { it.entity(identifier) }

    override fun zone(identifier: EntityIdentifier): Flow<Zone?> =
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

    private fun determineEntityDataSource(configuration: ConnectionConfiguration): EntityDataSource =
        when (configuration) {
            is ConnectionConfiguration.Demo -> demoEntityDataSource
            else -> backendEntityDataSource
        }
}
