package infrastructure.tables

import core.domain.entities.DFile
import org.ktorm.schema.*

object Files : Table<DFile>("t_files") {
    val id = int("id").primaryKey().bindTo { it.id }
    val chatId = long("chat_id").bindTo { it.chatId }
    val fileUniqueId = text("file_unique_id").bindTo { it.fileUniqueId }
    val messageId = int("message_id").references(Messages) { it.message }
    val version = int("version").bindTo { it.version }
    val whenCreated = timestamp("created").bindTo { it.whenCreated }
    val whenModified = timestamp("modified").bindTo { it.whenModified }
}