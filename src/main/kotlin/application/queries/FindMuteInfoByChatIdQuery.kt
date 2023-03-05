package application.queries

import core.repositories.IReadQuery
import infrastructure.tables.Mutes
import org.ktorm.dsl.eq
import org.ktorm.expression.BinaryExpression

class FindMuteInfoByChatIdQuery(private val chatId: Long) : IReadQuery {
    override val query: BinaryExpression<Boolean>
        get() = Mutes.chatId eq chatId
}