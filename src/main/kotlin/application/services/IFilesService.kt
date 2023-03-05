package application.services

import core.domain.entities.DFile
import core.domain.entities.DMessage

interface IFilesService {
    fun addFile(newChatId: Long, newFileUniqueId: String, newMessage: DMessage): Int
    fun findFileByUniqueId(chatId: Long, uniqueId: String): DFile?
}