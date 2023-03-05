package infrastructure.abstraction

import core.domain.entities.DParticipant

interface IParticipantsRepository {
    fun addParticipant(participant: DParticipant): Int
    fun findParticipantId(chatId: Long, username: String): Int?
    fun findParticipantById(id: Int): DParticipant?
    fun updateParticipant(participant: DParticipant): Int
    fun checkIsChatAdded(chatId: Long): Boolean
    fun findParticipantsByChatId(chatId: Long): List<DParticipant>
}