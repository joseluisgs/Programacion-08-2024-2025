package dev.joseluisgs.ventas.dao

import java.time.LocalDateTime
import java.util.*

data class VentaEntity(
    val id: UUID,
    val clienteId: Long,
    val total: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deleted: Boolean = false
)

