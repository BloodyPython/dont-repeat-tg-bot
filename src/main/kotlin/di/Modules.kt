package di

import application.services.*
import infrastructure.DbUtilizes
import infrastructure.abstraction.IFilesRepository
import infrastructure.abstraction.IMessagesRepository
import infrastructure.abstraction.IMutesRepository
import infrastructure.abstraction.IParticipantsRepository
import infrastructure.repositories.FilesRepository
import infrastructure.repositories.MessagesRepository
import infrastructure.repositories.MutesRepository
import infrastructure.repositories.ParticipantsRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import org.ktorm.database.Database

val DbModule = module {
    singleOf<Database> { DbUtilizes.initializeDB() } withOptions { createdAtStart() }
}

val RepositoriesModule = module {
    singleOf(::MessagesRepository) { bind<IMessagesRepository>() }
    singleOf(::ParticipantsRepository) { bind<IParticipantsRepository>() }
    singleOf(::FilesRepository) { bind<IFilesRepository>() }
    singleOf(::MutesRepository) { bind<IMutesRepository>() }
}

val ServicesModule = module {
    singleOf(::MessagesService) { bind<IMessagesService>() }
    singleOf(::ParticipantsService) { bind<IParticipantsService>() }
    singleOf(::FilesService) { bind<IFilesService>() }
    singleOf(::MutesService) { bind<IMutesService>() }
}