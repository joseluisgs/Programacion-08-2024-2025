package dev.joseluisgs.productos.mapper

import dev.joseluisgs.productos.dao.ProductoEntity
import dev.joseluisgs.productos.dto.ProductoDto
import dev.joseluisgs.productos.model.Categoria
import dev.joseluisgs.productos.model.Producto
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import java.time.LocalDateTime
import kotlin.test.Test

@DisplayName("Tests del Mapeador de Productos")
class ProductoMapperTest {
    private val now = LocalDateTime.now()
    
    private val producto = Producto(
        id = 1L,
        nombre = "Test",
        precio = 9.99,
        stock = 10,
        categoria = Categoria.ELECTRONICA,
        createdAt = now,
        updatedAt = now,
        deleted = false
    )
    
    private val productoEntity = ProductoEntity(
        id = 1L,
        nombre = "Test",
        precio = 9.99,
        stock = 10,
        categoria = "ELECTRONICA",
        createdAt = now,
        updatedAt = now,
        deleted = false
    )
    
    private val productoDto = ProductoDto(
        id = 1L,
        nombre = "Test",
        precio = 9.99,
        stock = 10,
        categoria = "ELECTRONICA",
        createdAt = now.toString(),
        updatedAt = now.toString(),
        deleted = false
    )
    
    @Test
    @DisplayName("ProductoEntity to Producto conversion")
    fun productoEntityToProducto() {
        val result = productoEntity.toModel()
        
        assertAll(
            { assertEquals(productoEntity.id, result.id, "Los IDs deben coincidir") },
            { assertEquals(productoEntity.nombre, result.nombre, "Los nombres deben coincidir") },
            { assertEquals(productoEntity.precio, result.precio, "Los precios deben coincidir") },
            { assertEquals(productoEntity.stock, result.stock, "El stock debe coincidir") },
            {
                assertEquals(
                    Categoria.valueOf(productoEntity.categoria),
                    result.categoria,
                    "Las categorías deben coincidir"
                )
            },
            { assertEquals(productoEntity.createdAt, result.createdAt, "Las fechas de creación deben coincidir") },
            { assertEquals(productoEntity.updatedAt, result.updatedAt, "Las fechas de actualización deben coincidir") },
            { assertEquals(productoEntity.deleted, result.deleted, "El estado de borrado debe coincidir") }
        )
    }
    
    @Test
    @DisplayName("ProductoDto to Producto conversion")
    fun productoDtoToProducto() {
        val result = productoDto.toModel()
        
        assertAll(
            { assertEquals(productoDto.id, result.id, "Los IDs deben coincidir") },
            { assertEquals(productoDto.nombre, result.nombre, "Los nombres deben coincidir") },
            { assertEquals(productoDto.precio, result.precio, "Los precios deben coincidir") },
            { assertEquals(productoDto.stock, result.stock, "El stock debe coincidir") },
            {
                assertEquals(
                    Categoria.valueOf(productoDto.categoria),
                    result.categoria,
                    "Las categorías deben coincidir"
                )
            },
            {
                assertEquals(
                    LocalDateTime.parse(productoDto.createdAt),
                    result.createdAt,
                    "Las fechas de creación deben coincidir"
                )
            },
            {
                assertEquals(
                    LocalDateTime.parse(productoDto.updatedAt),
                    result.updatedAt,
                    "Las fechas de actualización deben coincidir"
                )
            },
            { assertEquals(productoDto.deleted, result.deleted, "El estado de borrado debe coincidir") }
        )
    }
    
    @Test
    @DisplayName("Producto to ProductoDto conversion")
    fun productoToProductoDto() {
        val result = producto.toDto()
        
        assertAll(
            { assertEquals(producto.id, result.id, "Los IDs deben coincidir") },
            { assertEquals(producto.nombre, result.nombre, "Los nombres deben coincidir") },
            { assertEquals(producto.precio, result.precio, "Los precios deben coincidir") },
            { assertEquals(producto.stock, result.stock, "El stock debe coincidir") },
            { assertEquals(producto.categoria.name, result.categoria, "Las categorías deben coincidir") },
            { assertEquals(producto.createdAt.toString(), result.createdAt, "Las fechas de creación deben coincidir") },
            {
                assertEquals(
                    producto.updatedAt.toString(),
                    result.updatedAt,
                    "Las fechas de actualización deben coincidir"
                )
            },
            { assertEquals(producto.deleted, result.deleted, "El estado de borrado debe coincidir") }
        )
    }
    
    @Test
    @DisplayName("Producto to ProductoEntity conversion")
    fun productoToProductoEntity() {
        val result = producto.toEntity()
        
        assertAll(
            { assertEquals(producto.id, result.id, "Los IDs deben coincidir") },
            { assertEquals(producto.nombre, result.nombre, "Los nombres deben coincidir") },
            { assertEquals(producto.precio, result.precio, "Los precios deben coincidir") },
            { assertEquals(producto.stock, result.stock, "El stock debe coincidir") },
            { assertEquals(producto.categoria.name, result.categoria, "Las categorías deben coincidir") },
            { assertEquals(producto.createdAt, result.createdAt, "Las fechas de creación deben coincidir") },
            { assertEquals(producto.updatedAt, result.updatedAt, "Las fechas de actualización deben coincidir") },
            { assertEquals(producto.deleted, result.deleted, "El estado de borrado debe coincidir") }
        )
    }
}