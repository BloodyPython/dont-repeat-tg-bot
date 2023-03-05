package application.services

import core.domain.entities.DFile
import core.domain.entities.DMessage
import infrastructure.abstraction.IFilesRepository

class FilesService(
    private val filesRepository: IFilesRepository
) : IFilesService {
    override fun addFile(newChatId: Long, newFileUniqueId: String, newMessage: DMessage): Int =
        filesRepository.addFile(DFile {
            chatId = newChatId
            fileUniqueId = newFileUniqueId
            message = newMessage
        })

    override fun findFileByUniqueId(chatId: Long, uniqueId: String): DFile? =
        filesRepository.findFileByUniqueId(chatId, uniqueId)
}