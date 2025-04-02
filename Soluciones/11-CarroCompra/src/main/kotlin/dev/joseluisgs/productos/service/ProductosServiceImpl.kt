package dev.joseluisgs.productos.service

import com.github.benmanes.caffeine.cache.Cache
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import dev.joseluisgs.productos.error.ProductoError
import dev.joseluisgs.productos.model.Producto
import dev.joseluisgs.productos.repository.ProductosRepository
import dev.joseluisgs.productos.storage.ProductosStorage
import dev.joseluisgs.productos.validator.ProductoValidator
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging

@Singleton
class ProductosServiceImpl(
    private val repository: ProductosRepository,
    private val storage: ProductosStorage,
    @Named("CacheProductos")
    private val cache: Cache<Long, Producto>,
    private val validator: ProductoValidator
) : ProductosService {
    private val logger = logging()
    override fun getAll(): Result<List<Producto>, ProductoError> {
        logger.debug { "Obteniendo todos los productos" }
        return try {
            Ok(repository.findAll())
        } catch (e: Exception) {
            logger.error { "Error al obtener los productos" }
            Err(ProductoError.ProductoOtherError("Error al obtener los productos ${e.message}"))
        }
        
    }
    
    override fun getById(id: Long): Result<Producto, ProductoError> {
        logger.debug { "Obteniendo producto por id $id" }
        return try {
            cache.getIfPresent(id)?.let {
                Ok(it)
            } ?: repository.findById(id)?.let {
                cache.put(id, it) // Guardamos en cache
                Ok(it)
            } ?: Err(ProductoError.ProductoNoEncontrado(id))
        } catch (e: Exception) {
            logger.error { "Error al obtener el producto por id $id" }
            Err(ProductoError.ProductoOtherError("Error al obtener el producto por id $id ${e.message}"))
        }
    }
    
    override fun create(producto: Producto): Result<Producto, ProductoError> {
        logger.debug { "Creando producto: $producto" }
        return try {
            validator.validate(producto).andThen {
                Ok(repository.save(it))
            }
        } catch (e: Exception) {
            logger.error { "Error al crear el producto" }
            Err(ProductoError.ProductoOtherError("Error al crear el producto ${e.message}"))
        }
    }
    
    override fun update(id: Long, producto: Producto): Result<Producto, ProductoError> {
        logger.debug { "Actualizando producto por id $id: $producto" }
        return try {
            validator.validate(producto).andThen {
                repository.update(id, it)?.let { prod ->
                    cache.invalidate(id) // Borramos de cache
                    Ok(prod)
                } ?: Err(ProductoError.ProductoNoEncontrado(id))
            }
        } catch (e: Exception) {
            logger.error { "Error al actualizar el producto por id $id" }
            Err(ProductoError.ProductoOtherError("Error al actualizar el producto por id $id ${e.message}"))
        }
    }
    
    override fun delete(id: Long): Result<Producto, ProductoError> {
        logger.debug { "Eliminando producto por id $id" }
        return try {
            // Implementamos un delete logico, porque el producto no se debe borrar para mantener la integridad e histórico
            repository.deleteLogico(id)?.let {
                cache.invalidate(id) // Borramos de cache
                Ok(it)
            } ?: Err(ProductoError.ProductoNoEncontrado(id))
        } catch (e: Exception) {
            logger.error { "Error al eliminar el producto por id $id" }
            Err(ProductoError.ProductoOtherError("Error al eliminar el producto por id $id ${e.message}"))
        }
    }
    
    override fun getAllPaginated(page: Int, size: Int): Result<List<Producto>, ProductoError> {
        logger.debug { "Obteniendo todos los productos paginados" }
        return try {
            Ok(repository.findAllPaginated(page, size))
        } catch (e: Exception) {
            logger.error { "Error al obtener los productos paginados" }
            Err(ProductoError.ProductoOtherError("Error al obtener los productos paginados ${e.message}"))
        }
    }
    
    override fun getByNombre(nombre: String): Result<List<Producto>, ProductoError> {
        logger.debug { "Obteniendo productos por nombre $nombre" }
        return try {
            Ok(repository.findByNombre(nombre))
        } catch (e: Exception) {
            logger.error { "Error al obtener los productos por nombre $nombre" }
            Err(ProductoError.ProductoOtherError("Error al obtener los productos por nombre $nombre ${e.message}"))
        }
    }
    
    override fun getByCategoria(categoria: String): Result<List<Producto>, ProductoError> {
        logger.debug { "Obteniendo productos por categoría $categoria" }
        return try {
            Ok(repository.findByCategoria(categoria))
        } catch (e: Exception) {
            logger.error { "Error al obtener los productos por categoría $categoria" }
            Err(ProductoError.ProductoOtherError("Error al obtener los productos por categoría $categoria ${e.message}"))
        }
    }
    
    override fun importFromFile(file: String): Result<Int, ProductoError> {
        logger.debug { "Importando productos desde fichero: $file" }
        return try {
            storage.loadFromFile(file).andThen { productos ->
                productos.forEach { producto ->
                    // Validamos el producto antes de guardarlo
                    validator.validate(producto).andThen { p ->
                        Ok(repository.save(p))
                    }
                }
                Ok(productos.size)
            }
        } catch (e: Exception) {
            logger.error { "Error al importar productos desde fichero" }
            Err(ProductoError.ProductoOtherError("Error al importar productos desde fichero ${e.message}"))
        }
    }
    
    override fun exportToFile(file: String): Result<Int, ProductoError> {
        logger.debug { "Exportando productos a fichero: $file" }
        return try {
            storage.saveToFile(repository.findAll(), file).andThen {
                Ok(it.size)
            }
        } catch (e: Exception) {
            logger.error { "Error al exportar productos a fichero" }
            Err(ProductoError.ProductoOtherError("Error al exportar productos a fichero ${e.message}"))
        }
    }
}