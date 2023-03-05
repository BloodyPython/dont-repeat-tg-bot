package application.handlers

import application.dto.TextLinkSource
import application.dto.TgMessage
import infrastructure.abstraction.IMessagesRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TextHandler(override val nextHandler: IQueryHandler<TgMessage, Int?>? = null) : IQueryHandler<TgMessage, Int?>,
    KoinComponent {

    private val messagesRepository by inject<IMessagesRepository>()

    override fun handle(entity: TgMessage): Int? {
        return if (entity.text.isNullOrEmpty() || checkIfTextOnlyContainsLink(
                entity.text,
                entity.textLinkSources
            )
        ) nextHandler?.handle(entity)
        else messagesRepository.findMessageByTextComparison(
            entity.chatId,
            entity.text
        ) ?: nextHandler?.handle(entity)
    }

    private fun checkIfTextOnlyContainsLink(text: String, textLinksSource: List<TextLinkSource>?): Boolean {
        if (textLinksSource == null || textLinksSource.size != 1)
            return false

        val textLinkSize = textLinksSource.first().source.length

        return text.length - textLinkSize <= 20
    }
}