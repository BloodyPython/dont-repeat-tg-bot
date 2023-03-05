package infrastructure.tables

import core.domain.entities.DMuteInfo
import org.ktorm.schema.*

object Mutes : Table<DMuteInfo>("t_mutes") {
    val id = int("id").primaryKey().bindTo { it.id }
    val chatId = long("chat_id").bindTo { it.chatId }
    val userId = long("user_id").bindTo { it.userId }
    val username = text("username").bindTo { it.username }
    val quota = int("quota").bindTo { it.quota }
    val votePositive = int("vote_positive").bindTo { it.votePositive }
    val voteNegative = int("vote_negative").bindTo { it.voteNegative }
    val duration = int("duration").bindTo { it.duration }
    val version = int("version").bindTo { it.version }
    val whenCreated = timestamp("created").bindTo { it.whenCreated }
    val whenModified = timestamp("modified").bindTo { it.whenModified }
}