package dev.joseluisgs.productos.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.joseluisgs.productos.dto.ProductoDto
import dev.joseluisgs.productos.error.ProductoError
import dev.joseluisgs.productos.mapper.toDto
import dev.joseluisgs.productos.mapper.toModel
import dev.joseluisgs.productos.model.Producto
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging
import java.io.File

private val logger = logging()

@Singleton
class ProductosStorageImpl : ProductosStorage {
    override fun loadFromFile(file: String): Result<List<Producto>, ProductoError> {
        logger.debug { "Cargando productos desde la ruta: $file" }
        if (!File(file).exists() || !File(file).isFile || !File(file).canRead()) {
            logger.error { "Error al cargar productos desde la ruta: $file. El fichero no existe, no es un fichero o no se puede leer" }
            return Err(ProductoError.ProductoStorageError("Error al cargar productos desde la ruta: $file. El fichero no existe"))
        }
        return try {
            Ok(File(file).readLines().drop(1).map { line ->
                val parts = line.split(",")
                ProductoDto(
                    nombre = parts[1],
                    precio = parts[2].toDouble(),
                    stock = parts[3].toInt(),
                    categoria = parts[4],
                ).toModel()
            })
        } catch (e: Exception) {
            logger.error { "Error al cargar productos desde la ruta: $file. ${e.message}" }
            Err(ProductoError.ProductoStorageError("Error al cargar productos desde la ruta: $file. ${e.message}"))
        }
    }
    
    override fun saveToFile(data: List<Producto>, file: String): Result<List<Producto>, ProductoError> {
        logger.debug { "Guardando productos en la ruta: $file" }
        if (!File(file).parentFile.exists() || !File(file).parentFile.isDirectory) {
            logger.error { "Error al guardar productos en la ruta: $file. No se puede crear el directorio padre" }
            return Err(ProductoError.ProductoStorageError("Error al guardar productos en la ruta: $file. No se puede crear el directorio padre"))
        }
        val json = Json { ignoreUnknownKeys = false; prettyPrint = true }
        return try {
            File(file).writeText(json.encodeToString(data.map { it.toDto() }))
            Ok(data)
        } catch (e: Exception) {
            logger.error { "Error al guardar productos en la ruta: $file. ${e.message}" }
            Err(ProductoError.ProductoStorageError("Error al guardar productos en la ruta: $file. ${e.message}"))
        }
    }
}