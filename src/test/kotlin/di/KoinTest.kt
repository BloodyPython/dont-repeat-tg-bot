package di

import application.queries.FindMessageDuplicateQuery
import application.services.IMessagesService
import core.domain.entities.DMessage
import core.domain.enums.MessageType
import infrastructure.abstraction.IMessagesRepository
import infrastructure.repositories.GenericRepository
import org.junit.jupiter.api.Test
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.logger.PrintLogger
import kotlin.test.assertEquals

class DiTest : KoinComponent {

/*    @Test
    fun `find duplicate message`() {
        startKoin {
            logger(PrintLogger())
            modules(DbModule, RepositoriesModule, ServicesModule)
        }
        val service = get<IMessagesService>()
        val repo = get<IMessagesRepository>()

        val ent = DMessage {
            chatId = 3
            messageId = 3
            forwardFromChatId = 3
            forwardFromMessageId = 3
            type = MessageType.TEXT
            text = "TEST123"
        }
        val o = repo.addMessage(ent)
        println(o)

        val i = (repo as GenericRepository<DMessage, Int>).requestQuery(
            FindMessageDuplicateQuery(
                ent.chatId,
                ent.forwardFromChatId,
                ent.forwardFromMessageId
            )
        )
        println(i)
        assertEquals(1, i.size)
        val f = (repo as GenericRepository<DMessage, Int>).getById(1)
        println(f)
    }*/
}