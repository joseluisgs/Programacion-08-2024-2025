package dev.joseluisgs.models

import dev.joseluisgs.locale.toLocalDate
import dev.joseluisgs.locale.toLocalDecimal
import dev.joseluisgs.utils.roundTo
import java.time.LocalDate
import java.time.LocalDateTime

data class Estudiante(
    val id: Int = NEW_ID,
    val nombre: String,
    val fechaNacimiento: LocalDate,
    val calificacion: Double = 0.0,
    val repetidor: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        const val NEW_ID = -1
    }
    
    override fun toString(): String {
        return "Estudiante(id=$id, nombre='$nombre', fechaNacimiento=${fechaNacimiento.toLocalDate()}, calificacion=${
            calificacion.roundTo(
                2
            ).toLocalDecimal()
        }, repetidor=$repetidor, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}
