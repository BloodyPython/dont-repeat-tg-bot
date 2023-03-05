package application.queries

import core.repositories.IReadQuery
import infrastructure.tables.Messages
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.expression.BinaryExpression

class FindMessageByTextComparisonQuery(private val chatId: Long, private val text: String) : IReadQuery {
    override val query: BinaryExpression<Boolean>
        get() {
            val cond1 = Messages.chatId eq chatId
            val cond2 = Messages.text eq text

            return cond1 and cond2
        }
}