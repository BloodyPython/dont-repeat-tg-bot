import application.dto.TgMessage
import application.services.IFilesService
import application.services.IMessagesService
import application.services.IMutesService
import application.services.IParticipantsService
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.api.answers.answerCallbackQuery
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.bot.setMyCommands
import dev.inmo.tgbotapi.extensions.api.chat.get.getChatAdministrators
import dev.inmo.tgbotapi.extensions.api.chat.members.restrictChatMember
import dev.inmo.tgbotapi.extensions.api.deleteMessage
import dev.inmo.tgbotapi.extensions.api.edit.text.editMessageText
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommandWithArgs
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onContentMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onDataCallbackQuery
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.entities
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.forward_from_chat
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.from
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.message
import dev.inmo.tgbotapi.extensions.utils.types.buttons.InlineKeyboardBuilder
import dev.inmo.tgbotapi.types.BotCommand
import dev.inmo.tgbotapi.types.TelegramDate
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardButtons.CallbackDataInlineKeyboardButton
import dev.inmo.tgbotapi.types.chat.ChatPermissions
import dev.inmo.tgbotapi.types.message.HTMLParseMode
import dev.inmo.tgbotapi.types.message.textsources.MentionTextSource
import infrastructure.DbUtilizes
import io.ktor.util.reflect.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import utils.TextReply
import utils.TextReplyCase
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class TelegramBotApplication : KoinComponent {
    private val messagesService by inject<IMessagesService>()
    private val participantsService by inject<IParticipantsService>()
    private val filesService by inject<IFilesService>()
    private val mutesService by inject<IMutesService>()

    suspend fun start() {
        val bot = telegramBot(Constants.BOT_TOKEN)
        val scope = CoroutineScope(Dispatchers.Default)

        val timer = Timer("Clearing DB", false)
            .schedule(TimeUnit.DAYS.toMillis(1), TimeUnit.DAYS.toMillis(1)) { DbUtilizes.clearDB() }
        timer.run()

        bot.buildBehaviourWithLongPolling(scope) {
            println("*** Bot Started ***")

            val botUsername = getMe().let {
                println(it)
                it.username
            }
            val muteMarkup = InlineKeyboardBuilder().apply {
                add(
                    listOf(
                        CallbackDataInlineKeyboardButton("Го", "MUTE:YES"),
                        CallbackDataInlineKeyboardButton("Не", "MUTE:NO")
                    )
                )
            }

            setMyCommands(
                listOf(
                    BotCommand("start", "to start bot"),
                    BotCommand("stats", "to see stats"),
                    BotCommand("mute", "to mute a user"),
                    BotCommand("all", "to tag all users")
                )
            )

            // *** Reply to Bot ***
            onContentMessage(initialFilter = { message ->
                val mentionsList =
                    message.entities?.filter { it.instanceOf(MentionTextSource::class) } as? List<MentionTextSource>
                val isMentions = mentionsList?.any { it.username == botUsername } ?: false

                val isReply = message.replyTo?.from?.username == botUsername

                isMentions or isReply
            }, scenarioReceiver = {
                sendMessage(
                    it.chat,
                    TextReply.createTextReply(TextReplyCase.REPLY),
                    replyToMessageId = it.messageId
                )
            })

            // *** Start Bot ***
            onCommand("start", initialFilter = {
                !participantsService.checkIsChatAdded(it.chat.id.chatId)
            }, scenarioReceiver = { message ->
                val admins = getChatAdministrators(message.chat.id)
                admins.filter { it.user.username?.username != botUsername.username }.forEachIndexed { index, member ->
                    participantsService.addParticipant(
                        message.from!!.id.chatId,
                        message.chat.id.chatId,
                        member.user.username?.username ?: "unknown_${index + 1}"
                    )
                }
                sendMessage(
                    message.chat,
                    TextReply.createTextReply(TextReplyCase.GREETINGS)
                )
            })

            // *** Participants Statistics ***
            onCommand("stats", initialFilter = {
                participantsService.checkIsChatAdded(it.chat.id.chatId)
            }, scenarioReceiver = { message ->
                val participantsInfo =
                    participantsService.findParticipantsByChatId(message.chat.id.chatId)
                        .map { it.username to it.counter }
                sendMessage(
                    message.chat,
                    TextReply.statisticsTextReply(participantsInfo),
                    parseMode = HTMLParseMode
                )
            })

            // *** Tag All ***
            onCommand("all", initialFilter = {
                participantsService.checkIsChatAdded(it.chat.id.chatId)
            }, scenarioReceiver = { message ->
                val participants =
                    participantsService.findParticipantsByChatId(message.chat.id.chatId).map { it.username }

                sendMessage(
                    message.chat,
                    "Петушки, просыпаемся и на выход"
                )
                participants.chunked(3).map { it.joinToString(" ") }.forEach {
                    sendMessage(
                        message.chat,
                        it
                    )
                }
            })

            // *** Mute User ***
            onCommandWithArgs("mute") { message, args ->
                if (args.count() != 2) {
                    sendMessage(
                        message.chat,
                        TextReply.muteTextReply()
                    )
                    return@onCommandWithArgs
                }
                if (args.getOrNull(1)?.toLongOrNull() == null
                    || (args.getOrNull(1)?.toLongOrNull() ?: 0) !in 1..1440
                ) {
                    sendMessage(
                        message.chat,
                        TextReply.muteTextReply()
                    )
                    return@onCommandWithArgs
                }
                if (participantsService.findParticipantId(
                        message.chat.id.chatId,
                        args.getOrNull(0) ?: "@unknown"
                    ) == null
                ) {
                    sendMessage(
                        message.chat,
                        TextReply.muteTextReply()
                    )
                    return@onCommandWithArgs
                }

                val participantsCount = participantsService.findParticipantsByChatId(message.chat.id.chatId).count()
                val quota = if (participantsCount == 1) 1 else (participantsCount * 2) / 3
                mutesService.addMuteInfo(
                    message.chat.id.chatId,
                    participantsService.findParticipantByUsername(message.chat.id.chatId, args[0])!!.userId,
                    args[0],
                    quota,
                    args[1].toInt()
                )
                sendMessage(
                    message.chat,
                    TextReply.mutePoll(args[0], TextReply.formatMinutes(args[1].toInt()), quota, 0, 0),
                    replyMarkup = muteMarkup.build()
                )
            }

            onDataCallbackQuery(initialFilter = {
                it.data.split(":")[0] == "MUTE"
            }, scenarioReceiver = {
                val answer = it.data.split(":")[1]
                val muteInfo = mutesService.findMuteInfoByChatId(it.message?.chat?.id?.chatId ?: 0)

                if (muteInfo != null) {
                    when (answer) {
                        "YES" -> {
                            if (muteInfo.quota == muteInfo.votePositive + 1) {
                                restrictChatMember(
                                    it.message?.chat?.id!!,
                                    UserId(muteInfo.userId),
                                    TelegramDate(
                                        Instant.now().plusSeconds(muteInfo.duration.toLong() * 60).epochSecond
                                    ),
                                    ChatPermissions(
                                        canSendMessages = false,
                                        canSendMediaMessages = true,
                                        canSendPolls = true,
                                        canSendOtherMessages = true,
                                        canAddWebPagePreviews = true,
                                        canChangeInfo = true,
                                        canInviteUsers = false,
                                        canPinMessages = true
                                    )
                                )
                                mutesService.deleteMuteInfoByChatId(it.message?.chat?.id?.chatId ?: 0)
                                sendMessage(
                                    it.message?.chat!!,
                                    TextReply.muteOver(
                                        muteInfo.username,
                                        TextReply.formatMinutes(muteInfo.duration),
                                        true
                                    )
                                )
                                editMessageText(
                                    it.message!!.chat,
                                    it.message!!.messageId,
                                    TextReply.mutePoll(
                                        muteInfo.username,
                                        TextReply.formatMinutes(muteInfo.duration),
                                        muteInfo.quota,
                                        muteInfo.votePositive,
                                        muteInfo.voteNegative
                                    )
                                )
                            } else {
                                mutesService.updateMuteInfo(muteInfo.apply {
                                    votePositive++
                                })
                                editMessageText(
                                    it.message!!.chat,
                                    it.message!!.messageId,
                                    TextReply.mutePoll(
                                        muteInfo.username,
                                        TextReply.formatMinutes(muteInfo.duration),
                                        muteInfo.quota,
                                        muteInfo.votePositive++,
                                        muteInfo.voteNegative
                                    ),
                                    replyMarkup = muteMarkup.build()
                                )
                            }
                        }
                        else -> {
                            mutesService.updateMuteInfo(muteInfo.apply {
                                voteNegative++
                            })
                            editMessageText(
                                it.message!!.chat,
                                it.message!!.messageId,
                                TextReply.mutePoll(
                                    muteInfo.username,
                                    TextReply.formatMinutes(muteInfo.duration),
                                    muteInfo.quota,
                                    muteInfo.votePositive,
                                    muteInfo.voteNegative++
                                ),
                                replyMarkup = muteMarkup.build()
                            )
                        }
                    }
                    if (muteInfo.votePositive + muteInfo.voteNegative == muteInfo.quota) {
                        sendMessage(
                            it.message?.chat!!,
                            TextReply.muteOver(muteInfo.username, TextReply.formatMinutes(muteInfo.duration), false)
                        )
                        editMessageText(
                            it.message!!.chat,
                            it.message!!.messageId,
                            TextReply.mutePoll(
                                muteInfo.username,
                                TextReply.formatMinutes(muteInfo.duration),
                                muteInfo.quota,
                                muteInfo.votePositive,
                                muteInfo.voteNegative
                            )
                        )
                        mutesService.deleteMuteInfoByChatId(it.message?.chat?.id?.chatId ?: 0)
                    }
                }

                answerCallbackQuery(it, "Ты проголосовал ${if (answer == "YES") "за мут" else "против мута"}")
            })

            // *** Repeat Messages ***
            onContentMessage(initialFilter = {
                participantsService.checkIsChatAdded(it.chat.id.chatId) && it.forward_from_chat != null
            }, scenarioReceiver = {
                println("Received ${it.content} from ${it.chat.id.chatId}.")
                val tgMessage = TgMessage.fromCommonMessage(it)
                val checkedMessage = messagesService.findMessageDuplicate(tgMessage)
                if (checkedMessage == null) {
                    messagesService.apply {
                        val messageId = addMessage(tgMessage)
                        val newMessage = findMessageById(messageId)!!
                        tgMessage.fileUniqueId.forEach { id -> filesService.addFile(tgMessage.chatId, id, newMessage) }
                    }
                } else {
                    deleteMessage(it)
                    participantsService.apply {
                        val participantId = findParticipantId(it.chat.id.chatId, it.from?.username?.username ?: "")
                        increaseCounter(participantId ?: 0)
                    }
                    sendMessage(
                        it.chat,
                        TextReply.createTextReply(TextReplyCase.REPEAT, it.from?.username?.username),
                        replyToMessageId = messagesService.findMessageIdById(checkedMessage)
                    )
                }
            })

        }.join()
    }

}