package core.domain.entities

import core.domain.IEntity
import core.domain.enums.MessageType
import org.ktorm.entity.Entity

interface DMessage : IEntity<Int>, Entity<DMessage> {
    companion object : Entity.Factory<DMessage>()

    var chatId: Long
    var messageId: Long
    var forwardFromChatId: Long
    var forwardFromMessageId: Long
    var type: MessageType
    var text: String?
}