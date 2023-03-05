package utils

fun compareMin(a: Pair<String, Int>, b: Pair<String, Int>): Pair<String, Int> =
    if (a.second < b.second) a else b

enum class TextReplyCase {
    REPEAT,
    REPLY,
    GREETINGS
}

object TextReply {
    fun createTextReply(case: TextReplyCase, vararg args: String?): String = when (case) {
        TextReplyCase.REPEAT -> repeatTextReply(args.firstOrNull() ?: "@unknown")
        TextReplyCase.REPLY -> replyTextReply()
        TextReplyCase.GREETINGS -> "Готовьте попки, сучки, я начинаю свой дозор"
    }

    fun formatMinutes(minutes: Int): String =
        when {
            minutes >= 60 && minutes % 60 == 0 -> "${minutes / 60} часов"
            minutes >= 60 && minutes % 60 != 0 -> "${(minutes / 60)} часов ${minutes % 60} минут"
            else -> "$minutes минут"
        }

    fun muteOver(username: String, time: String, isPositive: Boolean): String = when (isPositive) {
        true -> "Муту быть! $username отправляется на шконку на $time"
        false -> "На сегодня без мута,$username можешь выдохнуть"
    }


    fun mutePoll(username: String, time: String, quota: Int, votesPositive: Int, votesNegative: Int): String =
        "Замутить $username на $time? [Требуется 2/3 голосов($quota)]\nЗа: $votesPositive\nПротив: $votesNegative"

    fun muteTextReply(): String {
        return "Долбоёб, напиши правильно:\n /mute <username> <минуты>(=1...1440)"
    }

    fun statisticsTextReply(participantsInfo: List<Pair<String, Int>>): String {
        val builder = StringBuilder()
        builder.append("Статистика по проёбам:\n\n")
        participantsInfo.forEachIndexed { index, pair ->
            builder.append(
                "${pair.first} - ${pair.second}\n${
                    if (index != participantsInfo.size - 1) "-".repeat(
                        15
                    ) else ""
                }\n"
            )
        }

        val maxCounterParticipant = participantsInfo.reduce(::compareMin)
        builder.append("\n<strong>Конченый долбоёб детектед:</strong> ${maxCounterParticipant.first}")

        return builder.toString()
    }

    private fun repeatTextReply(username: String): String =
        when ((0..5).random()) {
            1 -> "$username - пидорюга, которая не читала чат!"
            2 -> "$username - пидорасина, которая не читала чат!"
            3 -> "$username - ну просто пидор, без слов!"
            4 -> "$username - ну ёбана в рот, гандон дня!"
            5 -> "$username - да заебал ты блять уже, долбоёб конченный, а ну иди сюда, сука!"
            else -> "$username - пидор, который не читал чат!"
        }

    private fun replyTextReply(): String =
        when ((0..5).random()) {
            1 -> "Ублюдок, мать твою, а ну иди сюда, говно собачье, решил ко мне лезть?"
            2 -> "Ты, засранец вонючий, мать твою, а?"
            3 -> "Ну иди сюда, попробуй меня трахнуть, я тебя сам трахну!"
            4 -> "Ублюдок, онанист чертов, будь ты проклят, иди идиот, трахать тебя и всю семью!"
            5 -> "Говно собачье, жлоб вонючий, дерьмо, сука, падла, иди сюда, мерзавец, негодяй, гад, иди сюда, ты — говно, жопа!"
            else -> "Циклоп прыщавый, сука, падла, говножуй залетный, урод, чмо болотное, а ну иди сюда, ты! Я те жопу порву, член на шею намотаю! Иди сюда, дюдел драный!"
        }
}