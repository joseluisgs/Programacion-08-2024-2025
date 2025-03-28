package service

import dev.joseluisgs.cache.Cache
import dev.joseluisgs.exception.EstudiantesException
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
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class EstudiantesServiceImplTest {
    
    @MockK
    lateinit var repository: EstudiantesRepository
    
    @MockK
    lateinit var cache: Cache<Int, Estudiante>
    
    @MockK
    lateinit var validator: Validator<Estudiante>
    
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
        
        every { cache.get(1) } returns mockResult
        
        // Act
        val result = service.findById(1)
        
        // Assert
        assertAll(
            { assertEquals(mockResult.id, result.id, "El id del estudiante no es el esperado") },
            { assertEquals(mockResult.nombre, result.nombre, "El nombre del estudiante no es el esperado") },
            {
                assertEquals(
                    mockResult.fechaNacimiento,
                    result.fechaNacimiento,
                    "La fecha de nacimiento del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockResult.calificacion,
                    result.calificacion,
                    "La calificacion del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockResult.repetidor,
                    result.repetidor,
                    "El estado de repeticion del estudiante no es el esperado"
                )
            }
        )
        
        // Verify
        verify(atLeast = 1) { cache.get(1) }
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
        
        every { cache.get(1) } returns null
        every { repository.findById(1) } returns mockResult
        every { cache.put(1, mockResult) } returns mockResult
        
        // Act
        val result = service.findById(1)
        
        // Assert
        assertAll(
            { assertEquals(mockResult.id, result.id, "El id del estudiante no es el esperado") },
            { assertEquals(mockResult.nombre, result.nombre, "El nombre del estudiante no es el esperado") },
            {
                assertEquals(
                    mockResult.fechaNacimiento,
                    result.fechaNacimiento,
                    "La fecha de nacimiento del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockResult.calificacion,
                    result.calificacion,
                    "La calificacion del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockResult.repetidor,
                    result.repetidor,
                    "El estado de repeticion del estudiante no es el esperado"
                )
            }
        )
        
        // Verify
        verify(atLeast = 1) { cache.get(1) }
        verify(atLeast = 1) { repository.findById(1) }
        verify(atLeast = 1) { cache.put(1, mockResult) }
    }
    
    @Test
    fun findByIdNotFound() {
        // Arrange
        every { repository.findById(1) } returns null
        every { cache.get(1) } returns null
        
        // Act
        val res = assertThrows<EstudiantesException.NotFoundException> {
            service.findById(1)
        }
        
        // Assert
        assertEquals("Estudiante no encontrado con id: 1", res.message, "El mensaje de error no es el esperado")
        
        // Verify
        verify(atLeast = 1) { repository.findById(1) }
        verify(atLeast = 1) { cache.get(1) }
        verify(exactly = 0) { cache.put(1, any()) }
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
        every { validator.validate(mockEstudiante) } returns mockEstudiante
        every { repository.save(mockEstudiante) } returns mockEstudiante
        
        // Act
        val result = service.save(mockEstudiante)
        
        // Assert
        assertAll(
            { assertEquals(mockEstudiante.id, result.id, "El id del estudiante no es el esperado") },
            { assertEquals(mockEstudiante.nombre, result.nombre, "El nombre del estudiante no es el esperado") },
            {
                assertEquals(
                    mockEstudiante.fechaNacimiento,
                    result.fechaNacimiento,
                    "La fecha de nacimiento del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockEstudiante.calificacion,
                    result.calificacion,
                    "La calificacion del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockEstudiante.repetidor,
                    result.repetidor,
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
        every { validator.validate(mockEstudiante) } throws EstudiantesException.ValidationException("Error de validación")
        
        // Act
        val result = try {
            service.save(mockEstudiante)
        } catch (e: Exception) {
            e.message
        }
        
        // Assert
        assertEquals("Estudiante no válido: Error de validación", result, "El mensaje de error no es el esperado")
        
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
        every { validator.validate(mockEstudiante) } returns mockEstudiante
        every { cache.remove(1) } returns mockEstudiante
        every { repository.update(1, mockEstudiante) } returns mockEstudiante
        
        // Act
        val result = service.update(1, mockEstudiante)
        
        // Assert
        assertAll(
            { assertEquals(mockEstudiante.id, result.id, "El id del estudiante no es el esperado") },
            { assertEquals(mockEstudiante.nombre, result.nombre, "El nombre del estudiante no es el esperado") },
            {
                assertEquals(
                    mockEstudiante.fechaNacimiento,
                    result.fechaNacimiento,
                    "La fecha de nacimiento del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockEstudiante.calificacion,
                    result.calificacion,
                    "La calificacion del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockEstudiante.repetidor,
                    result.repetidor,
                    "El estado de repeticion del estudiante no es el esperado"
                )
            }
        )
        
        // Verify
        verify(atLeast = 1) { validator.validate(mockEstudiante) }
        verify(atLeast = 1) { cache.remove(1) }
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
        every { validator.validate(mockEstudiante) } returns mockEstudiante
        every {
            repository.update(
                1,
                mockEstudiante
            )
        } returns null
        
        // Act
        val res = assertThrows<EstudiantesException.NotFoundException> {
            service.update(1, mockEstudiante)
        }
        
        // Assert
        assertEquals("Estudiante no encontrado con id: 1", res.message, "El mensaje de error no es el esperado")
        
        // Verify
        verify(atLeast = 1) { validator.validate(mockEstudiante) }
        verify(atLeast = 0) { cache.remove(1) }
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
        every { validator.validate(mockEstudiante) } throws EstudiantesException.ValidationException("Error de validación")
        
        // Act
        val result = assertThrows<EstudiantesException.ValidationException> {
            service.update(1, mockEstudiante)
        }
        // Assert
        assertEquals(
            "Estudiante no válido: Error de validación",
            result.message,
            "El mensaje de error no es el esperado"
        )
        
        // Verify
        verify(atLeast = 1) { validator.validate(mockEstudiante) }
        verify(exactly = 0) { cache.remove(1) }
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
        every { cache.remove(1) } returns mockEstudiante
        
        // Act
        val result = service.delete(1)
        
        // Assert
        assertAll(
            { assertEquals(mockEstudiante.id, result.id, "El id del estudiante no es el esperado") },
            { assertEquals(mockEstudiante.nombre, result.nombre, "El nombre del estudiante no es el esperado") },
            {
                assertEquals(
                    mockEstudiante.fechaNacimiento,
                    result.fechaNacimiento,
                    "La fecha de nacimiento del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockEstudiante.calificacion,
                    result.calificacion,
                    "La calificacion del estudiante no es la esperada"
                )
            },
            {
                assertEquals(
                    mockEstudiante.repetidor,
                    result.repetidor,
                    "El estado de repeticion del estudiante no es el esperado"
                )
            }
        )
        
        // Verify
        verify(atLeast = 1) { repository.delete(1) }
        verify(atLeast = 1) { cache.remove(1) }
    }
    
    @Test
    fun deleteNoExisteEstudiante() {
        // Arrange
        every { repository.delete(1) } returns null
        
        // Act
        val result = assertThrows<EstudiantesException.NotFoundException> {
            service.delete(1)
        }
        
        // Assert
        assertEquals("Estudiante no encontrado con id: 1", result.message, "El mensaje de error no es el esperado")
        
        // Verify
        verify(atLeast = 1) { repository.delete(1) }
        verify(atLeast = 0) { cache.remove(1) }
    }
}