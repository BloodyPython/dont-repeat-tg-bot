package infrastructure.repositories

import application.queries.FindMessageByTextComparisonQuery
import application.queries.FindMessageDuplicateQuery
import core.domain.entities.DMessage
import infrastructure.abstraction.IMessagesRepository
import infrastructure.tables.Messages
import org.ktorm.database.Database

class MessagesRepository(
    database: Database
) : IMessagesRepository, GenericRepository<DMessage, Int>(database, Messages) {

    override fun findMessageDuplicate(chatId: Long, forwardFromChatId: Long, forwardFromMessageId: Long): Int? =
        requestQuery(FindMessageDuplicateQuery(chatId, forwardFromChatId, forwardFromMessageId)).toList()
            .singleOrNull()?.id

    override fun addMessage(message: DMessage): Int = add(message)

    override fun findMessageIdById(id: Int): Long = getById(id)?.messageId ?: 0L

    override fun findMessageByTextComparison(chatId: Long, text: String): Int? =
        requestQuery(FindMessageByTextComparisonQuery(chatId, text)).singleOrNull()?.id

    override fun findMessageById(id: Int): DMessage? =
        getById(id)

}