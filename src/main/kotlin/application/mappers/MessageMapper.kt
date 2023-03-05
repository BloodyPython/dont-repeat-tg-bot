package application.mappers

import application.dto.TgMessage
import core.domain.entities.DMessage

class MessageMapper : IMapper<TgMessage, DMessage> {
    override fun mapTo(obj: TgMessage): DMessage =
        DMessage {
            chatId = obj.chatId
            messageId = obj.messageId
            forwardFromChatId = obj.forwardFromChatId
            forwardFromMessageId = obj.forwardFromMessageId
            type = obj.type
            text = obj.text
        }
}