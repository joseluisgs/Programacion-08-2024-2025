package dev.joseluisgs.models

import java.time.LocalDate

data class Alumno(
    val id: Long = -1,
    val nombre: String,
    val calificacion: Double,
    val aprobado: Boolean,
    val fechaNacimiento: LocalDate
)