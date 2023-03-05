package infrastructure.repositories

import application.queries.FindParticipantIdQuery
import application.queries.FindParticipantsByChatIdQuery
import application.queries.IsChatAddedQuery
import core.domain.entities.DParticipant
import infrastructure.abstraction.IParticipantsRepository
import infrastructure.tables.Participants
import org.ktorm.database.Database

class ParticipantsRepository(
    database: Database
) : IParticipantsRepository, GenericRepository<DParticipant, Int>(database, Participants) {
    override fun addParticipant(participant: DParticipant): Int =
        add(participant)

    override fun findParticipantId(chatId: Long, username: String): Int? =
        requestQuery(FindParticipantIdQuery(chatId, username)).singleOrNull()?.id

    override fun findParticipantById(id: Int): DParticipant? =
        getById(id)

    override fun updateParticipant(participant: DParticipant): Int =
        update(participant)

    override fun checkIsChatAdded(chatId: Long): Boolean =
        requestQuery(IsChatAddedQuery(chatId)).isNotEmpty()

    override fun findParticipantsByChatId(chatId: Long): List<DParticipant> =
        requestQuery(FindParticipantsByChatIdQuery(chatId))
}