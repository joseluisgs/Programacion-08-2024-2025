package dev.joseluisgs

import com.github.michaelbull.result.get
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.joseluisgs.clientes.service.ClientesService
import dev.joseluisgs.productos.model.Categoria
import dev.joseluisgs.productos.model.Producto
import dev.joseluisgs.productos.service.ProductosService
import dev.joseluisgs.utils.openInBrowser
import dev.joseluisgs.ventas.models.LineaVenta
import dev.joseluisgs.ventas.models.Venta
import dev.joseluisgs.ventas.service.VentasService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.fileProperties
import org.koin.ksp.generated.defaultModule
import java.io.File
import java.nio.file.Files
import java.util.*

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
        println("Hola Productos!")
        println()
        println("Buscando productos...")
        
        val productosService: ProductosService by inject()
        
        
        productosService.getAll()
            .onSuccess { productos ->
                productos.forEach { println(it.toStringLocaleFormatted()) }
            }
        
        println()
        println("Buscando producto por id...")
        productosService.getById(1)
            .onSuccess { println("Encontrado: ${it.toStringLocaleFormatted()}") }
            .onFailure { println("ERROR: ${it.message}") }
        
        
        println("Buscando producto por id...")
        productosService.getById(99)
            .onSuccess { println("Encontrado: ${it.toStringLocaleFormatted()}") }
            .onFailure { println("ERROR: ${it.message}") }
        
        println()
        println("Creando producto...")
        val producto = Producto(
            nombre = "Producto 1",
            precio = 10.0,
            stock = 10,
            categoria = Categoria.DEPORTE
        )
        productosService.create(producto)
            .onSuccess { println("Creado: ${it.toStringLocaleFormatted()}") }
            .onFailure { println("ERROR: ${it.message}") }
        
        println()
        println("Creando producto...")
        val producto2 = Producto(
            nombre = "Producto 1",
            precio = -10.0,
            stock = -10,
            categoria = Categoria.DEPORTE
        )
        productosService.create(producto2)
            .onSuccess { println("Creado: ${it.toStringLocaleFormatted()}") }
            .onFailure { println("ERROR: ${it.message}") }
        
        println()
        println("Actualizando productos...")
        productosService.update(1, producto.copy(nombre = "Producto 1 Modificado"))
            .onSuccess { println("Actualizado: ${it.toStringLocaleFormatted()}") }
            .onFailure { println("ERROR: ${it.message}") }
        
        
        
        println()
        println("Buscando producto...")
        productosService.getById(1)
            .onSuccess { println("Encontrado: ${it.toStringLocaleFormatted()}") }
            .onFailure { println("ERROR: ${it.message}") }
        
        println()
        println("Buscando productos...")
        productosService.getAll()
            .onSuccess { productos ->
                productos.forEach { println(it.toStringLocaleFormatted()) }
            }
        
        println()
        println("Borrando producto por id...")
        productosService.delete(1)
            .onSuccess { println("Borrado: ${it.toStringLocaleFormatted()}") }
            .onFailure { println("ERROR: ${it.message}") }
        
        println()
        println("Borrando producto por id...")
        productosService.delete(99)
            .onSuccess { println("Borrado: ${it.toStringLocaleFormatted()}") }
            .onFailure { println("ERROR: ${it.message}") }
        
        
        println()
        println("Buscando producto...")
        productosService.getById(1)
            .onSuccess { println("Encontrado: ${it.toStringLocaleFormatted()}") }
            .onFailure { println("ERROR: ${it.message}") }
        
        println()
        println("Buscando productos...")
        println(productosService.getAll().get())
        
        println()
        println("Buscando productos por categoria...")
        println(productosService.getByCategoria(Categoria.DEPORTE.name))
        
        
        val clientesService: ClientesService by inject()
        println("Hola Clientes!")
        clientesService.getAll().get()?.forEach { println(it) }
        
        println()
        println("Buscando cliente por id...")
        clientesService.getById(1)
            .onSuccess { println("Encontrado: $it") }
            .onFailure { println("ERROR: ${it.message}") }
        
        
        println("Buscando cliente por id...")
        clientesService.getById(99)
            .onSuccess { println("Encontrado: $it") }
            .onFailure { println("ERROR: ${it.message}") }
        
        println()
        println("Hola Ventas!")
        val ventasService: VentasService by inject()
        
        println()
        println("Buscando Ventas...")
        ventasService.getById(UUID.fromString("1862b89e-5e86-45b7-801a-c13bc3a0b362"))
            .onSuccess { println("Encontrado: $it") }
            .onFailure { println("ERROR: ${it.message}") }
        
        
        println()
        println("Buscando Ventas...")
        ventasService.getById(UUID.fromString("ffa315af-7333-452f-9e17-72fc9188cabd"))
            .onSuccess { println("Encontrado: $it") }
            .onFailure { println("ERROR: ${it.message}") }
        
        println()
        println("Creando Venta...")
        val cliente = clientesService.getById(1).value
        val producto3 = productosService.getById(3).value
        val producto4 = productosService.getById(5).value
        
        val venta = Venta(
            cliente = cliente,
            lineas = listOf(
                LineaVenta(
                    producto = producto3,
                    cantidad = 2,
                    precio = producto3.precio
                ),
                LineaVenta(
                    producto = producto4,
                    cantidad = 1,
                    precio = producto4.precio
                )
            )
        )
        println(venta.lineas)
        println(venta)
        
        val res = ventasService.create(venta)
        res
            .onSuccess { println("Creada venta: $it") }
            .onFailure { println("ERROR: ${it.message}") }
        
        //println(producto3.precio * 2 + producto4.precio)
        //println(venta.total)
        
        
        println()
        res.onSuccess { println("Creado: $it") }
            .onFailure { println("ERROR: ${it.message}") }
        
        
        println()
        println("Buscando Ventas...")
        ventasService.getById(res.value.id)
            .onSuccess { println("Encontrado: $it") }
            .onFailure { println("ERROR: ${it.message}") }
        
        
        println()
        println("Buscando Ventas...")
        ventasService.getById(UUID.fromString("ffa315af-7333-452f-9e17-72fc9188cabd"))
            .onSuccess { println("Encontrado: $it") }
            .onFailure { println("ERROR: ${it.message}") }
        
        val venta2 = Venta(
            cliente = cliente,
            lineas = listOf(
                LineaVenta(
                    producto = producto3,
                    cantidad = 999999999,
                    precio = producto3.precio
                ),
                LineaVenta(
                    producto = producto4,
                    cantidad = 1,
                    precio = producto4.precio
                )
            )
        )
        
        println()
        println("Creando Venta...")
        ventasService.create(venta2)
            .onSuccess { println("Creado: $it") }
            .onFailure { println("ERROR: ${it.message}") }
        
        
        println()
        println("Hola Storage!")
        val csvFile = File("data", "productos.csv")
        productosService.importFromFile(csvFile.absolutePath)
            .onSuccess {
                println("Importado correctamente desde $csvFile con  $it productos")
            }
            .onFailure { println("ERROR: ${it.message}") }
        
        println()
        var jsonFile = File("data", "productos-back.json")
        productosService.exportToFile(jsonFile.absolutePath)
            .onSuccess {
                println("Importado correctamente desde $jsonFile con  $it productos")
            }
            .onFailure { println("ERROR: ${it.message}") }
        
        println()
        jsonFile = File("data", "${res.value.id}.json")
        ventasService.exportToJson(venta, jsonFile.absolutePath)
            .onSuccess { println("Exportado correctamente a $jsonFile") }
            .onFailure { println("ERROR: ${it.message}") }
        
        // Creamos el directorio de salida si no existe
        val outDir =
            getKoin().getProperty("storage.out") ?: "out" // Por defecto es out pero leemos gracias a Koin la propiedad
        val outDirFile = File(outDir)
        if (!outDirFile.exists()) {
            outDirFile.mkdirs()
        }
        // por si acaso borro los ficheros anteriores
        outDirFile.listFiles()?.forEach { Files.delete(it.toPath()) }
        
        val htmlFile = File(outDir, "${res.value.id}.html")
        ventasService.exportToHtml(venta, htmlFile.absolutePath)
            .onSuccess {
                println("Exportado correctamente a $htmlFile")
                openInBrowser(htmlFile)
            }
            .onFailure { println("ERROR: ${it.message}") }
        
    }
}