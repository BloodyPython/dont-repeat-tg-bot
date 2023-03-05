package application.services

import core.domain.entities.DParticipant
import infrastructure.abstraction.IParticipantsRepository

class ParticipantsService(
    private val participantsRepository: IParticipantsRepository
) : IParticipantsService {
    override fun addParticipant(newUserId: Long, newChatId: Long, newUsername: String): Int =
        participantsRepository.addParticipant(DParticipant {
            userId = newUserId
            chatId = newChatId
            username = newUsername
            counter = 0
        })

    override fun findParticipantId(chatId: Long, username: String): Int? =
        participantsRepository.findParticipantId(chatId, username)

    override fun increaseCounter(id: Int) {
        val participant = participantsRepository.findParticipantById(id)
        if (participant != null)
            participantsRepository.updateParticipant(participant.apply { counter++ })
    }

    override fun checkIsChatAdded(chatId: Long): Boolean =
        participantsRepository.checkIsChatAdded(chatId)

    override fun findParticipantsByChatId(chatId: Long): List<DParticipant> =
        participantsRepository.findParticipantsByChatId(chatId)

    override fun findParticipantByUsername(chatId: Long, username: String): DParticipant? =
        participantsRepository.findParticipantById(findParticipantId(chatId, username) ?: 0)
}