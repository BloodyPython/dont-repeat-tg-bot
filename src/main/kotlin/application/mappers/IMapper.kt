package application.mappers

interface IMapper<in TIn, out TOut> {
    fun mapTo(obj: TIn): TOut
}