package network.marsys.smarthome.shared.library.i18n.memory.di

import network.marsys.smarthome.shared.library.i18n.TranslationRepository
import network.marsys.smarthome.shared.library.i18n.memory.EmptyTranslationRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val internationalizationModule = module {
    single { EmptyTranslationRepository } bind TranslationRepository::class
}
