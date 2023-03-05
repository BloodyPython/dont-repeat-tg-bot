package application.queries

import core.repositories.IReadQuery
import infrastructure.tables.Files
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.expression.BinaryExpression

class FindFileByUniqueIdQuery(private val chatId: Long, private val uniqueId: String) : IReadQuery {
    override val query: BinaryExpression<Boolean>
        get() {
            val cond1 = Files.chatId eq chatId
            val cond2 = Files.fileUniqueId eq uniqueId

            return cond1 and cond2
        }
}