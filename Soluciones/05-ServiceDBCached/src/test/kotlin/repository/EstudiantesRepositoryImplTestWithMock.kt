package repository

import dev.joseluisgs.database.DatabaseManager
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
    private lateinit var databaseManager: DatabaseManager
    
    // Creamos el repositoriocon la inyección de dependencias
    @InjectMockKs
    private lateinit var estudiantesRepository: EstudiantesRepositoryImpl
    
    @Test
    @DisplayName("Debe devolver todos los estudiantes")
    fun findAll() {
        // arrange
        val mockResult = listOf(
            mapOf(
                "id" to 1,
                "nombre" to "Juan",
                "fecha_nacimiento" to "2000-01-01",
                "calificacion" to 8.5,
                "repetidor" to 0,
                "created_at" to "2023-01-01T00:00:00",
                "updated_at" to "2023-01-01T00:00:00"
            )
        )
        val sql = "SELECT * FROM Estudiantes"
        every { databaseManager.select(sql) } returns mockResult
        
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
        verify(atLeast = 1) { databaseManager.select(sql) }
    }
    
    @Test
    @DisplayName("Debe devolver un estudiante por su id")
    fun findById() {
        // arrange
        val mockResult = mapOf(
            "id" to 1,
            "nombre" to "Juan",
            "fecha_nacimiento" to "2000-01-01",
            "calificacion" to 8.5,
            "repetidor" to 0,
            "created_at" to "2023-01-01T00:00:00",
            "updated_at" to "2023-01-01T00:00:00"
        )
        val sql = "SELECT * FROM Estudiantes WHERE id = ?"
        every { databaseManager.select(sql, 1) } returns listOf(mockResult)
        
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
        verify(atLeast = 1) { databaseManager.select(sql, 1) }
    }
    
    @Test
    @DisplayName("Debe devolver null si no encuentra un estudiante por su id")
    fun findByIdNotFound() {
        // arrange
        val mockResult = emptyList<Map<String, Any>>()
        val sql = "SELECT * FROM Estudiantes WHERE id = ?"
        every { databaseManager.select(sql, 1) } returns mockResult
        
        // Act
        val estudiante = estudiantesRepository.findById(1)
        
        // Assert
        assert(estudiante == null)
        
        // verify
        verify(atLeast = 1) { databaseManager.select(sql, 1) }
    }
    
    @Test
    @DisplayName("Debe de insertar un nuevo estudiante")
    fun save() {
        // arrange
        val estudiante = Estudiante(
            nombre = "Juan",
            fechaNacimiento = LocalDate.parse("2000-01-01"),
            calificacion = 8.5,
            repetidor = false
        )
        
        val sql = """
        INSERT INTO Estudiantes (nombre, fecha_nacimiento, calificacion, repetidor, created_at, updated_at)
        VALUES (?, ?, ?, ?, ?, ?)
    """.trimIndent()
        
        every {
            databaseManager.insertAndGetId(
                sql,
                estudiante.nombre,
                estudiante.fechaNacimiento.toString(), // Cuideado con las fechas, que van como String
                estudiante.calificacion,
                estudiante.repetidor,
                any(), // usamos any para las fechas porque no podemos saber exactamente el valor
                any()
            )
        } returns 1 // Int
        
        // Act
        val result = estudiantesRepository.save(estudiante)
        
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
        verify(atLeast = 1) {
            databaseManager.insertAndGetId(
                sql,
                estudiante.nombre,
                estudiante.fechaNacimiento.toString(),
                estudiante.calificacion,
                estudiante.repetidor,
                any(),
                any()
            )
        }
    }
    
    
    @Test
    @DisplayName("Debe de actualizar un estudiante")
    fun update() {
        // Arrange
        val estudiante = Estudiante(
            id = 1,
            nombre = "Juan",
            fechaNacimiento = LocalDate.parse("2000-01-01"),
            calificacion = 8.5,
            repetidor = false
        )
        
        val sqlUpdate = """
        UPDATE Estudiantes
        SET nombre = ?, fecha_nacimiento = ?, calificacion = ?, repetidor = ?, updated_at = ?
        WHERE id = ?
    """.trimIndent()
        
        val sqlSelect = "SELECT * FROM Estudiantes WHERE id = ?"
        
        every {
            databaseManager.select(sqlSelect, estudiante.id)
        } returns listOf(
            mapOf(
                "id" to 1,
                "nombre" to "Juan",
                "fecha_nacimiento" to "2000-01-01",
                "calificacion" to 8.5,
                "repetidor" to 0,
                "created_at" to "2023-01-01T00:00:00",
                "updated_at" to "2023-01-01T00:00:00"
            )
        )
        
        every {
            databaseManager.update(
                sqlUpdate,
                estudiante.nombre,
                estudiante.fechaNacimiento,
                estudiante.calificacion,
                estudiante.repetidor,
                any(),
                estudiante.id
            )
        } returns 1 // Simula que una fila fue afectada por la actualización
        
        // Act
        val result = estudiantesRepository.update(1, estudiante)
        
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
        verify(atLeast = 1) {
            databaseManager.select(sqlSelect, estudiante.id)
            databaseManager.update(
                sqlUpdate,
                estudiante.nombre,
                estudiante.fechaNacimiento,
                estudiante.calificacion,
                estudiante.repetidor,
                any(),
                estudiante.id
            )
        }
        
        verify(atLeast = 1) { databaseManager.select(sqlSelect, estudiante.id) }
    }
    
    @Test
    @DisplayName("No debe de actualizar un estudiante si no existe")
    fun updateNotFound() {
        // Arrange
        val estudiante = Estudiante(
            id = 1,
            nombre = "Juan",
            fechaNacimiento = LocalDate.parse("2000-01-01"),
            calificacion = 8.5,
            repetidor = false
        )
        
        val sqlSelect = "SELECT * FROM Estudiantes WHERE id = ?"
        
        every {
            databaseManager.select(sqlSelect, estudiante.id)
        } returns emptyList()
        
        // Act
        val result = estudiantesRepository.update(1, estudiante)
        
        // Assert
        assertNull(result, "El objeto es nulo")
        
        // Verify
        verify(atLeast = 1) { databaseManager.select(sqlSelect, estudiante.id) }
    }
    
    
    @Test
    @DisplayName("Debe de borrar un estudiante")
    fun delete() {
        // Arrange
        val estudiante = Estudiante(
            id = 1,
            nombre = "Juan",
            fechaNacimiento = LocalDate.parse("2000-01-01"),
            calificacion = 8.5,
            repetidor = false
        )
        
        val sqlSelect = "SELECT * FROM Estudiantes WHERE id = ?"
        
        every {
            databaseManager.select(sqlSelect, estudiante.id)
        } returns listOf(
            mapOf(
                "id" to 1,
                "nombre" to "Juan",
                "fecha_nacimiento" to "2000-01-01",
                "calificacion" to 8.5,
                "repetidor" to 0,
                "created_at" to "2023-01-01T00:00:00",
                "updated_at" to "2023-01-01T00:00:00"
            )
        )
        
        val sqlDelete = "DELETE FROM Estudiantes WHERE id = ?"
        
        every {
            databaseManager.delete(sqlDelete, estudiante.id)
        } returns 1 // Simula que una fila fue afectada por la eliminación
        
        // Act
        val result = estudiantesRepository.delete(1)
        
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
        verify(atLeast = 1) { databaseManager.delete(sqlDelete, estudiante.id) }
        
        verify(atLeast = 1) { databaseManager.select(sqlSelect, estudiante.id) }
    }
    
    @Test
    @DisplayName("No debe de borrar un estudiante si no existe")
    fun deleteNotFound() {
        // Arrange
        val estudiante = Estudiante(
            id = 1,
            nombre = "Juan",
            fechaNacimiento = LocalDate.parse("2000-01-01"),
            calificacion = 8.5,
            repetidor = false
        )
        
        val sqlSelect = "SELECT * FROM Estudiantes WHERE id = ?"
        
        every {
            databaseManager.select(sqlSelect, estudiante.id)
        } returns emptyList()
        
        // Act
        val result = estudiantesRepository.delete(1)
        
        // Assert
        assertNull(result, "El objeto es nulo")
        
        // Verify
        verify(atLeast = 1) { databaseManager.select(sqlSelect, estudiante.id) }
    }
    
    @Test
    @DisplayName("Debe devolver todos los estudiantes paginados")
    fun findAllPaginated() {
        // Arrange
        val sqlSelect = "SELECT * FROM Estudiantes LIMIT ? OFFSET ?"
        
        every {
            databaseManager.select(sqlSelect, 5, 0)
        } returns listOf(
            mapOf(
                "id" to 1,
                "nombre" to "Juan",
                "fecha_nacimiento" to "2000-01-01",
                "calificacion" to 8.5,
                "repetidor" to 0,
                "created_at" to "2023-01-01T00:00:00",
                "updated_at" to "2023-01-01T00:00:00"
            )
        )
        
        // Act
        val estudiantes = estudiantesRepository.findAllPaginated(1, 5)
        
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
        
        // Verify
        verify(atLeast = 1) { databaseManager.select(sqlSelect, 5, 0) }
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