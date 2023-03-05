package application.queries

import core.repositories.IReadQuery
import infrastructure.tables.Participants
import org.ktorm.dsl.eq
import org.ktorm.expression.BinaryExpression

class IsChatAddedQuery(private val chatId: Long) : IReadQuery {
    override val query: BinaryExpression<Boolean>
        get() = Participants.chatId eq chatId
}