package dev.joseluisgs.ventas.error

import java.util.*

sealed class VentaError(val message: String) {
    class VentaNoEncontrada(id: UUID) : VentaError("Venta con id $id no encontrada")
    class VentaNoValida(message: String) : VentaError("Venta no válida. $message")
    class VentaProductoOtherError(message: String) : VentaError(message)
    class VentaStorageError(message: String) : VentaError("Error en almacenamiento de productos. $message")
}