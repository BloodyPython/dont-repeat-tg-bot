package core.repositories

import core.domain.IEntity

interface ICrudRepository<TEntity, TId> : IQueryRepository<TEntity, TId>
        where TEntity : IEntity<TId>,
              TId : Comparable<TId> {
    fun getAll(): List<TEntity>
    fun getById(id: TId): TEntity?
    fun add(entity: TEntity): TId
    fun update(entity: TEntity): Int
    fun delete(id: TId): Boolean
}
