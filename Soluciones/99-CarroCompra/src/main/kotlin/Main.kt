package dev.joseluisgs

import com.github.michaelbull.result.fold
import com.github.michaelbull.result.get
import dev.joseluisgs.productos.service.ProductosService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.fileProperties
import org.koin.ksp.generated.defaultModule
import java.nio.file.Paths

fun main() {
    
    startKoin {
        // declare used logger
        printLogger()
        // Leemos las propiedades de un fichero
        fileProperties("/config.properties") // Por defecto busca en src/main/resources/config.properties, pero puede ser otro fichero si se lo pasas como parametro
        // declara modulos de inyección de dependencias, pero lo verificamos antes de inyectarlos
        modules(defaultModule)
    }
    
    // Creamos una instancia de nuestra aplicación y la ejecutamos
    val app = CarritoApp()
    app.run()
    
}

class CarritoApp : KoinComponent {
    
    fun run() {
        // Instanciamos el servicio de estudiantes de manera inyectada
        val service: ProductosService by inject()
        println("Hola")
        // Importamos del /data/productos.csv
        val pathImport = Paths.get("data", "productos.csv")
        service.importFromFile(pathImport.toString()).fold(
            success = { println("Productos importados: $it nuevo(s)") },
            failure = { println("Error al importar productos: $it") }
        )
        // Mostramos los productos
        service.getAll().get()?.forEach { println(it.toStringLocaleFormatted()) }
        val pathExport = Paths.get("data", "productos-back.json")
        service.exportToFile(pathExport.toString()).fold(
            success = { println("Productos exportados: $it") },
            failure = { println("Error al exportar productos: $it") }
        )
        
    }
}