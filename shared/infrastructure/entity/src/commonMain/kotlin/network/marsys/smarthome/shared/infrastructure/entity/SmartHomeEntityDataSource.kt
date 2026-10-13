package network.marsys.smarthome.shared.infrastructure.entity

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import network.marsys.smarthome.domain.identifiers.EntityIdentifier
import network.marsys.smarthome.shared.data.entity.ports.outbound.EntityDataSource
import network.marsys.smarthome.shared.domain.entity.EntityRepository
import network.marsys.smarthome.shared.domain.entity.EntitySnapshotUseCase
import network.marsys.smarthome.shared.domain.entity.entity.Entity
import network.marsys.smarthome.shared.library.core.Result
import network.marsys.smarthome.shared.library.core.Result.Companion.fail
import network.marsys.smarthome.shared.library.core.Result.Companion.succeed

class SmartHomeEntityDataSource(
    private val entitySnapshotUseCase: EntitySnapshotUseCase,
) : EntityDataSource {
    private val state =
        MutableStateFlow<Map<EntityIdentifier, Entity<*>>>(emptyMap())

    override val entities: Flow<Collection<Entity<*>>> =
        state.map { it.values }

    override fun entity(
        identifier: EntityIdentifier,
    ): Flow<Entity<*>?> =
        state
            .map { it[identifier] }
            .distinctUntilChanged()

    override suspend fun synchronize(): Result<Unit, EntityRepository.Reason> =
        when (val snapshot = entitySnapshotUseCase.invoke()) {
            is Result.Success -> succeed(
                with = state.update {
                    it + snapshot.value.associateBy { it.identifier }
                },
            )

            is Result.Failure -> fail(
                with = when (snapshot.value) {
                    EntitySnapshotUseCase.Reason.InvalidData ->
                        EntityRepository.Reason.InvalidData

                    EntitySnapshotUseCase.Reason.Unreachable ->
                        EntityRepository.Reason.Unreachable

                    EntitySnapshotUseCase.Reason.Unauthenticated ->
                        EntityRepository.Reason.Unauthenticated
                },
            )
        }

    override suspend fun execute(action: Entity.Action) {
        TODO("Entity actions are not implemented yet")
    }
}
