package dev.joseluisgs.productos.dao


import dev.joseluisgs.common.locale.LocaleFormatter.roundTo
import dev.joseluisgs.global.database.provideDatabaseManager
import dev.joseluisgs.productos.model.Categoria
import dev.joseluisgs.productos.model.Producto
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertAll
import java.time.LocalDateTime
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductosDaoTest {
    private lateinit var dao: ProductosDao
    
    @BeforeEach
    fun setUp() {
        val jbi = provideDatabaseManager()
        dao = provideProductosDao(jbi)
    }
    
    @Nested
    @DisplayName("Tests positivos de ProductosRepository")
    inner class PositiveTests {
        
        @Test
        @DisplayName("findAll devuelve todos los productos correctamente")
        fun findAll() {
            val result = dao.findAll()
            
            assertAll(
                { assertNotNull(result, "La lista de productos no debe ser nula") },
                { assertEquals(10, result.size, "Debe haber 10 productos en la lista") },
                {
                    assertTrue(
                        result.any { it.nombre == "Laptop HP Pavilion" },
                        "Debe existir el producto Laptop HP Pavilion"
                    )
                }
            )
        }
        
        @Test
        @DisplayName("findById encuentra un producto existente")
        fun findById() {
            val result = dao.findById(1L)
            
            assertAll(
                { assertNotNull(result, "El producto no debe ser nulo") },
                { assertEquals(1L, result?.id, "El ID del producto debe ser 1") },
                {
                    assertTrue(
                        result?.nombre?.contains("Laptop HP Pavilion") ?: false,
                        "El nombre del producto debe ser Laptop HP Pavilion"
                    )
                },
                { assertEquals(899.99, result?.precio?.roundTo(2), "El precio debe ser 899.99") }
            )
        }
        
        @Test
        @DisplayName("save guarda un nuevo producto correctamente")
        fun save() {
            val nuevo = ProductoEntity(
                id = Producto.NEW_ID,
                nombre = "Nuevo Producto",
                precio = 99.99,
                stock = 10,
                categoria = Categoria.OTROS.name,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                deleted = false
            )
            
            val result = dao.save(nuevo)
            
            assertAll(
                { assertNotNull(result, "El producto guardado no debe ser nulo") },
                { assertNotNull(result, "El ID del producto no debe ser nulo") },
                { assertEquals(11, result, "El ID del producto guardado debe ser 11") },
            )
        }
        
        @Test
        @DisplayName("update actualiza un producto existente")
        fun update() {
            val actualizado = ProductoEntity(
                id = 1L,
                nombre = "Laptop HP Pavilion Actualizado",
                precio = 999.99,
                stock = 20,
                categoria = Categoria.ELECTRONICA.name,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                deleted = false
            )
            
            val result = dao.update(actualizado)
            
            assertAll(
                { assertNotNull(result, "El producto actualizado no debe ser nulo") },
                { assertEquals(1, result, "El numero de filas actualizado debe ser 1") },
            )
        }
        
        @Test
        @DisplayName("findByNombre encuentra productos por nombre")
        fun findByNombre() {
            val result = dao.findByName("%iPhone%")
            
            assertAll(
                { assertNotNull(result, "La lista no debe ser nula") },
                { assertTrue(result.isNotEmpty(), "La lista no debe estar vacía") },
                {
                    assertTrue(
                        result.all { it.nombre.contains("iPhone") },
                        "Todos los productos deben contener iPhone en el nombre"
                    )
                }
            )
        }
        
        @Test
        @DisplayName("findByCategoria encuentra productos por categoría")
        fun findByCategoria() {
            val result = dao.findByCategoria("ELECTRONICA")
            
            assertAll(
                { assertNotNull(result, "La lista no debe ser nula") },
                { assertTrue(result.isNotEmpty(), "La lista no debe estar vacía") },
                {
                    assertTrue(
                        result.all { it.categoria == Categoria.ELECTRONICA.name },
                        "Todos los productos deben ser de categoría ELECTRONICA"
                    )
                }
            )
        }
        
        @Test
        @DisplayName("findAllPaginated devuelve productos paginados correctamente")
        fun findAllPaginated() {
            val result = dao.findAll(1, 5)
            
            assertAll(
                { assertNotNull(result, "La lista paginada no debe ser nula") },
                { assertEquals(1, result.size, "Debe devolver 1 productos") }
            )
        }
    }
    
    @Nested
    @DisplayName("Tests negativos de ProductosRepository")
    inner class NegativeTests {
        
        @Test
        @DisplayName("findById devuelve null para ID inexistente")
        fun findByIdNotFound() {
            val result = dao.findById(999L)
            assertNull(result, "Debe devolver null para un ID inexistente")
        }
        
        @Test
        @DisplayName("update devuelve null para ID inexistente")
        fun updateNotFound() {
            val producto = ProductoEntity(
                id = 999L,
                nombre = "No existe",
                precio = 99.99,
                stock = 10,
                categoria = Categoria.OTROS.name,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                deleted = false
            )
            
            val result = dao.update(producto)
            assertEquals(0, result, "Debe devolver 0 al actualizar un producto inexistente")
        }
        
        @Test
        @DisplayName("delete devuelve null para ID inexistente")
        fun deleteNotFound() {
            val result = dao.delete(999L)
            assertEquals(0, result, "Debe devolver 0 al eliminar un producto inexistente")
        }
        
        @Test
        @DisplayName("findByNombre devuelve lista vacía para nombre inexistente")
        fun findByNombreNotFound() {
            val result = dao.findByName("ProductoInexistente")
            assertTrue(result.isEmpty(), "Debe devolver una lista vacía para un nombre inexistente")
        }
        
        @Test
        @DisplayName("findByCategoria devuelve lista vacía para categoría inexistente")
        fun findByCategoriaNotFound() {
            val result = dao.findByCategoria("CATEGORIA_INEXISTENTE")
            assertTrue(result.isEmpty(), "Debe devolver una lista vacía para una categoría inexistente")
        }
        
        @Test
        @DisplayName("findAllPaginated con parámetros inválidos devuelve valores por defecto")
        fun findAllPaginatedInvalidParams() {
            
            val result = assertThrows<UnableToExecuteStatementException> {
                dao.findAll(-1, -5)
            }
            assertAll(
                { assertNotNull(result, "La lista paginada no debe ser nula") },
            )
            
        }
    }
}
