package application.queries

import core.repositories.IReadQuery
import infrastructure.tables.Participants
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.expression.BinaryExpression

class FindParticipantIdQuery(
    private val chatId: Long,
    private val username: String
) : IReadQuery {
    override val query: BinaryExpression<Boolean>
        get() {
            val cond1 = Participants.chatId eq chatId
            val cond2 = Participants.username eq username

            return cond1 and cond2
        }
}