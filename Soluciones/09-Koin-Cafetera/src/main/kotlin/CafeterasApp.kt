package dev.joseluisgs

import cafeteras.Cafetera
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

// Para inyectar dependencias necesitamos que sea un componente de Koin
class CafeterasApp : KoinComponent {
    val cafetera: Cafetera by inject() // podriamos usar get()

    fun run() {
        println("Ejemplo de Cafeteras Koin")
        println("===========================")
        println("Cafetera: $cafetera")
        cafetera.servir()
        println(cafetera)
        println()

        val cafetera2: Cafetera = get()
        println("Cafetera2: $cafetera2")
        cafetera2.servir()
    }
}
