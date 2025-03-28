package dev.joseluisgs.ventas.dto

import dev.joseluisgs.clientes.dto.ClienteDto
import kotlinx.serialization.Serializable

@Serializable
data class VentaDto(
    val id: String,
    val cliente: ClienteDto,
    val lineas: List<LineaVentaDto>,
    val total: Double,
    val createdAt: String,
    val updatedAt: String,
    val deleted: Boolean = false
)

