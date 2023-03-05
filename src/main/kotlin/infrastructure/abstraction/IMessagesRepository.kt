package infrastructure.abstraction

import core.domain.entities.DMessage

interface IMessagesRepository {
    fun findMessageDuplicate(chatId: Long, forwardFromChatId: Long, forwardFromMessageId: Long): Int?
    fun addMessage(message: DMessage): Int
    fun findMessageIdById(id: Int): Long
    fun findMessageByTextComparison(chatId: Long, text: String): Int?
    fun findMessageById(id: Int): DMessage?
}