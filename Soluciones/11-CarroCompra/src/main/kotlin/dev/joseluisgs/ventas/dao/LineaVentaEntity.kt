package dev.joseluisgs.ventas.dao

import java.time.LocalDateTime
import java.util.*

data class LineaVentaEntity(
    val id: UUID,
    val ventaId: UUID,
    val productoId: Long,
    val cantidad: Int,
    val precio: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)