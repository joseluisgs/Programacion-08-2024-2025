package repositories

import database.DataBaseManager
import dev.joseluisgs.models.Estudiante
import dev.joseluisgs.repositories.estudiantes.EstudiantesRepository
import dev.joseluisgs.repositories.estudiantes.EstudiantesRepositoryImpl
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Necesario para que las variables de la clase se inicialicen una sola vez
class EstudiantesRepositoryImplTest {
    
    private lateinit var repository: EstudiantesRepository
    
    @BeforeAll
    fun setUp() {
        println("Trying to delete test.db before all tests")
        Files.deleteIfExists(Paths.get("test.db"))
        println("Deleted test.db before all tests")
    }
    
    @AfterAll
    fun tearDown() {
        println("Trying to delete test.db after all tests")
        Files.deleteIfExists(Paths.get("test.db"))
        println("Deleted test.db after all tests")
    }
    
    @AfterEach
    fun closeConnections() {
        println("Closing database connections after each test")
        // Aquí deberías cerrar cualquier conexión abierta
        DataBaseManager.close() // Suponiendo que tienes un método para cerrar conexiones
    }
    
    
    @BeforeEach
    fun initRepository() {
        DataBaseManager.initDatabase() // Inicializamos la base de datos
        repository = EstudiantesRepositoryImpl()
    }
    
    @Test
    @DisplayName("Test de obtención de todos los estudiantes")
    fun findAll() {
        val estudiantes = repository.findAll()
        assertAll(
            { assertNotNull(estudiantes, "No debe ser nulo") },
            { assertTrue(estudiantes.isNotEmpty(), "No debe estar vacío") },
            { assertTrue(estudiantes.size == 1, "El tamaño es 1") },
            { assertTrue(estudiantes[0].nombre == "Test", "No debe coincidir el nombre") },
            // Se puede hacer con el resto
            // 'Test', '2000-01-01', 8.5, 0, '2024-03-20T20:30:24.352127400', '2024-03-20T20:30:24.369123200');
        
        )
    }
    
    @Test
    @DisplayName("Test de obtención de un estudiante por ID")
    fun findById() {
        val estudiante = repository.findById(1)
        assertAll(
            { assertNotNull(estudiante, "No debe ser nulo") },
            { assertTrue(estudiante!!.nombre == "Test", "No debe coincidir el nombre") },
            // Se puede hacer con el resto
            // 'Test', '2000-01-01', 8.5, 0, '2024-03-20T20:30:24.352127400', '2024-03-20T20:30:24.369123200');
        
        )
    }
    
    @Test
    @DisplayName("Devuleve null si no existe el estudiante")
    fun findByIdNull() {
        val estudiante = repository.findById(100)
        assertAll(
            { assertTrue(estudiante == null, "Debe ser nulo") }
        )
    }
    
    @Test
    @DisplayName("Test de inserción de un estudiante")
    fun save() {
        val estudiante =
            Estudiante(nombre = "Test Update", fechaNacimiento = LocalDate.now(), calificacion = 8.5, repetidor = false)
        val estudianteGuardado = repository.save(estudiante)
        assertAll(
            { assertNotNull(estudianteGuardado, "No debe ser nulo") },
            { assertTrue(estudianteGuardado.nombre == "Test Update", "No debe coincidir el nombre") },
            // Se puede hacer con el resto
            // 'Test Update', '2000-01-01', 8.5, 0, '2024-03-20T20:30:24.352127400', '2024-03-20T20:30:24.369123200');
        
        )
    }
    
    @Test
    @DisplayName("Test de actualización de un estudiante")
    fun update() {
        val estudiante = repository.findById(1)
        val nuestudianteActualizado = estudiante?.copy(nombre = "Test Update Actualizado")!!
        val estudianteGuardado = repository.update(1, nuestudianteActualizado)
        assertAll(
            { assertNotNull(estudianteGuardado, "No debe ser nulo") },
            { assertTrue(estudianteGuardado!!.nombre == "Test Update Actualizado", "No debe coincidir el nombre") },
            // Se puede hacer con el resto
            // 'Test Update Actualizado', '2000-01-01', 8.5, 0, '2024-03-20T20:30:24.352127400', '2024-03-20T20:30:24.369123200');
        
        )
    }
    
    
    @Test
    @DisplayName("Test de borrado de un estudiante")
    fun delete() {
        val estudiante = repository.findById(1)
        val borrado = repository.delete(1)
        assertAll(
            { assertNotNull(estudiante, "No debe ser nulo") },
            { assertEquals(1, borrado!!.id, "Debe ser 1") },
            { assertTrue(repository.findById(1) == null, "Debe ser nulo") }
        )
    }
    
    
    @Test
    fun findAllPaginated() {
        val estudiantes = repository.findAllPaginated(0, 1)
        assertAll(
            { assertNotNull(estudiantes, "No debe ser nulo") },
            { assertTrue(estudiantes.isNotEmpty(), "No debe estar vacío") },
            { assertTrue(estudiantes.size == 1, "El tamaño es 1") },
            { assertTrue(estudiantes[0].nombre == "Test", "No debe coincidir el nombre") },
            // Se puede hacer con el resto
            // 'Test', '2000-01-01', 8.5, 0, '2024-03-20T20:30:24.352127400', '2024-03-20T20:30:24.369123200');
        
        )
    }
    
    // El resto lo haces tú
}