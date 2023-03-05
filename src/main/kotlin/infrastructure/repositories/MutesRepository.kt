package infrastructure.repositories

import application.queries.FindMuteInfoByChatIdQuery
import core.domain.entities.DMuteInfo
import infrastructure.abstraction.IMutesRepository
import infrastructure.tables.Mutes
import org.ktorm.database.Database

class MutesRepository(
    database: Database
) : IMutesRepository, GenericRepository<DMuteInfo, Int>(database, Mutes) {
    override fun addMuteInfo(muteInfo: DMuteInfo): Int =
        add(muteInfo)

    override fun updateMuteInfo(muteInfo: DMuteInfo): Int =
        update(muteInfo)

    override fun findMuteInfoByChatId(chatId: Long): DMuteInfo? =
        requestQuery(FindMuteInfoByChatIdQuery(chatId)).singleOrNull()

    override fun deleteMuteInfoByChatId(chatId: Long): Boolean =
        delete(findMuteInfoByChatId(chatId)?.id ?: 0)
}