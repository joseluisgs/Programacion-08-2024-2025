package dev.joseluisgs.validator

import dev.joseluisgs.exception.EstudiantesException
import dev.joseluisgs.models.Estudiante
import org.lighthousegames.logging.logging
import java.time.LocalDate

/**
 * Validador de estudiantes
 */
class EstudianteValidator : Validator<Estudiante> {
    private val logger = logging()
    override fun validate(t: Estudiante): Estudiante {
        logger.debug { "Validando estudiante: $t" }
        
        if (t.nombre.isBlank()) {
            throw EstudiantesException.ValidationException("El nombre no puede estar vacío")
        }
        if (t.nombre.length > 100) {
            throw EstudiantesException.ValidationException("El nombre no puede tener más de 100 caracteres")
        }
        if (t.fechaNacimiento.isAfter(LocalDate.now())) {
            throw EstudiantesException.ValidationException("La fecha de nacimiento no puede ser posterior a la fecha actual")
        }
        if (t.calificacion < 0.0 || t.calificacion > 10.0) {
            throw EstudiantesException.ValidationException("La calificación debe estar entre 0 y 10")
        }
        return t
    }
}