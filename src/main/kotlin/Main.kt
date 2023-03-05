import di.DbModule
import di.RepositoriesModule
import di.ServicesModule
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.MESSAGE
import org.slf4j.LoggerFactory

suspend fun main() {
    startKoin {
        val adapterLogger = object : org.koin.core.logger.Logger() {
            val slf4jLogger = LoggerFactory.getLogger("Koin")

            override fun log(level: Level, msg: MESSAGE) {
                when (level) {
                    Level.INFO -> slf4jLogger.info(msg)
                    Level.ERROR -> slf4jLogger.error(msg)
                    Level.DEBUG -> slf4jLogger.debug(msg)
                    Level.NONE -> slf4jLogger.warn(msg)
                }
            }
        }
        logger(adapterLogger)
        modules(DbModule, RepositoriesModule, ServicesModule)
    }

    TelegramBotApplication().start()
}