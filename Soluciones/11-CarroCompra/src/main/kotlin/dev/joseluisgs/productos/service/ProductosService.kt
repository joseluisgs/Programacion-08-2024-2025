package dev.joseluisgs.productos.service

import com.github.michaelbull.result.Result
import dev.joseluisgs.productos.error.ProductoError
import dev.joseluisgs.productos.model.Producto

interface ProductosService {
    fun getAll(): Result<List<Producto>, ProductoError>
    fun getById(id: Long): Result<Producto, ProductoError>
    fun create(producto: Producto): Result<Producto, ProductoError>
    fun update(id: Long, producto: Producto): Result<Producto, ProductoError>
    fun delete(id: Long): Result<Producto, ProductoError>
    fun getAllPaginated(page: Int, size: Int): Result<List<Producto>, ProductoError>
    fun getByNombre(nombre: String): Result<List<Producto>, ProductoError>
    fun getByCategoria(categoria: String): Result<List<Producto>, ProductoError>
    fun importFromFile(file: String): Result<Int, ProductoError>
    fun exportToFile(file: String): Result<Int, ProductoError>
}