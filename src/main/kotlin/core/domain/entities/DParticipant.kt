package core.domain.entities

import core.domain.IEntity
import org.ktorm.entity.Entity

interface DParticipant : IEntity<Int>, Entity<DParticipant> {
    companion object : Entity.Factory<DParticipant>()

    var userId: Long
    var chatId: Long
    var username: String
    var counter: Int
}