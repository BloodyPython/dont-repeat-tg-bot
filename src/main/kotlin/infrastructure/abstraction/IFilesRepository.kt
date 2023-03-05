package infrastructure.abstraction

import core.domain.entities.DFile

interface IFilesRepository {
    fun addFile(file: DFile): Int
    fun findFileByUniqueId(chatId: Long, uniqueId: String): DFile?
}