package dev.joseluisgs.dao

import java.time.LocalDate
import java.time.LocalDateTime


data class EstudianteEntity(
    val id: Int,
    val nombre: String,
    val fechaNacimiento: LocalDate,
    val calificacion: Double,
    val repetidor: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)