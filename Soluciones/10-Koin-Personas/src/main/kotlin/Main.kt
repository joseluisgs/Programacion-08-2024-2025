package dev.joseluisgs

import koin.personas.PersonasApp
import koin.personas.PersonasModule
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.GlobalContext.startKoin
import org.koin.fileProperties
import org.koin.test.check.checkKoinModules
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
fun main() {
    println("Hola Koin")
    
    // Inicializamos Koin
    startKoin {
        // declare used logger
        printLogger()
        // Leemos las propiedades de un fichero
        fileProperties("/config.properties") // Por defecto busca en src/main/resources/koin.properties, pero puede ser otro fichero si se lo pasas como parametro
        // declara modulos de inyección de dependencias, pero lo verificamos antes de inyectarlos
        PersonasModule.verify()
        modules(PersonasModule)
    }


    val app = PersonasApp()
    app.run()
}