package service

import com.github.benmanes.caffeine.cache.Cache
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import dev.joseluisgs.error.EstudiantesError
import dev.joseluisgs.models.Estudiante
import dev.joseluisgs.repositories.EstudiantesRepository
import dev.joseluisgs.service.EstudiantesServiceImpl
import dev.joseluisgs.validator.Validator
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class EstudiantesServiceImplTest {
    
    @MockK
    lateinit var repository: EstudiantesRepository
    
    @MockK
    lateinit var cache: Cache<Int, Estudiante>
    
    @MockK
    lateinit var validator: Validator<Estudiante, EstudiantesError>
    
    @InjectMockKs
    lateinit var service: EstudiantesServiceImpl
    
    @Test
    fun findAll() {
        
        // Arrange
        val mockResult = Estudiante(
            1,
            nombre = "Test",
            fechaNacimiento = LocalDate.of(1990, 1, 1),
            calificacion = 5.5,
            repetidor = true
        )
        // Assert
        every { repository.findAll() } returns listOf(mockResult)
        
        // Act
        val result = service.findAll()
        
        // Assert
        assertAll(
            { assertEquals(1, result.size, "El tamaño de la lista no es correcto") },
            { assertEquals(mockResult.id, result[0].id, "El id del estudiante no es el esperado") },
            { assertEquals(mockResult.nombre, result[0].nombre, "El nombre del estudiante no es el esperado") },
            {
                assertEquals(
                    mockResult.fechaNacimiento,
                    result[0].fechaNacimiento,
                    "La fecha de nacimiento del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockResult.calificacion,
                    result[0].calificacion,
                    "La calificacion del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockResult.repetidor,
                    result[0].repetidor,
                    "El estado de repeticion del estudiante no es el esperado"
                )
            }
        )
        
        // Verify
        verify(atLeast = 1) { repository.findAll() }
    }
    
    @Test
    fun findByIdExisteCache() {
        
        // Arrange
        val mockResult = Estudiante(
            1,
            nombre = "Test",
            fechaNacimiento = LocalDate.of(1990, 1, 1),
            calificacion = 5.5,
            repetidor = true
        )
        
        every { cache.getIfPresent(1) } returns mockResult
        
        // Act
        val result = service.findById(1)
        
        // Assert
        assertAll(
            { assertTrue(result.isOk, "El resultado no es correcto") },
            { assertEquals(mockResult.id, result.get()?.id, "El id del estudiante no es el esperado") },
            { assertEquals(mockResult.nombre, result.get()?.nombre, "El nombre del estudiante no es el esperado") },
            {
                assertEquals(
                    mockResult.fechaNacimiento,
                    result.get()?.fechaNacimiento,
                    "La fecha de nacimiento del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockResult.calificacion,
                    result.get()?.calificacion,
                    "La calificacion del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockResult.repetidor,
                    result.get()?.repetidor,
                    "El estado de repeticion del estudiante no es el esperado"
                )
            }
        )
        
        // Verify
        verify(atLeast = 1) { cache.getIfPresent(1) }
        verify(exactly = 0) { repository.findById(1) }
        verify(exactly = 0) { cache.put(1, mockResult) }
    }
    
    @Test
    fun findByIdNoCacheSiEnBaseDatos() {
        
        // Arrange
        val mockResult = Estudiante(
            1,
            nombre = "Test",
            fechaNacimiento = LocalDate.of(1990, 1, 1),
            calificacion = 5.5,
            repetidor = true
        )
        
        every { cache.getIfPresent(1) } returns null
        every { repository.findById(1) } returns mockResult
        every { cache.put(1, mockResult) } returns Unit
        
        // Act
        val result = service.findById(1)
        
        // Assert
        assertAll(
            { assertTrue(result.isOk, "El resultado no es correcto") },
            { assertEquals(mockResult.id, result.get()?.id, "El id del estudiante no es el esperado") },
            { assertEquals(mockResult.nombre, result.get()?.nombre, "El nombre del estudiante no es el esperado") },
            {
                assertEquals(
                    mockResult.fechaNacimiento,
                    result.get()?.fechaNacimiento,
                    "La fecha de nacimiento del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockResult.calificacion,
                    result.get()?.calificacion,
                    "La calificacion del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockResult.repetidor,
                    result.get()?.repetidor,
                    "El estado de repeticion del estudiante no es el esperado"
                )
            }
        )
        
        // Verify
        verify(atLeast = 1) { cache.getIfPresent(1) }
        verify(atLeast = 1) { repository.findById(1) }
        verify(atLeast = 1) { cache.put(1, mockResult) }
    }
    
    @Test
    fun save() {
        // Arrange
        val mockEstudiante = Estudiante(
            1,
            nombre = "Test",
            fechaNacimiento = LocalDate.of(1990, 1, 1),
            calificacion = 5.5,
            repetidor = true
        )
        every { validator.validate(mockEstudiante) } returns Ok(mockEstudiante)
        every { repository.save(mockEstudiante) } returns mockEstudiante
        
        // Act
        val result = service.save(mockEstudiante)
        
        // Assert
        assertAll(
            { assertTrue(result.isOk, "El resultado no es correcto") },
            { assertEquals(mockEstudiante.id, result.get()?.id, "El id del estudiante no es el esperado") },
            { assertEquals(mockEstudiante.nombre, result.get()?.nombre, "El nombre del estudiante no es el esperado") },
            {
                assertEquals(
                    mockEstudiante.fechaNacimiento,
                    result.get()?.fechaNacimiento,
                    "La fecha de nacimiento del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockEstudiante.calificacion,
                    result.get()?.calificacion,
                    "La calificacion del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockEstudiante.repetidor,
                    result.get()?.repetidor,
                    "El estado de repeticion del estudiante no es el esperado"
                )
            }
        )
        
        // Verify
        verify(atLeast = 1) { validator.validate(mockEstudiante) }
        verify(atLeast = 1) { repository.save(mockEstudiante) }
    }
    
    @Test
    fun saveFailsValidation() {
        // Arrange
        val mockEstudiante = Estudiante(
            1,
            nombre = "Test",
            fechaNacimiento = LocalDate.of(1990, 1, 1),
            calificacion = 15.5,
            repetidor = true
        )
        every { validator.validate(mockEstudiante) } returns Err(EstudiantesError.ValidationError("Error de validación"))
        
        val expectedError = EstudiantesError.ValidationError("Error de validación")
        
        // Act
        val result = service.save(mockEstudiante)
        
        // Assert
        assertAll(
            { assertFalse(result.isOk, "El resultado no es correcto") },
            { assertTrue(result.isErr, "El resultado no es correcto") },
            {
                assertEquals(
                    expectedError.message,
                    result.error.message,
                    "El mensaje de error no es el esperado"
                )
            }
        )
        
        // Verify
        verify(atLeast = 1) { validator.validate(mockEstudiante) }
        verify(exactly = 0) { repository.save(mockEstudiante) }
    }
    
    @Test
    fun update() {
        // Arrange
        val mockEstudiante = Estudiante(
            1,
            nombre = "Test",
            fechaNacimiento = LocalDate.of(1990, 1, 1),
            calificacion = 5.5,
            repetidor = true
        )
        every { validator.validate(mockEstudiante) } returns Ok(mockEstudiante)
        every { cache.invalidate(1) } returns Unit
        every { repository.update(1, mockEstudiante) } returns mockEstudiante
        
        // Act
        val result = service.update(1, mockEstudiante)
        
        // Assert
        assertAll(
            { assertTrue(result.isOk, "El resultado no es correcto") },
            { assertEquals(mockEstudiante.id, result.get()?.id, "El id del estudiante no es el esperado") },
            { assertEquals(mockEstudiante.nombre, result.get()?.nombre, "El nombre del estudiante no es el esperado") },
            {
                assertEquals(
                    mockEstudiante.fechaNacimiento,
                    result.get()?.fechaNacimiento,
                    "La fecha de nacimiento del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockEstudiante.calificacion,
                    result.get()?.calificacion,
                    "La calificacion del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockEstudiante.repetidor,
                    result.get()?.repetidor,
                    "El estado de repeticion del estudiante no es el esperado"
                )
            }
        )
        
        // Verify
        verify(atLeast = 1) { validator.validate(mockEstudiante) }
        verify(atLeast = 1) { cache.invalidate(1) }
        verify(atLeast = 1) { repository.update(1, mockEstudiante) }
    }
    
    // Actualizar si el estudiante no existe en la base de datos
    @Test
    fun updateNoExisteEstudiante() {
        // Arrange
        val mockEstudiante = Estudiante(
            1,
            nombre = "Test",
            fechaNacimiento = LocalDate.of(1990, 1, 1),
            calificacion = 5.5,
            repetidor = true
        )
        every { validator.validate(mockEstudiante) } returns Ok(mockEstudiante)
        every {
            repository.update(
                1,
                mockEstudiante
            )
        } returns null
        
        val expectedError = EstudiantesError.NotFoundError(1)
        
        // Act
        val result = service.update(1, mockEstudiante)
        
        // Assert
        assertAll(
            { assertTrue(result.isErr, "El resultado no es correcto") },
            {
                assertEquals(
                    expectedError.message,
                    result.error.message,
                    "El mensaje de error no es el esperado"
                )
            }
        )
        
        // Verify
        verify(atLeast = 1) { validator.validate(mockEstudiante) }
        verify(atLeast = 0) { cache.invalidate(1) }
        verify(atLeast = 0) { repository.update(1, mockEstudiante) }
    }
    
    // Estudiante validado error al actualizar
    @Test
    fun updateFailsValidation() {
        // Arrange
        val mockEstudiante = Estudiante(
            1,
            nombre = "Test",
            fechaNacimiento = LocalDate.of(1990, 1, 1),
            calificacion = 15.5,
            repetidor = true
        )
        every { validator.validate(mockEstudiante) } returns Err(EstudiantesError.ValidationError("Error de validación"))
        
        val expectedError = EstudiantesError.ValidationError("Error de validación")
        
        // Act
        val result = service.update(1, mockEstudiante)
        
        // Assert
        assertAll(
            { assertTrue(result.isErr, "El resultado no es correcto") },
            {
                assertEquals(
                    expectedError.message,
                    result.error.message,
                    "El mensaje de error no es el esperado"
                )
            }
        )
        
        // Verify
        verify(atLeast = 1) { validator.validate(mockEstudiante) }
        verify(exactly = 0) { cache.invalidate(1) }
        verify(exactly = 0) { repository.update(1, mockEstudiante) }
    }
    
    @Test
    fun delete() {
        val mockEstudiante = Estudiante(
            1,
            nombre = "Test",
            fechaNacimiento = LocalDate.of(1990, 1, 1),
            calificacion = 5.5,
            repetidor = true
        )
        
        every { repository.delete(1) } returns mockEstudiante
        every { cache.invalidate(1) } returns Unit
        
        // Act
        val result = service.delete(1)
        
        // Assert
        assertAll(
            { assertTrue(result.isOk, "El resultado no es correcto") },
            { assertEquals(mockEstudiante.id, result.get()?.id, "El id del estudiante no es el esperado") },
            { assertEquals(mockEstudiante.nombre, result.get()?.nombre, "El nombre del estudiante no es el esperado") },
            {
                assertEquals(
                    mockEstudiante.fechaNacimiento,
                    result.get()?.fechaNacimiento,
                    "La fecha de nacimiento del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockEstudiante.calificacion,
                    result.get()?.calificacion,
                    "La calificacion del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockEstudiante.repetidor,
                    result.get()?.repetidor,
                    "El estado de repeticion del estudiante no es el esperado"
                )
            }
        
        )
        
        // Verify
        verify(atLeast = 1) { repository.delete(1) }
        verify(atLeast = 1) { cache.invalidate(1) }
    }
    
    @Test
    fun deleteNoExisteEstudiante() {
        // Arrange
        every { repository.delete(1) } returns null
        
        val expectedError = EstudiantesError.NotFoundError(1)
        
        // Act
        val result = service.delete(1)
        
        // Assert
        assertAll(
            { assertTrue(result.isErr, "El resultado no es correcto") },
            {
                assertEquals(
                    expectedError.message,
                    result.error.message,
                    "El mensaje de error no es el esperado"
                )
            }
        )
        
        // Verify
        verify(atLeast = 1) { repository.delete(1) }
        verify(atLeast = 0) { cache.invalidate(1) }
    }
}