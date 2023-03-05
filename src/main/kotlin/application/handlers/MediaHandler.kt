package application.handlers

import application.dto.TgMessage
import core.domain.enums.MessageType
import infrastructure.abstraction.IFilesRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MediaHandler(override val nextHandler: IQueryHandler<TgMessage, Int?>? = null) : IQueryHandler<TgMessage, Int?>,
    KoinComponent {

    private val filesRepository by inject<IFilesRepository>()


    override fun handle(entity: TgMessage): Int? {
        if (entity.type == MessageType.TEXT || entity.type == MessageType.OTHER) return nextHandler?.handle(entity)

        for (uniqueId in entity.fileUniqueId) {
            val file = filesRepository.findFileByUniqueId(entity.chatId, uniqueId)
            if (file != null) return file.id
        }

        return null
    }
}