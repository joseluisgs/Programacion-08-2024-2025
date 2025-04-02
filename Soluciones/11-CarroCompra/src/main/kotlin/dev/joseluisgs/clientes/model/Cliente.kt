package dev.joseluisgs.clientes.model

import java.time.LocalDateTime

data class Cliente(
    val id: Long = NEW_ID,
    val nombre: String,
    val email: String,
    val direccion: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val deleted: Boolean = false
) {
    companion object {
        const val NEW_ID = -1L
    }
}