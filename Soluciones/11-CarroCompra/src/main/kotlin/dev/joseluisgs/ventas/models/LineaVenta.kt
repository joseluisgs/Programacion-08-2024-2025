package dev.joseluisgs.ventas.models

import dev.joseluisgs.common.locale.LocaleFormatter.toLocalMoney
import dev.joseluisgs.productos.model.Producto
import java.time.LocalDateTime
import java.util.*

data class LineaVenta(
    val id: UUID = UUID.randomUUID(),
    val producto: Producto,
    val cantidad: Int,
    val precio: Double,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    val total: Double
        get() = precio * cantidad
    
    fun toStringLocaleFormatted(): String {
        return "Producto(id=$id, producto='${producto.nombre}', cantidad=$cantidad, precio=${precio.toLocalMoney()}, total=${total.toLocalMoney()}, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}