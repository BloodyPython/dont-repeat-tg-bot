package application.services

import core.domain.entities.DMuteInfo
import infrastructure.abstraction.IMutesRepository

class MutesService(
    private val mutesRepository: IMutesRepository
) : IMutesService {
    override fun addMuteInfo(newChatId: Long, newUserId: Long, newUsername: String, newQuota: Int, newDuration: Int): Int =
        mutesRepository.addMuteInfo(DMuteInfo {
            chatId = newChatId
            userId = newUserId
            username = newUsername
            quota = newQuota
            votePositive = 0
            voteNegative  = 0
            duration = newDuration
        })

    override fun updateMuteInfo(muteInfo: DMuteInfo): Int =
        mutesRepository.updateMuteInfo(muteInfo)

    override fun findMuteInfoByChatId(chatId: Long): DMuteInfo? =
        mutesRepository.findMuteInfoByChatId(chatId)

    override fun deleteMuteInfoByChatId(chatId: Long): Boolean =
        mutesRepository.deleteMuteInfoByChatId(chatId)
}