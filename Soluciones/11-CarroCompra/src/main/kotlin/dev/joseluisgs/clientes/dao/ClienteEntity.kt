package dev.joseluisgs.clientes.dao

import java.time.LocalDateTime


data class ClienteEntity(
    val id: Long,
    val nombre: String,
    val email: String,
    val direccion: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deleted: Boolean = false
)