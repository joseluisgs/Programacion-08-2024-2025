package dev.joseluisgs.validator

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.joseluisgs.error.EstudiantesError
import dev.joseluisgs.models.Estudiante
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging
import java.time.LocalDate

/**
 * Validador de estudiantes
 */

@Singleton
class EstudianteValidator : Validator<Estudiante, EstudiantesError> {
    private val logger = logging()
    override fun validate(t: Estudiante): Result<Estudiante, EstudiantesError> {
        logger.debug { "Validando estudiante: $t" }
        
        if (t.nombre.isBlank()) {
            return Err(EstudiantesError.ValidationError("El nombre no puede estar vacío"))
        }
        if (t.fechaNacimiento.isAfter(LocalDate.now())) {
            return Err(EstudiantesError.ValidationError("La fecha de nacimiento no puede ser posterior a la fecha actual"))
        }
        if (t.calificacion < 0.0 || t.calificacion > 10.0) {
            return Err(EstudiantesError.ValidationError("La calificación debe estar entre 0 y 10"))
            
        }
        return Ok(t)
    }
}