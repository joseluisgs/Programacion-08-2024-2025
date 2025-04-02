package dev.joseluisgs.ventas.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.joseluisgs.common.locale.LocaleFormatter.toLocalDateTime
import dev.joseluisgs.common.locale.LocaleFormatter.toLocalMoney
import dev.joseluisgs.ventas.error.VentaError
import dev.joseluisgs.ventas.models.Venta
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging
import java.io.File

@Singleton
@Named("VentasStorageHtml")
class VentasStorageHtmlImpl : VentasStorage {
    private val logger = logging()
    override fun export(venta: Venta, file: String): Result<Unit, VentaError> {
        
        if (!File(file).parentFile.exists() || !File(file).parentFile.isDirectory) {
            logger.error { "Error al guardar productos en la ruta: $file. No se puede crear el directorio padre" }
            return Err(VentaError.VentaStorageError("Error al guardar venta en la ruta: $file. No se puede crear el directorio padre"))
        }
        val myFile = File(file)
        
        // to HTML
        return try {
            val html = """
            <html>
                <head>
                    <title>Venta: ${venta.id}</title>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
                </head>
                <body>
                    <div class="container">
                        <h1>Venta ref: ${venta.id}</h1>
                        <p>Fecha: ${venta.createdAt.toLocalDateTime()}</p>
                        <p>Cliente: ${venta.cliente.nombre}</p>
                        <p>Dirección: ${venta.cliente.direccion}</p>
                        <p>Email: ${venta.cliente.email}</p>
                        <h2>Productos:</h2>
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Nombre</th>
                                    <th>Cantidad</th>
                                    <th>Precio Unitario</th>
                                    <th>Precio Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${
                venta.lineas.joinToString("") {
                    "<tr><td>${it.producto.nombre}</td><td>${it.cantidad}</td><td>${it.producto.precio.toLocalMoney()}</td><td>${(it.cantidad * it.producto.precio).toLocalMoney()}</td></tr>"
                }
            }
                            </tbody>
                        </table>
                        <p class="text-right lead">Total: <span style="font-weight: bold;">${venta.total.toLocalMoney()}</span></p>
                    </div>
                </body>
            </html>
        """.trimIndent()
            Ok(myFile.writeText(html, Charsets.UTF_8))
        } catch (e: Exception) {
            logger.error { "Error al salvar ventas a fichero: ${myFile.absolutePath}. ${e.message}" }
            Err(VentaError.VentaStorageError("Error al salvar ventas a fichero: ${myFile.absolutePath}. ${e.message}"))
        }
    }
    
}