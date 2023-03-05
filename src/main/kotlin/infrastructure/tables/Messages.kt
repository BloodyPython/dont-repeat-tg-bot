package infrastructure.tables

import core.domain.entities.DMessage
import core.domain.enums.MessageType
import org.ktorm.schema.*

object Messages : Table<DMessage>("t_messages") {
    val id = int("id").primaryKey().bindTo { it.id }
    val chatId = long("chat_id").bindTo { it.chatId }
    val messageId = long("message_id").bindTo { it.messageId }
    val forwardFromChatId = long("forward_from_chat_id").bindTo { it.forwardFromChatId }
    val forwardFromMessageId = long("forward_from_message_id").bindTo { it.forwardFromMessageId }
    val type = enum<MessageType>("type").bindTo { it.type }
    val text = text("text").bindTo { it.text }
    val version = int("version").bindTo { it.version }
    val whenCreated = timestamp("created").bindTo { it.whenCreated }
    val whenModified = timestamp("modified").bindTo { it.whenModified }
}