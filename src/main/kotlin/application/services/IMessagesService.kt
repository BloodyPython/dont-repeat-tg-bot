package application.services

import application.dto.TgMessage
import core.domain.entities.DMessage

interface IMessagesService {
    fun findMessageDuplicate(tgMessage: TgMessage): Int?
    fun addMessage(tgMessage: TgMessage): Int
    fun findMessageIdById(id: Int): Long
    fun findMessageById(id: Int): DMessage?
}