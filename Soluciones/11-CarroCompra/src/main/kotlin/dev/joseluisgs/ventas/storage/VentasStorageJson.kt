package dev.joseluisgs.ventas.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.joseluisgs.ventas.dto.VentaDto
import dev.joseluisgs.ventas.error.VentaError
import dev.joseluisgs.ventas.mapper.toDto
import dev.joseluisgs.ventas.models.Venta
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging
import java.io.File

@Singleton
@Named("VentasStorageJson")
class VentasStorageJsonImpl : VentasStorage {
    private val logger = logging()
    override fun export(venta: Venta, file: String): Result<Unit, VentaError> {
        
        if (!File(file).parentFile.exists() || !File(file).parentFile.isDirectory) {
            logger.error { "Error al guardar productos en la ruta: $file. No se puede crear el directorio padre" }
            return Err(VentaError.VentaStorageError("Error al guardar venta en la ruta: $file. No se puede crear el directorio padre"))
        }
        val myFile = File(file)
        return try {
            val json = Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            }
            Ok(myFile.writeText(json.encodeToString<VentaDto>(venta.toDto())))
        } catch (e: Exception) {
            logger.error { "Error al salvar ventas a fichero: ${myFile.absolutePath}. ${e.message}" }
            Err(VentaError.VentaStorageError("Error al salvar ventas a fichero: ${myFile.absolutePath}. ${e.message}"))
        }
    }
}