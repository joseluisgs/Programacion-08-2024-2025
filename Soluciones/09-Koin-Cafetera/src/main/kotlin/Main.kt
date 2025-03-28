package dev.joseluisgs

import dev.joseluisgs.di.CafeterasModule
import org.koin.core.context.GlobalContext.startKoin

fun main() {
    println("Hola Koin")
    startKoin {
        // declare used logger
        // printLogger()
        // declara modulos de inyección de dependencias
        modules(CafeterasModule)
    }

    val app = CafeterasApp()
    app.run()
}