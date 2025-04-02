package repositories

import dev.joseluisgs.dao.EstudianteEntity
import dev.joseluisgs.dao.EstudiantesDao
import dev.joseluisgs.mappers.toModel
import dev.joseluisgs.models.Estudiante
import dev.joseluisgs.repositories.EstudiantesRepositoryImpl
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@ExtendWith(MockKExtension::class)
class EstudiantesRepositoryImplTestWithMock {
    //Cremos el mock de databaseManager
    @MockK
    private lateinit var dao: EstudiantesDao
    
    // Creamos el repositoriocon la inyección de dependencias
    @InjectMockKs
    private lateinit var estudiantesRepository: EstudiantesRepositoryImpl
    
    @Test
    @DisplayName("Debe devolver todos los estudiantes")
    fun findAll() {
        // arrange
        val estudianteEntity = EstudianteEntity(
            id = 1,
            nombre = "Juan",
            fechaNacimiento = "2000-01-01",
            calificacion = 8.5,
            repetidor = 0,
            createdAt = "2023-01-01T00:00:00",
            updatedAt = "2023-01-01T00:00:00"
        )
        
        every { dao.findAll() } returns listOf(estudianteEntity)
        
        // Act
        val estudiantes = estudiantesRepository.findAll()
        
        // Assert
        assertAll(
            { assertNotNull(estudiantes.size == 1, "El objeto no es nulo") },
            { assertEquals(1, estudiantes.size, "Debe devolver un objeto") },
            { assertEquals(1, estudiantes[0].id, "El id del estudiante es correcto") },
            { assertEquals("Juan", estudiantes[0].nombre, "El nombre del estudiante es correcto") },
            {
                assertEquals(
                    LocalDate.parse("2000-01-01"),
                    estudiantes[0].fechaNacimiento,
                    "La fecha de nacimiento del estudiante es correcta"
                )
            },
            { assertEquals(8.5, estudiantes[0].calificacion, "La calificación del estudiante es correcta") },
            { assertEquals(false, estudiantes[0].repetidor, "El estudiante no es repetidor") },
            {
                assertEquals(
                    LocalDateTime.parse("2023-01-01T00:00:00"),
                    estudiantes[0].createdAt,
                    "La fecha de creación del estudiante es correcta"
                )
            },
            {
                assertEquals(
                    LocalDateTime.parse("2023-01-01T00:00:00"),
                    estudiantes[0].updatedAt,
                    "La fecha de actualización del estudiante es correcta"
                )
            }
        )
        
        // verify
        verify(atLeast = 1) { dao.findAll() }
    }
    
    @Test
    @DisplayName("Debe devolver un estudiante por su id")
    fun findById() {
        // arrange
        val mockResult = EstudianteEntity(
            id = 1,
            nombre = "Juan",
            fechaNacimiento = "2000-01-01",
            calificacion = 8.5,
            repetidor = 0,
            createdAt = "2023-01-01T00:00:00",
            updatedAt = "2023-01-01T00:00:00"
        )
        
        every { dao.findById(1) } returns mockResult
        
        // Act
        val estudiante = estudiantesRepository.findById(1)
        
        // Assert
        assertAll(
            { assertNotNull(estudiante, "El objeto no es nulo") },
            { assertEquals(1, estudiante!!.id, "El id del estudiante es correcto") },
            { assertEquals("Juan", estudiante!!.nombre, "El nombre del estudiante es correcto") },
            {
                assertEquals(
                    LocalDate.parse("2000-01-01"),
                    estudiante!!.fechaNacimiento,
                    "La fecha de nacimiento del estudiante es correcta"
                )
            },
            { assertEquals(8.5, estudiante!!.calificacion, "La calificación del estudiante es correcta") },
            { assertEquals(false, estudiante!!.repetidor, "El estudiante no es repetidor") },
            {
                assertEquals(
                    LocalDateTime.parse("2023-01-01T00:00:00"),
                    estudiante!!.createdAt,
                    "La fecha de creación del estudiante es correcta"
                )
            },
            {
                assertEquals(
                    LocalDateTime.parse("2023-01-01T00:00:00"),
                    estudiante!!.updatedAt,
                    "La fecha de actualización del estudiante es correcta"
                )
            }
        )
        
        // verify
        verify(atLeast = 1) { dao.findById(1) }
    }
    
    
    @Test
    @DisplayName("Debe devolver null si no encuentra un estudiante por su id")
    fun findByIdNotFound() {
        // arrange
        val mockResult = null
        
        every { dao.findById(1) } returns mockResult
        
        // Act
        val estudiante = estudiantesRepository.findById(1)
        
        // Assert
        assert(estudiante == null)
        
        // verify
        verify(atLeast = 1) { dao.findById(1) }
    }
    
    
    @Test
    @DisplayName("Debe de insertar un nuevo estudiante")
    fun save() {
        // arrange
        val mockResult = EstudianteEntity(
            id = 0,
            nombre = "Juan",
            fechaNacimiento = "2000-01-01",
            calificacion = 8.5,
            repetidor = 0,
            createdAt = "2023-01-01T00:00:00",
            updatedAt = "2023-01-01T00:00:00"
        )
        
        val model = mockResult.toModel().copy(id = 1)
        
        // Usamos match para comprobar que los valores son correctos, no necesitamos las fechas y el id.
        every {
            dao.save(match {
                it.nombre == "Juan" &&
                        it.fechaNacimiento == "2000-01-01" &&
                        it.calificacion == 8.5 &&
                        it.repetidor == 0
            })
        } returns 1
        
        // Act
        val result = estudiantesRepository.save(model)
        
        // Assert
        assertAll(
            { assertNotNull(result, "El objeto no es nulo") },
            { assertEquals(1, result.id, "El id del estudiante es correcto") },
            { assertEquals("Juan", result.nombre, "El nombre del estudiante es correcto") },
            {
                assertEquals(
                    LocalDate.parse("2000-01-01"),
                    result.fechaNacimiento,
                    "La fecha de nacimiento del estudiante es correcta"
                )
            },
            { assertEquals(8.5, result.calificacion, "La calificación del estudiante es correcta") },
            { assertEquals(false, result.repetidor, "El estudiante no es repetidor") },
            { assertNotNull(result.createdAt, "La fecha de creación del estudiante no es nula") },
            { assertNotNull(result.updatedAt, "La fecha de actualización del estudiante no es nula") }
        )
        
        // verify
        verify(atLeast = 1) { dao.save(any()) }
    }
    
    
    @Test
    @DisplayName("Debe de actualizar un estudiante")
    fun update() {
        // Arrange
        val mockResult = EstudianteEntity(
            id = 1,
            nombre = "Juan",
            fechaNacimiento = "2000-01-01",
            calificacion = 8.5,
            repetidor = 0,
            createdAt = "2023-01-01T00:00:00",
            updatedAt = "2023-01-01T00:00:00"
        )
        
        val model: Estudiante = mockResult.toModel().copy(id = 1)
        
        every { dao.findById(1) } returns mockResult
        
        every {
            dao.update(match {
                it.id == 1 &&
                        it.nombre == "Juan" &&
                        it.fechaNacimiento == "2000-01-01" &&
                        it.calificacion == 8.5 &&
                        it.repetidor == 0
            })
        } returns 1
        
        // Act
        val result = estudiantesRepository.update(1, model)
        
        // Assert
        assertAll(
            { assertNotNull(result, "El objeto no es nulo") },
            { assertEquals(1, result!!.id, "El id del estudiante es correcto") },
            { assertEquals("Juan", result!!.nombre, "El nombre del estudiante es correcto") },
            {
                assertEquals(
                    LocalDate.parse("2000-01-01"),
                    result!!.fechaNacimiento,
                    "La fecha de nacimiento del estudiante es correcta"
                )
            },
            { assertEquals(8.5, result!!.calificacion, "La calificación del estudiante es correcta") },
            { assertEquals(false, result!!.repetidor, "El estudiante no es repetidor") },
            { assertNotNull(result!!.createdAt, "La fecha de creación del estudiante no es nula") },
            { assertNotNull(result!!.updatedAt, "La fecha de actualización del estudiante no es nula") }
        )
        
        // Verify
        verify(atLeast = 1) { dao.findById(1) }
        verify(atLeast = 1) { dao.update(any()) }
    }
    
    
    @Test
    @DisplayName("No debe de actualizar un estudiante si no existe")
    fun updateNotFound() {
        // Arrange
        val mockResult = null
        
        val model = Estudiante(
            id = 1,
            nombre = "Juan",
            fechaNacimiento = LocalDate.parse("2000-01-01"),
            calificacion = 8.5,
            repetidor = false
        )
        
        every { dao.findById(1) } returns mockResult
        
        // Act
        val result = estudiantesRepository.update(1, model)
        
        // Assert
        assertNull(result, "El objeto es nulo")
        
        // Verify
        verify(atLeast = 1) { dao.findById(1) }
        verify(exactly = 0) { dao.update(any()) }
        
        
    }
    
    
    @Test
    @DisplayName("Debe de borrar un estudiante")
    fun delete() {
        // Arrange
        val mockResult = EstudianteEntity(
            id = 1,
            nombre = "Juan",
            fechaNacimiento = "2000-01-01",
            calificacion = 8.5,
            repetidor = 0,
            createdAt = "2023-01-01T00:00:00",
            updatedAt = "2023-01-01T00:00:00"
        )
        
        val mockResultFind = mockResult.copy()
        
        every { dao.findById(1) } returns mockResultFind
        
        every { dao.delete(1) } returns 1
        
        val model = Estudiante(
            id = 1,
            nombre = "Juan",
            fechaNacimiento = LocalDate.parse("2000-01-01"),
            calificacion = 8.5,
            repetidor = false
        )
        
        // Act
        val result = estudiantesRepository.delete(1)
        
        // Assert
        assertAll(
            { assertEquals(1, result?.id, "El id resultado es correcto") },
            { assertEquals("Juan", result?.nombre, "El nombre resultado es correcto") },
            {
                assertEquals(
                    LocalDate.parse("2000-01-01"),
                    result?.fechaNacimiento,
                    "La fecha de nacimiento resultado es correcta"
                )
            },
            { assertEquals(8.5, result?.calificacion, "La calificación resultado es correcta") },
            { assertEquals(false, result?.repetidor, "El estudiante no es repetidor") }
        
        )
        
        // Verify
        verify(atLeast = 1) { dao.delete(1) }
        verify(atLeast = 1) { dao.findById(1) }
    }
    
    
    @Test
    @DisplayName("No debe de borrar un estudiante si no existe")
    fun deleteNotFound() {
        // Arrange
        val mockResult = null
        
        every { dao.findById(1) } returns mockResult
        
        // Act
        val result = estudiantesRepository.delete(1)
        
        // Assert
        assertNull(result, "El objeto es nulo")
        
        // Verify
        verify(atLeast = 0) { dao.delete(1) }
        verify(exactly = 1) { dao.findById(1) }
    }
    
    
    @Test
    @DisplayName("Debe devolver todos los estudiantes paginados")
    fun findAllPaginated() {
        // Arrange
        val mockResult = mutableListOf(
            EstudianteEntity(
                id = 1,
                nombre = "Juan",
                fechaNacimiento = "2000-01-01",
                calificacion = 8.5,
                repetidor = 0,
                createdAt = "2023-01-01T00:00:00",
                updatedAt = "2023-01-01T00:00:00"
            ),
            EstudianteEntity(
                id = 2,
                nombre = "Pedro",
                fechaNacimiento = "2001-01-01",
                calificacion = 9.0,
                repetidor = 0,
                createdAt = "2023-01-01T00:00:00",
                updatedAt = "2023-01-01T00:00:00"
            )
        )
        
        every { dao.findAll(2, 0) } returns mockResult
        
        // Act
        val result = estudiantesRepository.findAllPaginated(1, 2)
        
        // Assert
        assertAll(
            { assertEquals(2, result.size, "El tamaño del resultado es correcto") },
            {
                assertEquals(
                    "Juan",
                    result[0].nombre,
                    "El nombre del estudiante 1 es correcto"
                )
            },
            {
                assertEquals(
                    "Pedro",
                    result[1].nombre,
                    "El nombre del estudiante 2 es correcto"
                )
            }
        )
        
        // Verify
        verify(atLeast = 1) { dao.findAll(2, 0) }
    }


// El resto hazlo tu!!!
    
    @Test
    @DisplayName("Debe devolver el estudiante con la mayor calificación")
    fun findMaximaCalificacion() {
    }
    
    fun findByNombre() {
    }
    
    @Test
    fun findByCalificacion() {
    }
    
    @Test
    fun findByRepetidor() {
    }
    
    @Test
    fun mediaCalificaciones() {
    }
    
    @Test
    fun maximaCalificacion() {
    }
    
    @Test
    fun minimaCalificacion() {
    }
    
    @Test
    fun numAprobados() {
    }
    
    @Test
    fun numSuspensos() {
    }
}