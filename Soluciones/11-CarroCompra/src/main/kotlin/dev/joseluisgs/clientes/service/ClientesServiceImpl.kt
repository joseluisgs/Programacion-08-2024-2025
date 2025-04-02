package dev.joseluisgs.clientes.service

import com.github.benmanes.caffeine.cache.Cache
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import dev.joseluisgs.clientes.error.ClienteError
import dev.joseluisgs.clientes.model.Cliente
import dev.joseluisgs.clientes.repository.ClientesRepository
import dev.joseluisgs.clientes.validator.ClienteValidator
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging


@Singleton
class ClientesServiceImpl(
    private val repository: ClientesRepository,
    @Named("CacheClientes")
    private val cache: Cache<Long, Cliente>,
    private val validator: ClienteValidator

) : ClientesService {
    private val logger = logging()
    override fun getAll(): Result<List<Cliente>, ClienteError> {
        logger.debug { "Obteniendo todos los clientes" }
        return try {
            Ok(repository.findAll())
        } catch (e: Exception) {
            logger.error { "Error al obtener los clientes" }
            Err(ClienteError.ClienteOtherError("Error al obtener los clientes ${e.message}"))
        }
    }
    
    
    override fun getById(id: Long): Result<Cliente, ClienteError> {
        logger.debug { "Obteniendo cliente por id $id" }
        return try {
            cache.getIfPresent(id)?.let {
                Ok(it)
            } ?: repository.findById(id)?.let {
                cache.put(id, it) // Guardamos en cache
                Ok(it)
            } ?: Err(ClienteError.ClienteNoEncontrado(id))
        } catch (e: Exception) {
            logger.error { "Error al obtener el cliente por id $id" }
            Err(ClienteError.ClienteOtherError("Error al obtener el cliente por id $id ${e.message}"))
        }
    }
    
    override fun create(cliente: Cliente): Result<Cliente, ClienteError> {
        logger.debug { "Creando cliente $cliente" }
        return try {
            validator.validate(cliente).andThen {
                Ok(repository.save(it))
            }
        } catch (e: Exception) {
            logger.error { "Error al crear el cliente" }
            Err(ClienteError.ClienteOtherError("Error al crear el cliente ${e.message}"))
        }
    }
    
    override fun update(id: Long, cliente: Cliente): Result<Cliente, ClienteError> {
        logger.debug { "Actualizando cliente $id con los datos $cliente" }
        return try {
            validator.validate(cliente).andThen {
                repository.update(id, it)?.let { cli ->
                    cache.put(id, cli) // Actualizamos en cache // O borrar según el caso de preferencia
                    Ok(cli)
                } ?: Err(ClienteError.ClienteNoEncontrado(id))
            }
        } catch (e: Exception) {
            logger.error { "Error al actualizar el cliente por id $id" }
            Err(ClienteError.ClienteOtherError("Error al actualizar el producto por id $id ${e.message}"))
        }
    }
    
    override fun delete(id: Long): Result<Cliente, ClienteError> {
        logger.debug { "Eliminando cliente por id $id" }
        return try {
            // Implementamos un delete logico, porque el cliente no se debe borrar para mantener la integridad e histórico
            repository.deleteLogico(id)?.let {
                cache.invalidate(id) // Borramos de cache
                Ok(it)
            } ?: Err(ClienteError.ClienteNoEncontrado(id))
        } catch (e: Exception) {
            logger.error { "Error al eliminar el producto por id $id" }
            Err(ClienteError.ClienteOtherError("Error al eliminar el producto por id $id ${e.message}"))
        }
    }
}