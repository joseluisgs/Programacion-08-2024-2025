package dev.joseluisgs.productos.repository

import dev.joseluisgs.common.repository.CrudRepository
import dev.joseluisgs.productos.model.Producto

interface ProductosRepository : CrudRepository<Producto, Long> {
    fun deleteLogico(id: Long): Producto?
    fun findAllPaginated(page: Int, size: Int): List<Producto>
    fun findByNombre(nombre: String): List<Producto>
    fun findByCategoria(categoria: String): List<Producto>
}