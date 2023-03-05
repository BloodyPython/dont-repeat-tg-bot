package application.services

import core.domain.entities.DMuteInfo

interface IMutesService {
    fun addMuteInfo(newChatId: Long, newUserId: Long,  newUsername: String, newQuota: Int, newDuration: Int): Int
    fun updateMuteInfo(muteInfo: DMuteInfo): Int
    fun findMuteInfoByChatId(chatId: Long): DMuteInfo?
    fun deleteMuteInfoByChatId(chatId: Long): Boolean
}