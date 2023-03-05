package core.repositories

import core.domain.IEntity

interface IQueryRepository<TEntity, TId>
        where TEntity : IEntity<TId>,
              TId : Comparable<TId> {
    fun requestQuery(query: IReadQuery): List<TEntity>
}
