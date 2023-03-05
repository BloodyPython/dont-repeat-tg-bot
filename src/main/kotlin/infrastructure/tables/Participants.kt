package infrastructure.tables

import core.domain.entities.DParticipant
import org.ktorm.schema.*

object Participants : Table<DParticipant>("t_participants") {
    val id = int("id").primaryKey().bindTo { it.id }
    val userId = long("user_id").bindTo { it.userId }
    val chatId = long("chat_id").bindTo { it.chatId }
    val username = text("username").bindTo { it.username }
    val counter = int("counter").bindTo { it.counter }
    val version = int("version").bindTo { it.version }
    val whenCreated = timestamp("created").bindTo { it.whenCreated }
    val whenModified = timestamp("modified").bindTo { it.whenModified }
}