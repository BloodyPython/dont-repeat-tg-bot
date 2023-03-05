package application.services

import application.dto.TgMessage
import application.handlers.MediaHandler
import application.handlers.ReferenceHandler
import application.handlers.TextHandler
import application.mappers.MessageMapper
import core.domain.entities.DMessage
import infrastructure.abstraction.IMessagesRepository

class MessagesService(
    private val messageRepository: IMessagesRepository
) : IMessagesService {

    private val messageMapper = MessageMapper()

    private val mediaHandler = MediaHandler()
    private val textHandler = TextHandler(mediaHandler)
    private val referenceHandler = ReferenceHandler(textHandler)

    override fun addMessage(tgMessage: TgMessage): Int =
        messageRepository.addMessage(messageMapper.mapTo(tgMessage))

    override fun findMessageIdById(id: Int): Long =
        messageRepository.findMessageIdById(id)

    override fun findMessageById(id: Int): DMessage? =
        messageRepository.findMessageById(id)

    override fun findMessageDuplicate(tgMessage: TgMessage): Int? =
        referenceHandler.handle(tgMessage)
}