package core.domain.entities
import core.domain.IEntity
import org.ktorm.entity.Entity

interface DMuteInfo : IEntity<Int>, Entity<DMuteInfo> {
    companion object : Entity.Factory<DMuteInfo>()

    var chatId: Long
    var userId: Long
    var username: String
    var quota: Int
    var votePositive: Int
    var voteNegative: Int
    var duration: Int

}