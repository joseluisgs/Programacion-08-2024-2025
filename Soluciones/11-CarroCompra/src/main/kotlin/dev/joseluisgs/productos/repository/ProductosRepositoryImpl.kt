package dev.joseluisgs.productos.repository

import dev.joseluisgs.productos.dao.ProductosDao
import dev.joseluisgs.productos.mapper.toEntity
import dev.joseluisgs.productos.mapper.toModel
import dev.joseluisgs.productos.model.Producto
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging
import java.time.LocalDateTime

@Singleton
class ProductosRepositoryImpl(
    private val dao: ProductosDao
) : ProductosRepository {
    private val logger = logging()
    
    override fun findAll(): List<Producto> {
        logger.debug { "Buscando todos los productos" }
        return dao.findAll().map { it.toModel() }
    }
    
    override fun findById(id: Long): Producto? {
        logger.debug { "Buscando producto por id: $id" }
        return dao.findById(id)?.toModel()
    }
    
    override fun save(t: Producto): Producto {
        logger.debug { "Guardando producto: $t" }
        val timestamp = LocalDateTime.now()
        val toSave = t.copy(createdAt = timestamp, updatedAt = timestamp)
        val id = dao.save(toSave.toEntity())
        return toSave.copy(id = id)
    }
    
    override fun update(id: Long, t: Producto): Producto? {
        logger.debug { "Actualizando producto por id: $id, nuevo valor: $t" }
        val timestamp = LocalDateTime.now()
        findById(id) ?: return null // Si no existe no actualizamos
        val toUpdate = t.copy(id = id, updatedAt = timestamp)
        dao.update(toUpdate.toEntity())
        return toUpdate
    }
    
    override fun delete(id: Long): Producto? {
        logger.debug { "Eliminando producto por id: $id" }
        val toFind = findById(id) ?: return null // Si no existe no eliminamos
        dao.delete(id)
        return toFind.copy(deleted = true)
    }
    
    override fun deleteLogico(id: Long): Producto? {
        logger.debug { "Eliminando lógicamente producto por id: $id" }
        val toFind = findById(id) ?: return null // Si no existe no eliminamos
        val timestamp = LocalDateTime.now()
        findById(id) ?: return null // Si no existe no actualizamos
        val toUpdate = toFind.copy(id = id, updatedAt = timestamp, deleted = true)
        dao.update(toUpdate.toEntity())
        return toUpdate
    }
    
    override fun findAllPaginated(page: Int, size: Int): List<Producto> {
        logger.debug { "Buscando todos los productos paginados, página: $page, tamaño: $size" }
        val myPage = if (page < 1) 0 else page - 1 // Cuidado con la paginación y los índices para test
        val mySize = if (size < 1) 10 else size
        
        return dao.findAll(mySize, myPage * mySize).map { it.toModel() }
    }
    
    override fun findByNombre(nombre: String): List<Producto> {
        logger.debug { "Buscando productos por nombre: $nombre" }
        return dao.findByName("%$nombre%").map { it.toModel() }
    }
    
    override fun findByCategoria(categoria: String): List<Producto> {
        logger.debug { "Buscando productos por categoría: $categoria" }
        return dao.findByCategoria(categoria).map { it.toModel() }
    }
}