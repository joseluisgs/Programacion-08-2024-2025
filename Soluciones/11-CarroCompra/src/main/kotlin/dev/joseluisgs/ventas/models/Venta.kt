package dev.joseluisgs.ventas.models

import dev.joseluisgs.clientes.model.Cliente
import dev.joseluisgs.common.locale.LocaleFormatter.toLocalMoney
import java.time.LocalDateTime
import java.util.*

data class Venta(
    val id: UUID = UUID.randomUUID(),
    val cliente: Cliente,
    val lineas: List<LineaVenta>,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val deleted: Boolean = false,
) {
    val total: Double
        get() = lineas.sumOf { it.precio * it.cantidad }
    
    fun toStringLocaleFormatted(): String {
        return "Venta(id=$id, cliente='${cliente.nombre}', total=${total.toLocalMoney()}, createdAt=$createdAt, updatedAt=$updatedAt, deleted=$deleted)"
    }
}

