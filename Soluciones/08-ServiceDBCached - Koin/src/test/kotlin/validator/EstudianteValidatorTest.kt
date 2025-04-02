package validator

import com.github.michaelbull.result.get
import dev.joseluisgs.error.EstudiantesError
import dev.joseluisgs.models.Estudiante
import dev.joseluisgs.validator.EstudianteValidator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.time.LocalDate

class EstudianteValidatorTest {
    val validator = EstudianteValidator()
    
    @Test
    fun validateOk() {
        val estudiante = Estudiante(
            id = 1,
            nombre = "Test",
            fechaNacimiento = LocalDate.parse("2000-01-01"),
            calificacion = 5.0,
            repetidor = false
        )
        
        val result = validator.validate(estudiante)
        
        assertAll(
            { assertEquals(estudiante, result.get(), "El estudiante no es correcto") },
            { assertEquals(true, result.isOk, "El resultado no es correcto") }
        )
    }
    
    @Test
    fun validateEmptyNombre() {
        val estudiante = Estudiante(
            id = 1,
            nombre = "",
            fechaNacimiento = LocalDate.parse("2000-01-01"),
            calificacion = 5.0,
            repetidor = false
        )
        
        val expectedError = EstudiantesError.ValidationError("El nombre no puede estar vacío")
        
        val res = validator.validate(estudiante)
        
        assertAll(
            { assertTrue(res.isErr, "El tipo no es correcto") },
            {
                assertEquals(
                    expectedError.message,
                    res.error.message,
                    "El mensaje de error no es correcto"
                )
            }
        )
        
    }
    
    @Test
    fun validateCalificacionFueraDeRangoSuperior() {
        val estudiante = Estudiante(
            id = 1,
            nombre = "Test",
            fechaNacimiento = LocalDate.parse("2000-01-01"),
            calificacion = 10.01,
            repetidor = false
        )
        
        val expectedError = EstudiantesError.ValidationError("La calificación debe estar entre 0 y 10")
        
        val res = validator.validate(estudiante)
        
        assertAll(
            { assertTrue(res.isErr, "El tipo no es correcto") },
            {
                assertEquals(
                    expectedError.message,
                    res.error.message,
                    "El mensaje de error no es correcto"
                )
            }
        )
        
    }
    
    @Test
    fun validateCalificacionFueraDeRangoInferior() {
        val estudiante = Estudiante(
            id = 1,
            nombre = "Test",
            fechaNacimiento = LocalDate.parse("2000-01-01"),
            calificacion = -0.01,
            repetidor = false
        )
        
        val expectedError = EstudiantesError.ValidationError("La calificación debe estar entre 0 y 10")
        
        val res = validator.validate(estudiante)
        
        assertAll(
            { assertTrue(res.isErr, "El tipo no es correcto") },
            {
                assertEquals(
                    expectedError.message,
                    res.error.message,
                    "El mensaje de error no es correcto"
                )
            }
        )
        
        
    }
    
    @Test
    fun validateFechaDespuesHoy() {
        val estudiante = Estudiante(
            id = 1,
            nombre = "Test",
            fechaNacimiento = LocalDate.now().plusDays(1),
            calificacion = 5.0,
            repetidor = false
        )
        
        val expectedError =
            EstudiantesError.ValidationError("La fecha de nacimiento no puede ser posterior a la fecha actual")
        
        val res = validator.validate(estudiante)
        
        assertAll(
            { assertTrue(res.isErr, "El tipo no es correcto") },
            {
                assertEquals(
                    expectedError.message,
                    res.error.message,
                    "El mensaje de error no es correcto"
                )
            }
        )
    }
}