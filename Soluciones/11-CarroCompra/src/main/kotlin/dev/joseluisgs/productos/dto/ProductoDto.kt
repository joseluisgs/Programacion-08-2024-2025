package dev.joseluisgs.productos.dto

import kotlinx.serialization.Serializable
import java.time.LocalDateTime


@Serializable
data class ProductoDto(
    val id: Long = -1,
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val categoria: String,
    val createdAt: String = LocalDateTime.now().toString(),
    val updatedAt: String = LocalDateTime.now().toString(),
    val deleted: Boolean = false
)