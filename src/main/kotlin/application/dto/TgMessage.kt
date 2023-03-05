package application.dto

import core.domain.enums.MessageType
import dev.inmo.tgbotapi.extensions.utils.asMediaCollectionContent
import dev.inmo.tgbotapi.extensions.utils.asTextContent
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.forward_from_chat
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.forward_from_message_id
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.*
import dev.inmo.tgbotapi.types.message.textsources.MentionTextSource
import dev.inmo.tgbotapi.types.message.textsources.TextLinkTextSource

class TgMessage private constructor(
    val chatId: Long,
    val messageId: Long,
    val forwardFromChatId: Long,
    val forwardFromMessageId: Long,
    val type: MessageType,
    val text: String?,
    val textLinkSources: List<TextLinkSource>?,
    val fileUniqueId: List<String>
) {
    companion object {
        fun fromCommonMessage(commonMessage: CommonMessage<*>): TgMessage {
            val mappedTypeAndText = mapToMessageType(commonMessage.content)
            val foundFileUniqueId = when (mappedTypeAndText.first) {
                MessageType.PHOTO, MessageType.VIDEO, MessageType.MEDIA -> commonMessage.content
                    .asMediaCollectionContent()?.mediaCollection?.map { it.fileUniqueId } ?: emptyList()
                else -> emptyList()
            }

            return TgMessage(
                commonMessage.chat.id.chatId,
                commonMessage.messageId,
                commonMessage.forward_from_chat?.id?.chatId ?: 0,
                commonMessage.forward_from_message_id ?: 0,
                mappedTypeAndText.first,
                mappedTypeAndText.second,
                mapTextLinkSources(commonMessage.content),
                foundFileUniqueId
            )
        }

        private fun mapToMessageType(content: MessageContent): Pair<MessageType, String?> = when (content) {
            is TextContent -> MessageType.TEXT to content.text
            is PhotoContent -> MessageType.PHOTO to content.text
            is VideoContent -> MessageType.VIDEO to content.text
            is MediaContent -> MessageType.MEDIA to content.asTextContent()?.text
            else -> MessageType.OTHER to content.asTextContent()?.text
        }

        private fun mapTextLinkSources(content: MessageContent?): List<TextLinkSource>? {
            if (content == null) return null

            val textSources = emptyList<TextLinkTextSource>().toMutableList()
            val mentionsSources = emptyList<MentionTextSource>().toMutableList()

            when (content) {
                is TextContent -> {
                    textSources.addAll(content.textSources.filterIsInstance<TextLinkTextSource>())
                    mentionsSources.addAll(content.textSources.filterIsInstance<MentionTextSource>())
                }
                is PhotoContent -> {
                    textSources.addAll(content.textSources.filterIsInstance<TextLinkTextSource>())
                    mentionsSources.addAll(content.textSources.filterIsInstance<MentionTextSource>())
                }
                is VideoContent -> {
                    textSources.addAll(content.textSources.filterIsInstance<TextLinkTextSource>())
                    mentionsSources.addAll(content.textSources.filterIsInstance<MentionTextSource>())
                }
                else -> {
                    val filteredLinksSources = content.asTextContent()?.textSources?.filterIsInstance<TextLinkTextSource>()
                    if (!filteredLinksSources.isNullOrEmpty())
                        textSources.addAll(filteredLinksSources)
                    val filteredMentionsSources = content.asTextContent()?.textSources?.filterIsInstance<MentionTextSource>()
                    if (!filteredMentionsSources.isNullOrEmpty())
                        mentionsSources.addAll(filteredMentionsSources)
                }
            }

            val textLinks = textSources.map { TextLinkSource(it.source, it.url) }.toMutableList()
            mentionsSources.forEach { textLinks.add(TextLinkSource(it.source, it.source)) }

            return textLinks
        }

    }
}

data class TextLinkSource(val source: String, val url: String)
