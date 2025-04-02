package dev.joseluisgs.productos.repository

import dev.joseluisgs.productos.dao.ProductosDao
import dev.joseluisgs.productos.mapper.toEntity
import dev.joseluisgs.productos.model.Categoria
import dev.joseluisgs.productos.model.Producto
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
@DisplayName("Tests del Repositorio de Productos")
class ProductosRepositoryImplMockTest {
    @MockK
    private lateinit var dao: ProductosDao
    
    @InjectMockKs
    private lateinit var productosRepository: ProductosRepositoryImpl
    
    private val producto = Producto(
        id = 1L,
        nombre = "Test",
        precio = 10.0,
        stock = 5,
        categoria = Categoria.ELECTRONICA,
    )
    
    @Nested
    @DisplayName("Tests correctos del Repositorio de Productos")
    inner class TestsCorrectos {
        
        @Test
        @DisplayName("Buscar todos los productos")
        fun findAll() {
            // Given
            every { dao.findAll() } returns listOf(producto.toEntity())
            
            // When
            val result = productosRepository.findAll()
            
            // Then
            assertAll(
                { assertEquals(1, result.size, "El tamaño de la lista debe ser 1") },
                { assertEquals(producto.id, result[0].id, "El ID debe coincidir") },
                { assertEquals(producto.nombre, result[0].nombre, "El nombre debe coincidir") }
            )
            
            verify(exactly = 1) { dao.findAll() }
        }
        
        @Test
        @DisplayName("Buscar producto por ID")
        fun findById() {
            // Given
            every { dao.findById(1L) } returns producto.toEntity()
            
            // When
            val result = productosRepository.findById(1L)
            
            // Then
            assertAll(
                { assertEquals(producto.id, result?.id, "El ID debe coincidir") },
                { assertEquals(producto.nombre, result?.nombre, "El nombre debe coincidir") }
            )
            
            verify(exactly = 1) { dao.findById(1L) }
        }
        
        @Test
        @DisplayName("Guardar producto")
        fun save() {
            // Given
            every { dao.save(any()) } returns producto.id
            
            // When
            val result = productosRepository.save(producto)
            
            // Then
            assertAll(
                { assertEquals(producto.id, result.id, "El ID debe coincidir") },
                { assertEquals(producto.nombre, result.nombre, "El nombre debe coincidir") },
                { assertEquals(producto.categoria, result.categoria, "La categoría debe coincidir") },
                { assertEquals(producto.precio, result.precio, "El precio debe coincidir") },
                { assertEquals(producto.stock, result.stock, "El stock debe coincidir") }
            )
            
            verify(exactly = 1) { dao.save(any()) }
        }
        
        @Test
        @DisplayName("Actualizar producto")
        fun update() {
            // Given
            every { dao.findById(1L) } returns producto.toEntity()
            every { dao.update(any()) } returns 1
            
            // When
            val result = productosRepository.update(1L, producto)
            
            // Then
            assertAll(
                { assertEquals(producto.id, result?.id, "El ID debe coincidir") },
                { assertEquals(producto.nombre, result?.nombre, "El nombre debe coincidir") },
                { assertEquals(producto.categoria, result?.categoria, "La categoría debe coincidir") },
                { assertEquals(producto.precio, result?.precio, "El precio debe coincidir") },
                { assertEquals(producto.stock, result?.stock, "El stock debe coincidir") }
            )
            
            verify(exactly = 1) {
                dao.findById(1L)
                dao.update(any())
            }
        }
        
        @Test
        @DisplayName("Eliminar producto")
        fun delete() {
            // Given
            every { dao.findById(1L) } returns producto.toEntity()
            every { dao.delete(1L) } returns 1
            
            // When
            val result = productosRepository.delete(1L)
            
            // Then
            assertAll(
                { assertEquals(producto.id, result?.id, "El ID debe coincidir") },
                { assertEquals(producto.nombre, result?.nombre, "El nombre debe coincidir") },
                { assertEquals(producto.categoria, result?.categoria, "La categoría debe coincidir") },
                { assertEquals(producto.precio, result?.precio, "El precio debe coincidir") },
                { assertEquals(producto.stock, result?.stock, "El stock debe coincidir") },
                { assertTrue(result?.deleted == true, "El producto debe estar marcado como eliminado") }
            )
            
            verify(exactly = 1) {
                dao.findById(1L)
                dao.delete(1L)
            }
        }
        
        @Test
        @DisplayName("Buscar productos paginados")
        fun findAllPaginated() {
            // Given
            every { dao.findAll(10, 0) } returns listOf(producto.toEntity())
            
            // When
            val result = productosRepository.findAllPaginated(1, 10)
            
            // Then
            assertAll(
                { assertEquals(1, result.size, "El tamaño de la lista debe ser 1") },
                { assertEquals(producto.id, result[0].id, "El ID debe coincidir") },
                { assertEquals(producto.nombre, result[0].nombre, "El nombre debe coincidir") },
                { assertEquals(producto.categoria, result[0].categoria, "La categoría debe coincidir") },
                { assertEquals(producto.precio, result[0].precio, "El precio debe coincidir") },
                { assertEquals(producto.stock, result[0].stock, "El stock debe coincidir") }
            )
            
            verify(exactly = 1) { dao.findAll(10, 0) }
        }
        
        @Test
        @DisplayName("Buscar productos por nombre")
        fun findByNombre() {
            // Given
            every { dao.findByName("%Test%") } returns listOf(producto.toEntity())
            
            // When
            val result = productosRepository.findByNombre("Test")
            
            // Then
            assertAll(
                { assertEquals(1, result.size, "El tamaño de la lista debe ser 1") },
                { assertEquals(producto.id, result[0].id, "El ID debe coincidir") },
                { assertEquals(producto.nombre, result[0].nombre, "El nombre debe coincidir") },
                { assertEquals(producto.categoria, result[0].categoria, "La categoría debe coincidir") },
                { assertEquals(producto.precio, result[0].precio, "El precio debe coincidir") },
                { assertEquals(producto.stock, result[0].stock, "El stock debe coincidir") }
            )
            
            verify(exactly = 1) { dao.findByName("%Test%") }
        }
        
        @Test
        @DisplayName("Buscar productos por categoría")
        fun findByCategoria() {
            // Given
            every { dao.findByCategoria("Test") } returns listOf(producto.toEntity())
            
            // When
            val result = productosRepository.findByCategoria("Test")
            
            // Then
            assertAll(
                { assertEquals(1, result.size, "El tamaño de la lista debe ser 1") },
                { assertEquals(producto.id, result[0].id, "El ID debe coincidir") },
                { assertEquals(producto.categoria, result[0].categoria, "La categoría debe coincidir") },
                { assertEquals(producto.precio, result[0].precio, "El precio debe coincidir") },
                { assertEquals(producto.stock, result[0].stock, "El stock debe coincidir") }
            )
            
            verify(exactly = 1) { dao.findByCategoria("Test") }
        }
    }
    
    @Nested
    @DisplayName("Tests incorrectos del Repositorio de Productos")
    inner class TestsIncorrectos {
        
        @Test
        @DisplayName("Buscar producto por ID inexistente")
        fun findByIdNotFound() {
            // Given
            every { dao.findById(99L) } returns null
            
            // When
            val result = productosRepository.findById(99L)
            
            // Then
            assertNull(result, "El resultado debe ser null para un ID inexistente")
            
            verify(exactly = 1) { dao.findById(99L) }
        }
        
        @Test
        @DisplayName("Actualizar producto inexistente")
        fun updateNotFound() {
            // Given
            every { dao.findById(99L) } returns null
            
            // When
            val result = productosRepository.update(99L, producto)
            
            // Then
            assertNull(result, "El resultado debe ser null al actualizar un producto inexistente")
            
            verify(exactly = 1) { dao.findById(99L) }
        }
        
        @Test
        @DisplayName("Eliminar producto inexistente")
        fun deleteNotFound() {
            // Given
            every { dao.findById(99L) } returns null
            
            // When
            val result = productosRepository.delete(99L)
            
            // Then
            assertNull(result, "El resultado debe ser null al eliminar un producto inexistente")
            
            verify(exactly = 1) { dao.findById(99L) }
        }
        
        @Test
        @DisplayName("Buscar productos paginados con página inválida")
        fun findAllPaginatedInvalidPage() {
            // Given
            every { dao.findAll(10, 0) } returns listOf(producto.toEntity())
            
            // When
            val result = productosRepository.findAllPaginated(-1, 10)
            
            // Then
            assertAll(
                { assertEquals(1, result.size, "El tamaño de la lista debe ser 1") },
                { assertEquals(producto.id, result[0].id, "El ID debe coincidir") }
            )
            
            verify(exactly = 1) { dao.findAll(10, 0) }
        }
        
        @Test
        @DisplayName("Buscar productos paginados con tamaño inválido")
        fun findAllPaginatedInvalidSize() {
            // Given
            every { dao.findAll(10, 0) } returns listOf(producto.toEntity())
            
            // When
            val result = productosRepository.findAllPaginated(1, -1)
            
            // Then
            assertAll(
                { assertEquals(1, result.size, "El tamaño de la lista debe ser 1") },
                { assertEquals(producto.id, result[0].id, "El ID debe coincidir") }
            )
            
            verify(exactly = 1) { dao.findAll(10, 0) }
        }
        
        @Test
        @DisplayName("Buscar productos por nombre inexistente")
        fun findByNombreNotFound() {
            // Given
            every { dao.findByName("%NoExiste%") } returns emptyList()
            
            // When
            val result = productosRepository.findByNombre("NoExiste")
            
            // Then
            assertTrue(result.isEmpty(), "La lista debe estar vacía")
            
            verify(exactly = 1) { dao.findByName("%NoExiste%") }
        }
        
        @Test
        @DisplayName("Buscar productos por categoría inexistente")
        fun findByCategoriaNotFound() {
            // Given
            every { dao.findByCategoria("NoExiste") } returns emptyList()
            
            // When
            val result = productosRepository.findByCategoria("NoExiste")
            
            // Then
            assertTrue(result.isEmpty(), "La lista debe estar vacía")
            
            verify(exactly = 1) { dao.findByCategoria("NoExiste") }
        }
    }
}