package infrastructure

import Constants
import infrastructure.tables.Files
import infrastructure.tables.Messages
import infrastructure.tables.Mutes
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.database.Database
import org.ktorm.dsl.less
import org.ktorm.entity.removeIf
import org.ktorm.entity.sequenceOf
import org.ktorm.logging.Slf4jLoggerAdapter
import org.ktorm.support.sqlserver.SqlServerDialect
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.OffsetDateTime

object DbUtilizes : KoinComponent {

    fun initializeDB(): Database {
        val db = Database.connect(
            url = Constants.DB_URL,
            driver = Constants.DB_DRIVER,
            //user = Constants.DB_USER,
            //password = Constants.DB_PASSWORD,
            logger = Slf4jLoggerAdapter(LoggerFactory.getLogger("Database")),
            dialect = SqlServerDialect()
        )
        db.useConnection { conn ->
            val sqlCommands = listOf(
            """
            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='t_messages')
            BEGIN
            CREATE TABLE t_messages(
            id INT PRIMARY KEY IDENTITY,
            chat_id BIGINT NOT NULL,
            message_id BIGINT NOT NULL,
            forward_from_chat_id BIGINT NOT NULL,
            forward_from_message_id BIGINT NOT NULL,
            type VARCHAR(255) NOT NULL,
            text NVARCHAR(MAX),
            version INT NOT NULL,
            created DATETIME2 NOT NULL,
            modified DATETIME2 NOT NULL
            )
            END
            """.trimIndent(),
            """
            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='t_files')
            BEGIN
            CREATE TABLE t_files(
            id INT PRIMARY KEY IDENTITY,
            chat_id BIGINT NOT NULL,
            file_unique_id VARCHAR(255) NOT NULL,
            message_id INT NOT NULL,
            version INT NOT NULL,
            created DATETIME2 NOT NULL,
            modified DATETIME2 NOT NULL,
                        
            FOREIGN KEY(message_id) REFERENCES t_messages(id)
            )
            END
            """.trimIndent(),
            """
            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='t_participants')
            BEGIN
            CREATE TABLE t_participants(
            id INT PRIMARY KEY IDENTITY,
            user_id BIGINT NOT NULL,
            chat_id BIGINT NOT NULL,
            username VARCHAR(255) NOT NULL,
            counter INT NOT NULL,
            version INT NOT NULL,
            created DATETIME2 NOT NULL,
            modified DATETIME2 NOT NULL
            )
            END
            """.trimIndent(),
            """
            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='t_mutes')
            BEGIN
            CREATE TABLE t_mutes(
            id INT PRIMARY KEY IDENTITY,
            chat_id BIGINT NOT NULL,
            user_id BIGINT NOT NULL,
            username VARCHAR(255) NOT NULL,
            quota INT NOT NULL,
            vote_positive INT NOT NULL,
            vote_negative INT NOT NULL,
            duration INT NOT NULL,
            version INT NOT NULL,
            created DATETIME2 NOT NULL,
            modified DATETIME2 NOT NULL
            )
            END
            """.trimIndent()
            )

            sqlCommands.forEach { conn.prepareStatement(it).execute() }
        }

        return db
    }

    fun clearDB() {
        val db by inject<Database>()
        val messagesSequence = db.sequenceOf(Messages)
        val filesSequence = db.sequenceOf(Files)
        val mutesSequence = db.sequenceOf(Mutes)
        messagesSequence.removeIf {
            Messages.whenCreated less LocalDateTime.now().minusWeeks(1).toInstant(OffsetDateTime.now().offset)
        }
        filesSequence.removeIf {
            Files.whenCreated less LocalDateTime.now().minusWeeks(1).toInstant(OffsetDateTime.now().offset)
        }
        mutesSequence.removeIf {
            Mutes.whenCreated less LocalDateTime.now().minusDays(1).toInstant(OffsetDateTime.now().offset)
        }
    }

}