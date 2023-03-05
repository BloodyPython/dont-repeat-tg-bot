package application.queries

import core.repositories.IReadQuery
import infrastructure.tables.Messages
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.expression.BinaryExpression

class FindMessageDuplicateQuery(
    private val chatId: Long,
    private val forwardFromChatId: Long,
    private val forwardFromMessageId: Long
) : IReadQuery {
    override val query: BinaryExpression<Boolean>
        get() {
            val cond1 = Messages.chatId eq chatId
            val cond2 = Messages.forwardFromChatId eq forwardFromChatId
            val cond3 = Messages.forwardFromMessageId eq forwardFromMessageId

            return cond1 and cond2 and cond3
        }
}