package dev.joseluisgs.clientes.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClienteDto(
    val id: Long,
    val nombre: String,
    val direccion: String,
    val email: String,
    val createdAt: String,
    val updatedAt: String,
    val deleted: Boolean = false
)