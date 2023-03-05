package infrastructure.repositories

import application.queries.FindFileByUniqueIdQuery
import core.domain.entities.DFile
import infrastructure.abstraction.IFilesRepository
import infrastructure.tables.Files
import org.ktorm.database.Database

class FilesRepository(
    database: Database
) : IFilesRepository, GenericRepository<DFile, Int>(database, Files) {
    override fun addFile(file: DFile): Int = add(file)

    override fun findFileByUniqueId(chatId: Long, uniqueId: String): DFile? =
        requestQuery(FindFileByUniqueIdQuery(chatId, uniqueId)).singleOrNull()
}