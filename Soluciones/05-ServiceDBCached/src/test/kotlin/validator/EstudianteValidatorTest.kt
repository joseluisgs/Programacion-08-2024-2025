package validator

import dev.joseluisgs.exception.EstudiantesException
import dev.joseluisgs.models.Estudiante
import dev.joseluisgs.validator.EstudianteValidator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
        
        assertEquals(estudiante, result)
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
        
        val res = assertThrows<EstudiantesException.ValidationException> { validator.validate(estudiante) }
        
        assertEquals(
            "Estudiante no válido: El nombre no puede estar vacío",
            res.message,
            "El mensaje de error no es correcto"
        )
        
    }
    
    @Test
    fun validateNombreMore100Lenght() {
        val estudiante = Estudiante(
            id = 1,
            nombre = "a".repeat(101),
            fechaNacimiento = LocalDate.parse("2000-01-01"),
            calificacion = 5.0,
            repetidor = false
        )
        
        val res = assertThrows<EstudiantesException.ValidationException> { validator.validate(estudiante) }
        
        assertEquals(
            "Estudiante no válido: El nombre no puede tener más de 100 caracteres",
            res.message,
            "El mensaje de error no es correcto"
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
        
        val res = assertThrows<EstudiantesException.ValidationException> { validator.validate(estudiante) }
        
        assertEquals(
            "Estudiante no válido: La calificación debe estar entre 0 y 10",
            res.message,
            "El mensaje de error no es correcto"
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
        
        val res = assertThrows<EstudiantesException.ValidationException> { validator.validate(estudiante) }
        
        assertEquals(
            "Estudiante no válido: La calificación debe estar entre 0 y 10",
            res.message,
            "El mensaje de error no es correcto"
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
        
        val res = assertThrows<EstudiantesException.ValidationException> { validator.validate(estudiante) }
        
        assertEquals(
            "Estudiante no válido: La fecha de nacimiento no puede ser posterior a la fecha actual",
            res.message,
            "El mensaje de error no es correcto"
        )
    }
}