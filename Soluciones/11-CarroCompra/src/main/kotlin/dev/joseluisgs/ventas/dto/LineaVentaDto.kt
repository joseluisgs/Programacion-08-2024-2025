package dev.joseluisgs.ventas.dto

import dev.joseluisgs.productos.dto.ProductoDto
import kotlinx.serialization.Serializable

@Serializable
data class LineaVentaDto(
    val id: String,
    val producto: ProductoDto,
    val cantidad: Int,
    val precio: Double,
    val createdAt: String,
    val updatedAt: String,
)