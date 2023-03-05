package infrastructure.abstraction

import core.domain.entities.DMuteInfo

interface IMutesRepository {
    fun addMuteInfo(muteInfo: DMuteInfo): Int
    fun updateMuteInfo(muteInfo: DMuteInfo): Int
    fun findMuteInfoByChatId(chatId: Long): DMuteInfo?
    fun deleteMuteInfoByChatId(chatId: Long): Boolean
}