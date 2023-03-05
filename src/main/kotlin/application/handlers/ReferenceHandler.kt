package application.handlers

import application.dto.TgMessage
import infrastructure.abstraction.IMessagesRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReferenceHandler(override val nextHandler: IQueryHandler<TgMessage, Int?>? = null) : IQueryHandler<TgMessage, Int?>,
    KoinComponent {

    private val messagesRepository by inject<IMessagesRepository>()

    override fun handle(entity: TgMessage): Int? {
        return messagesRepository.findMessageDuplicate(
            entity.chatId,
            entity.forwardFromChatId,
            entity.forwardFromMessageId
        ) ?: nextHandler?.handle(entity)
    }
}