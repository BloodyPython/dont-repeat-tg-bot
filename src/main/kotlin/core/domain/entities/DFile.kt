package core.domain.entities

import core.domain.IEntity
import org.ktorm.entity.Entity

interface DFile : IEntity<Int>, Entity<DFile> {
    companion object : Entity.Factory<DFile>()

    var chatId: Long
    var fileUniqueId: String
    var message: DMessage
}