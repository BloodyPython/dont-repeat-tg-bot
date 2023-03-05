package application.services

import core.domain.entities.DParticipant

interface IParticipantsService {
    fun addParticipant(newUserId: Long, newChatId: Long, newUsername: String): Int
    fun findParticipantId(chatId: Long, username: String): Int?
    fun increaseCounter(id: Int)
    fun checkIsChatAdded(chatId: Long): Boolean
    fun findParticipantsByChatId(chatId: Long): List<DParticipant>
    fun findParticipantByUsername(chatId: Long, username: String): DParticipant?
}