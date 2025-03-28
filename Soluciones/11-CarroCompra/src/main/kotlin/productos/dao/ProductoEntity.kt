package dev.joseluisgs.productos.dao

import java.time.LocalDateTime


data class ProductoEntity(
    val id: Long,
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val categoria: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deleted: Boolean,
)