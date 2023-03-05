package infrastructure.repositories

import Constants
import core.domain.IEntity
import core.repositories.ICrudRepository
import core.repositories.IReadQuery
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.ktorm.schema.Column
import org.ktorm.schema.Table
import java.time.LocalDateTime
import java.time.OffsetDateTime

abstract class GenericRepository<TEntity, TId>(
    private val database: Database,
    table: Table<TEntity>
) : ICrudRepository<TEntity, TId>
        where TEntity : IEntity<TId>,
              TEntity : Entity<TEntity>,
              TId : Comparable<TId> {

    private val entitySequence = database.sequenceOf(table)

    override fun requestQuery(query: IReadQuery): List<TEntity> =
        database.useTransaction {
            entitySequence.filter { query.query }.toList()
        }

    override fun getAll(): List<TEntity> =
        database.useTransaction {
            entitySequence.toList()
        }

    override fun delete(id: TId): Boolean =
        database.useTransaction {
            entitySequence.removeIf { it["id"] as Column<TId> eq id } >= 0
        }

    override fun update(entity: TEntity) =
        database.useTransaction {
            entity.whenModified = LocalDateTime.now().toInstant(OffsetDateTime.now().offset)
            entitySequence.update(entity)
        }

    override fun add(entity: TEntity): TId =
        database.useTransaction {
            val time = LocalDateTime.now().toInstant(OffsetDateTime.now().offset)
            entity.apply {
                version = Constants.DB_VERSION.toInt()
                whenCreated = time
                whenModified = time
            }
            entitySequence.add(entity)
            entity.id
        }

    override fun getById(id: TId): TEntity? =
        database.useTransaction {
            entitySequence.singleOrNull { it["id"] as Column<TId> eq id }
        }
}
