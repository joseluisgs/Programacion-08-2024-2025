package dev.joseluisgs.productos.storage

import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import dev.joseluisgs.productos.model.Categoria
import dev.joseluisgs.productos.model.Producto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

@DisplayName("Tests del Storage de Productos")
class ProductosStorageImplTest {
    private val storage = ProductosStorageImpl()
    
    @Nested
    @DisplayName("Tests de carga de productos desde fichero")
    inner class LoadFromFileTest {
        @Test
        @DisplayName("Carga correctamente los productos desde un fichero CSV")
        fun loadFromFileTest(@TempDir tempDir: File) {
            // Arrange
            val testFile = File(tempDir, "productos_test.csv")
            val csvContent = """
            id,nombre,precio,stock,categoria
            1,Producto 1,10.99,100,ELECTRONICA
            2,Producto 2,20.50,50,ELECTRONICA
        """.trimIndent()
            testFile.writeText(csvContent)
            
            // Act
            val result = storage.loadFromFile(testFile.absolutePath)
            
            // Assert
            assertNotNull(result.get(), "El resultado no debería ser null")
            val productos = result.get()!!
            assertAll(
                { assertEquals(2, productos.size, "La lista debería contener 2 productos") },
                { assertEquals("Producto 1", productos[0].nombre, "El nombre del primer producto no coincide") },
                { assertEquals(10.99, productos[0].precio, "El precio del primer producto no coincide") },
                { assertEquals(100, productos[0].stock, "El stock del primer producto no coincide") },
                {
                    assertEquals(
                        "ELECTRONICA",
                        productos[0].categoria.name,
                        "La categoría del primer producto no coincide"
                    )
                }
            )
        }
        
        @Test
        @DisplayName("Falla al cargar productos desde un fichero que no existe")
        fun loadFromNonExistentFileTest(@TempDir tempDir: File) {
            // Arrange
            val nonExistentFile = File(tempDir, "no_existe.csv")
            
            // Act
            val result = storage.loadFromFile(nonExistentFile.absolutePath)
            
            // Assert
            assertNotNull(result.getError(), "Debería haber un error cuando el fichero no existe")
            assertTrue(
                result.getError()!!.message.contains("El fichero no existe"),
                "El mensaje de error debería indicar que el fichero no existe"
            )
        }
    }
    
    @Nested
    @DisplayName("Tests de guardado de productos en fichero")
    inner class SaveToFileTest {
        
        
        @Test
        @DisplayName("Guarda correctamente los productos en un fichero JSON")
        fun saveToFileTest(@TempDir tempDir: File) {
            // Arrange
            val testFile = File(tempDir, "productos_test.json")
            val productos = listOf(
                Producto(
                    nombre = "Producto Test",
                    precio = 15.99,
                    stock = 75,
                    categoria = Categoria.DEPORTE
                )
            )
            
            // Act
            val result = storage.saveToFile(productos, testFile.absolutePath)
            
            val readFile = File(testFile.absolutePath).readLines()
            
            // Assert
            assertAll(
                { assertTrue(testFile.exists(), "El fichero debería existir después de guardar") },
                { assertTrue(testFile.length() > 0, "El fichero no debería estar vacío") },
                { assertNotNull(result.get(), "El resultado no debería ser null") },
                {
                    assertEquals(
                        productos,
                        result.get(),
                        "Los productos guardados deberían coincidir con los originales"
                    )
                }
            )
        }
        
        @Test
        @DisplayName("Falla al guardar en un directorio que no existe")
        fun saveToInvalidDirectoryTest() {
            // Arrange
            val invalidPath = "/ruta/invalida/productos.json"
            val productos = listOf(
                Producto(
                    nombre = "Producto Test",
                    precio = 15.99,
                    stock = 75,
                    categoria = Categoria.ELECTRONICA
                )
            )
            
            // Act
            val result = storage.saveToFile(productos, invalidPath)
            
            // Assert
            assertNotNull(result.getError(), "Debería haber un error cuando la ruta es inválida")
            assertTrue(
                result.getError()!!.message.contains("No se puede crear el directorio padre"),
                "El mensaje de error debería indicar que no se puede crear el directorio"
            )
        }
    }
}