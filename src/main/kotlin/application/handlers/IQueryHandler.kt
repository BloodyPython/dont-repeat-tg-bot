package application.handlers

interface IQueryHandler<TEntity, TId> {
    val nextHandler: IQueryHandler<TEntity, TId>?
    fun handle(entity: TEntity): TId
}