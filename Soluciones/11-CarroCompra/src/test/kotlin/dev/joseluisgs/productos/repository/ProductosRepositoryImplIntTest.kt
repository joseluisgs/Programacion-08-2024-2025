/*
package productos.repository

import common.locale.LocaleFormatter.roundTo
import dev.joseluisgs.global.database.provideDatabaseManager
import dev.joseluisgs.productos.dao.provideProductosDao
import dev.joseluisgs.productos.model.Categoria
import dev.joseluisgs.productos.model.Producto
import dev.joseluisgs.productos.repository.ProductosRepositoryImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductosRepositoryImplTest {
    private lateinit var repository: ProductosRepositoryImpl
    
    @BeforeEach
    fun setUp() {
        println("Configurando la base de datos para los tests")
        val jbi = provideDatabaseManager()
        val dao = provideProductosDao(jbi)
        repository = ProductosRepositoryImpl(dao)
    }
    
    @Nested
    @DisplayName("Tests positivos de ProductosRepository")
    inner class PositiveTests {
        
        @Test
        @DisplayName("findAll devuelve todos los productos correctamente")
        fun findAll() {
            val result = repository.findAll()
            
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
            val result = repository.findById(1L)
            
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
            val nuevo = Producto(
                nombre = "Nuevo Producto",
                precio = 99.99,
                stock = 10,
                categoria = Categoria.OTROS
            )
            
            val result = repository.save(nuevo)
            
            assertAll(
                { assertNotNull(result, "El producto guardado no debe ser nulo") },
                { assertNotNull(result.id, "El ID del producto no debe ser nulo") },
                { assertEquals(nuevo.nombre, result.nombre, "El nombre debe coincidir") },
                { assertNotNull(result.createdAt, "La fecha de creación no debe ser nula") }
            )
        }
        
        @Test
        @DisplayName("update actualiza un producto existente")
        fun update() {
            val actualizado = Producto(
                id = 1L,
                nombre = "Laptop HP Pavilion Actualizado",
                precio = 999.99,
                stock = 20,
                categoria = Categoria.ELECTRONICA
            )
            
            val result = repository.update(1L, actualizado)
            
            assertAll(
                { assertNotNull(result, "El producto actualizado no debe ser nulo") },
                { assertEquals(actualizado.nombre, result?.nombre, "El nombre debe estar actualizado") },
                { assertEquals(actualizado.precio, result?.precio, "El precio debe estar actualizado") }
            )
        }
        
        @Test
        @DisplayName("findByNombre encuentra productos por nombre")
        fun findByNombre() {
            val result = repository.findByNombre("iPhone")
            
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
            val result = repository.findByCategoria("ELECTRONICA")
            
            assertAll(
                { assertNotNull(result, "La lista no debe ser nula") },
                { assertTrue(result.isNotEmpty(), "La lista no debe estar vacía") },
                {
                    assertTrue(
                        result.all { it.categoria == Categoria.ELECTRONICA },
                        "Todos los productos deben ser de categoría ELECTRONICA"
                    )
                }
            )
        }
        
        @Test
        @DisplayName("findAllPaginated devuelve productos paginados correctamente")
        fun findAllPaginated() {
            val result = repository.findAllPaginated(1, 5)
            
            assertAll(
                { assertNotNull(result, "La lista paginada no debe ser nula") },
                { assertEquals(5, result.size, "Debe devolver 5 productos") }
            )
        }
    }
    
    @Nested
    @DisplayName("Tests negativos de ProductosRepository")
    inner class NegativeTests {
        
        @Test
        @DisplayName("findById devuelve null para ID inexistente")
        fun findByIdNotFound() {
            val result = repository.findById(999L)
            assertNull(result, "Debe devolver null para un ID inexistente")
        }
        
        @Test
        @DisplayName("update devuelve null para ID inexistente")
        fun updateNotFound() {
            val producto = Producto(
                id = 999L,
                nombre = "No existe",
                precio = 99.99,
                stock = 10,
                categoria = Categoria.OTROS
            )
            
            val result = repository.update(999L, producto)
            assertNull(result, "Debe devolver null al actualizar un producto inexistente")
        }
        
        @Test
        @DisplayName("delete devuelve null para ID inexistente")
        fun deleteNotFound() {
            val result = repository.delete(999L)
            assertNull(result, "Debe devolver null al eliminar un producto inexistente")
        }
        
        @Test
        @DisplayName("findByNombre devuelve lista vacía para nombre inexistente")
        fun findByNombreNotFound() {
            val result = repository.findByNombre("ProductoInexistente")
            assertTrue(result.isEmpty(), "Debe devolver una lista vacía para un nombre inexistente")
        }
        
        @Test
        @DisplayName("findByCategoria devuelve lista vacía para categoría inexistente")
        fun findByCategoriaNotFound() {
            val result = repository.findByCategoria("CATEGORIA_INEXISTENTE")
            assertTrue(result.isEmpty(), "Debe devolver una lista vacía para una categoría inexistente")
        }
        
        @Test
        @DisplayName("findAllPaginated con parámetros inválidos devuelve valores por defecto")
        fun findAllPaginatedInvalidParams() {
            val result = repository.findAllPaginated(-1, -5)
            
            assertAll(
                { assertNotNull(result, "La lista no debe ser nula") },
                { assertTrue(result.size <= 10, "Debe devolver máximo 10 elementos con parámetros inválidos") }
            )
        }
    }
}
*/
