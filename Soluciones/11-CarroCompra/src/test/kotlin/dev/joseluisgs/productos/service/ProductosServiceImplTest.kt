package dev.joseluisgs.productos.service

import com.github.benmanes.caffeine.cache.Cache
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import dev.joseluisgs.productos.error.ProductoError
import dev.joseluisgs.productos.model.Categoria
import dev.joseluisgs.productos.model.Producto
import dev.joseluisgs.productos.repository.ProductosRepository
import dev.joseluisgs.productos.storage.ProductosStorage
import dev.joseluisgs.productos.validator.ProductoValidator
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
@DisplayName("Tests del Servicio de Productos")
class ProductosServiceImplTest {
    @MockK
    private lateinit var repository: ProductosRepository
    
    @MockK
    private lateinit var storage: ProductosStorage
    
    @MockK
    private lateinit var cache: Cache<Long, Producto>
    
    @MockK
    private lateinit var validator: ProductoValidator
    
    @InjectMockKs
    private lateinit var service: ProductosServiceImpl
    
    private val producto = Producto(
        id = 1L,
        nombre = "Test",
        precio = 10.0,
        stock = 5,
        categoria = Categoria.OTROS
    )
    
    
    @Nested
    @DisplayName("Casos Correctos")
    inner class CasosCorrectos {
        @Test
        @DisplayName("Obtener todos los productos")
        fun getAllOk() {
            // Given
            every { repository.findAll() } returns listOf(producto)
            
            // When
            val result = service.getAll()
            
            // Then
            assertAll(
                { assertTrue(result.isOk, "El resultado debe ser Ok") },
                { assertEquals(1, result.get()?.size, "Debe devolver un producto") },
                { assertEquals(producto, result.get()?.first(), "El producto debe ser el esperado") }
            )
            
            verify(exactly = 1) { repository.findAll() }
        }
        
        @Test
        @DisplayName("Obtener producto por ID desde cache")
        fun getByIdFromCacheOk() {
            // Given
            every { cache.getIfPresent(1L) } returns producto
            
            // When
            val result = service.getById(1L)
            
            // Then
            assertAll(
                { assertTrue(result.isOk, "El resultado debe ser Ok") },
                { assertEquals(producto, result.get(), "El producto debe ser el esperado") }
            )
            
            verify(exactly = 1) { cache.getIfPresent(1L) }
            verify(exactly = 0) { repository.findById(any()) }
            verify(exactly = 0) { cache.put(any(), any()) }
        }
        
        @Test
        @DisplayName("Obtener producto por ID desde repositorio")
        fun getByIdFromRepositoryOk() {
            // Given
            every { cache.getIfPresent(1L) } returns null
            every { repository.findById(1L) } returns producto
            every { cache.put(1L, producto) } returns Unit
            
            // When
            val result = service.getById(1L)
            
            // Then
            assertAll(
                { assertTrue(result.isOk, "El resultado debe ser Ok") },
                { assertEquals(producto, result.get(), "El producto debe ser el esperado") }
            )
            
            verify(exactly = 1) {
                cache.getIfPresent(1L)
                repository.findById(1L)
                cache.put(1L, producto)
            }
        }
        
        @Test
        @DisplayName("Obtener producto por ID da error, no esta en cache ni repositorio")
        fun getByIdError() {
            // Given
            every { cache.getIfPresent(1L) } returns null
            every { repository.findById(1L) } returns null
            
            // When
            val result = service.getById(1L)
            
            // Then
            assertAll(
                { assertTrue(result.isErr, "El resultado debe ser Error") },
                {
                    assertTrue(
                        result.error is ProductoError.ProductoNoEncontrado,
                        "El error debe ser de ProductoNoEncontrado"
                    )
                },
                { assertEquals("Producto con id 1 no encontrado", result.error.message) }
            )
            
            verify(exactly = 1) {
                cache.getIfPresent(1L)
                repository.findById(1L)
            }
            verify(exactly = 0) { cache.put(any(), any()) }
        }
        
        @Test
        @DisplayName("Crear producto")
        fun createOk() {
            // Given
            every { validator.validate(producto) } returns Ok(producto)
            every { repository.save(producto) } returns producto
            
            // When
            val result = service.create(producto)
            
            // Then
            assertAll(
                { assertTrue(result.isOk, "El resultado debe ser Ok") },
                { assertEquals(producto, result.get(), "El producto debe ser el esperado") }
            )
            
            verify(exactly = 1) {
                validator.validate(producto)
                repository.save(producto)
            }
        }
        
        @Test
        @DisplayName("Actualizar producto")
        fun updateOk() {
            // Given
            every { validator.validate(producto) } returns Ok(producto)
            every { repository.update(1L, producto) } returns producto
            every { cache.invalidate(1L) } returns Unit
            
            // When
            val result = service.update(1L, producto)
            
            // Then
            assertAll(
                { assertTrue(result.isOk, "El resultado debe ser Ok") },
                { assertEquals(producto, result.get(), "El producto debe ser el esperado") }
            )
            
            verify(exactly = 1) {
                validator.validate(producto)
                repository.update(1L, producto)
                cache.invalidate(1L)
            }
        }
        
        @Test
        @DisplayName("Eliminar producto")
        fun deleteOk() {
            // Given
            every { repository.deleteLogico(1L) } returns producto
            every { cache.invalidate(1L) } returns Unit
            
            // When
            val result = service.delete(1L)
            
            // Then
            assertAll(
                { assertTrue(result.isOk, "El resultado debe ser Ok") },
                { assertEquals(producto, result.get(), "El producto debe ser el esperado") }
            )
            
            verify(exactly = 1) {
                repository.deleteLogico(1L)
                cache.invalidate(1L)
            }
        }
        
        @Test
        @DisplayName("Obtener productos paginados")
        fun getAllPaginatedOk() {
            // Given
            every { repository.findAllPaginated(0, 10) } returns listOf(producto)
            
            // When
            val result = service.getAllPaginated(0, 10)
            
            // Then
            assertAll(
                { assertTrue(result.isOk, "El resultado debe ser Ok") },
                { assertEquals(1, result.get()?.size, "Debe devolver un producto") }
            )
            
            verify(exactly = 1) { repository.findAllPaginated(0, 10) }
        }
        
        @Test
        @DisplayName("Buscar por nombre")
        fun getByNombreOk() {
            // Given
            every { repository.findByNombre("Test") } returns listOf(producto)
            
            // When
            val result = service.getByNombre("Test")
            
            // Then
            assertAll(
                { assertTrue(result.isOk, "El resultado debe ser Ok") },
                { assertEquals(1, result.get()?.size, "Debe devolver un producto") }
            )
            
            verify(exactly = 1) { repository.findByNombre("Test") }
        }
        
        @Test
        @DisplayName("Buscar por categoría")
        fun getByCategoriaOk() {
            // Given
            every { repository.findByCategoria("Test") } returns listOf(producto)
            
            // When
            val result = service.getByCategoria("Test")
            
            // Then
            assertAll(
                { assertTrue(result.isOk, "El resultado debe ser Ok") },
                { assertEquals(1, result.get()?.size, "Debe devolver un producto") }
            )
            
            verify(exactly = 1) { repository.findByCategoria("Test") }
        }
        
        @Test
        @DisplayName("Importar desde archivo")
        fun importFromFileOk() {
            // Given
            every { storage.loadFromFile("test.json") } returns Ok(listOf(producto))
            every { validator.validate(producto) } returns Ok(producto)
            every { repository.save(producto) } returns producto
            
            // When
            val result = service.importFromFile("test.json")
            
            // Then
            assertAll(
                { assertTrue(result.isOk, "El resultado debe ser Ok") },
                { assertEquals(1, result.get(), "Debe importar un producto") }
            )
            
            verify(atLeast = 1) { storage.loadFromFile("test.json") }
            verify(exactly = 1) { validator.validate(producto) }
            verify(exactly = 1) { repository.save(producto) }
        }
        
        @Test
        @DisplayName("Exportar a archivo")
        fun exportToFileOk() {
            // Given
            every { repository.findAll() } returns listOf(producto)
            every { storage.saveToFile(any(), "test.json") } returns Ok(listOf(producto))
            
            // When
            val result = service.exportToFile("test.json")
            
            // Then
            assertAll(
                { assertTrue(result.isOk, "El resultado debe ser Ok") },
                { assertEquals(1, result.get(), "Debe exportar un producto") }
            )
            
            verify(exactly = 1) {
                repository.findAll()
                storage.saveToFile(any(), "test.json")
            }
        }
    }
    
    @Nested
    @DisplayName("Casos Incorrectos")
    inner class CasosIncorrectos {
        @Test
        @DisplayName("Obtener producto por ID inexistente")
        fun getByIdNotFound() {
            // Given
            every { cache.getIfPresent(1L) } returns null
            every { repository.findById(1L) } returns null
            
            // When
            val result = service.getById(1L)
            
            // Then
            assertAll(
                { assertTrue(result.isErr, "El resultado debe ser Error") },
                {
                    assertTrue(
                        result.error is ProductoError.ProductoNoEncontrado,
                        "El error debe ser de ProductoNoEncontrado"
                    )
                },
                { assertEquals("Producto con id 1 no encontrado", result.error.message) }
            )
            
            verify(exactly = 1) {
                cache.getIfPresent(1L)
                repository.findById(1L)
            }
        }
        
        @Test
        @DisplayName("Crear producto con validación incorrecta")
        fun createWithInvalidData() {
            // Given
            every { validator.validate(producto) } returns Err(ProductoError.ProductoNoValido("Error de validación"))
            
            // When
            val result = service.create(producto)
            
            // Then
            assertAll(
                { assertTrue(result.isErr, "El resultado debe ser Error") },
                { assertTrue(result.error is ProductoError.ProductoNoValido, "El error debe ser de ProductoNoValido") },
                { assertEquals("Producto no válido. Error de validación", result.error.message) }
            )
            
            verify(exactly = 1) { validator.validate(producto) }
            verify(exactly = 0) { repository.save(any()) }
        }
        
        @Test
        @DisplayName("Actualizar producto inexistente")
        fun updateNotFound() {
            // Given
            every { validator.validate(producto) } returns Ok(producto)
            every { repository.update(1L, producto) } returns null
            
            // When
            val result = service.update(1L, producto)
            
            // Then
            assertAll(
                { assertTrue(result.isErr, "El resultado debe ser Error") },
                {
                    assertTrue(
                        result.error is ProductoError.ProductoNoEncontrado,
                        "El error debe ser de ProductoNoEncontrado"
                    )
                },
                {
                    assertEquals(
                        "Producto con id 1 no encontrado",
                        result.error.message
                    )
                }
            )
            
            verify(exactly = 1) {
                validator.validate(producto)
                repository.update(1L, producto)
            }
        }
        
        @Test
        @DisplayName("Eliminar producto inexistente")
        fun deleteNotFound() {
            // Given
            every { repository.deleteLogico(1L) } returns null
            
            // When
            val result = service.delete(1L)
            
            // Then
            assertAll(
                { assertTrue(result.isErr, "El resultado debe ser Error") },
                {
                    assertTrue(
                        result.error is ProductoError.ProductoNoEncontrado,
                        "El error debe ser de ProductoNoEncontrado"
                    )
                },
                {
                    assertEquals(
                        "Producto con id 1 no encontrado",
                        result.error.message
                    )
                }
            )
            
            verify(exactly = 1) { repository.deleteLogico(1L) }
        }
        
        @Test
        @DisplayName("Error al importar desde archivo")
        fun importFromFileError() {
            // Given
            every { storage.loadFromFile("test.json") } returns Err(ProductoError.ProductoStorageError("Error al leer archivo"))
            
            // When
            val result = service.importFromFile("test.json")
            
            // Then
            assertAll(
                { assertTrue(result.isErr, "El resultado debe ser Error") },
                {
                    assertTrue(
                        result.error is ProductoError.ProductoStorageError,
                        "El error debe ser de ProductoStorageError"
                    )
                },
                { assertEquals("Error en almacenamiento de productos. Error al leer archivo", result.error.message) }
            )
            
            verify(exactly = 1) { storage.loadFromFile("test.json") }
            verify(exactly = 0) { repository.save(any()) }
        }
        
        @Test
        @DisplayName("Error al exportar a archivo")
        fun exportToFileError() {
            // Given
            every { repository.findAll() } returns listOf(producto)
            every {
                storage.saveToFile(
                    any(),
                    "test.json"
                )
            } returns Err(ProductoError.ProductoStorageError("Error al escribir archivo"))
            
            // When
            val result = service.exportToFile("test.json")
            
            // Then
            assertAll(
                { assertTrue(result.isErr, "El resultado debe ser Error") },
                {
                    assertTrue(
                        result.error is ProductoError.ProductoStorageError,
                        "El error debe ser de ProductoStorageError"
                    )
                },
                {
                    assertEquals(
                        "Error en almacenamiento de productos. Error al escribir archivo",
                        result.error.message
                    )
                }
            )
            
            verify(exactly = 1) {
                repository.findAll()
                storage.saveToFile(any(), "test.json")
            }
        }
    }
}
