package dev.joseluisgs.models

import java.time.LocalDate
import java.time.LocalDateTime

data class Estudiante(
    val id: Long = NEW_ID,
    val nombre: String,
    val fechaNacimiento: LocalDate,
    val calificacion: Double = 0.0,
    val repetidor: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        const val NEW_ID = -1L
    }
}
