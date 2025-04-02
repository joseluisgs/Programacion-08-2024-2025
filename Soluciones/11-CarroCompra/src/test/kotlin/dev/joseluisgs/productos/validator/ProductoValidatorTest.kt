package dev.joseluisgs.productos.validator

import com.github.michaelbull.result.get
import dev.joseluisgs.productos.error.ProductoError
import dev.joseluisgs.productos.model.Categoria
import dev.joseluisgs.productos.model.Producto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@DisplayName("Tests del Validador de Productos")
class ProductoValidatorTest {
    private val validator = ProductoValidator()
    private val fechaActual = LocalDateTime.now()
    
    @Nested // Agrupamos los tests
    @DisplayName("Tests de productos válidos")
    inner class ProductosValidos {
        @Test
        @DisplayName("Producto válido debe pasar la validación")
        fun productoValidoTest() {
            val producto = Producto(
                nombre = "iPhone 13",
                precio = 999.99,
                stock = 10,
                categoria = Categoria.ELECTRONICA,
                createdAt = fechaActual,
                updatedAt = fechaActual
            )
            
            val resultado = validator.validate(producto)
            
            assertAll(
                { assertTrue(resultado.isOk, "Debe coincidir el tipo") },
                { assertEquals(producto, resultado.get(), "No coincide el producto") }
            )
        }
    }
    
    @Nested // Agrupamos los tests
    @DisplayName("Tests de productos inválidos")
    inner class ProductosInvalidos {
        @Test
        @DisplayName("Producto con nombre vacío debe ser inválido")
        fun nombreVacioTest() {
            val producto = Producto(
                nombre = "",
                precio = 999.99,
                stock = 10,
                categoria = Categoria.ELECTRONICA,
                createdAt = fechaActual,
                updatedAt = fechaActual
            )
            
            val resultado = validator.validate(producto)
            
            assertAll(
                { assertTrue(resultado.isErr, "No coincide el tipo") },
                { assertTrue(resultado.error is ProductoError.ProductoNoValido, "El error no es de validación") },
                {
                    assertEquals(
                        "Producto no válido. Nombre no puede estar vacío",
                        resultado.error.message,
                        "El mensaje no coincide"
                    )
                }
            )
        }
        
        @Test
        @DisplayName("Producto con precio negativo debe ser inválido")
        fun precioNegativoTest() {
            val producto = Producto(
                nombre = "iPhone 13",
                precio = -100.0,
                stock = 10,
                categoria = Categoria.ELECTRONICA,
                createdAt = fechaActual,
                updatedAt = fechaActual
            )
            
            val resultado = validator.validate(producto)
            
            assertAll(
                { assertTrue(resultado.isErr, "No coincide el tipo") },
                { assertTrue(resultado.error is ProductoError.ProductoNoValido, "El error no es de validación") },
                {
                    assertEquals(
                        "Producto no válido. Precio no puede ser menor a 0",
                        resultado.error.message,
                        "El mensaje no coincide"
                    )
                }
            )
        }
        
        @Test
        @DisplayName("Producto con stock negativo debe ser inválido")
        fun stockNegativoTest() {
            val producto = Producto(
                nombre = "iPhone 13",
                precio = 999.99,
                stock = -5,
                categoria = Categoria.ELECTRONICA,
                createdAt = fechaActual,
                updatedAt = fechaActual
            )
            
            val resultado = validator.validate(producto)
            
            assertAll(
                { assertTrue(resultado.isErr, "No coincide el tipo") },
                { assertTrue(resultado.error is ProductoError.ProductoNoValido, "El error no es de validación") },
                {
                    assertEquals(
                        "Producto no válido. Stock no puede ser menor a 0",
                        resultado.error.message,
                        "El mensaje no coincide"
                    )
                }
            )
        }
        
        @Test
        @DisplayName("Producto con nombre en blanco debe ser inválido")
        fun nombreEnBlancoTest() {
            val producto = Producto(
                nombre = "   ",
                precio = 999.99,
                stock = 10,
                categoria = Categoria.ELECTRONICA,
                createdAt = fechaActual,
                updatedAt = fechaActual
            )
            
            val resultado = validator.validate(producto)
            
            assertAll(
                { assertTrue(resultado.isErr, "No coincide el tipo") },
                { assertTrue(resultado.error is ProductoError.ProductoNoValido, "El error no es de validación") },
                {
                    assertEquals(
                        "Producto no válido. Nombre no puede estar vacío",
                        resultado.error.message,
                        "El mensaje no coincide"
                    )
                }
            )
        }
    }
}