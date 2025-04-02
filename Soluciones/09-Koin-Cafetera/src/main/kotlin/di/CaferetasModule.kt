package dev.joseluisgs.di

import cafeteras.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val CafeterasModule = module {
    // Lo voy a definir todo como Singleton
    single<Calentador> { CalentadorElectrico() }
    single<Bomba> { Termosifon(get()) }
    factory { Cafetera(get(), get()) }

}

val CafeterasModule2 = module {
    // Lo voy a definir todo como Singleton
    singleOf(::CalentadorElectrico) { bind<Calentador>() }
    singleOf(::Termosifon) { bind<Bomba>() }
    singleOf(::Cafetera)
}
