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
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.domain.entity.zone.Zone
import network.marsys.smarthome.shared.library.store.ApplicationConfigurationRepository
import network.marsys.smarthome.shared.library.store.model.ConnectionConfiguration

class DefaultEntityRepository(
    applicationConfigurationRepository: ApplicationConfigurationRepository,
    private val demoEntityDataSource: EntityDataSource,
    private val smartHomeEntityDataSource: EntityDataSource,
) : EntityRepository {
    private val source: Flow<EntityDataSource> =
        applicationConfigurationRepository.connection
            .map { configuration ->
                when (configuration) {
                    is ConnectionConfiguration.Demo ->
                        demoEntityDataSource

                    else ->
                        smartHomeEntityDataSource
                }
            }
            .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val entities: Flow<Collection<Entity<*>>> =
        source.flatMapLatest { it.entities }

    override val zones: Flow<Collection<Zone>> =
        entities
            .map { entities ->
                entities
                    .mapNotNull { it.zone }
                    .distinct()
            }
            .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun entity(identifier: EntityIdentifier): Flow<Entity<*>?> =
        source.flatMapLatest {
            it.entity(identifier)
        }

    override suspend fun execute(action: Entity.Action) {
        source.first().execute(action)
    }

    override suspend fun synchronize() =
        source.first().synchronize()
}
