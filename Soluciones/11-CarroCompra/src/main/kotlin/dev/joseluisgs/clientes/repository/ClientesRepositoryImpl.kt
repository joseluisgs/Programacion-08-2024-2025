package dev.joseluisgs.clientes.repository

import dev.joseluisgs.clientes.dao.ClientesDao
import dev.joseluisgs.clientes.mapper.toEntity
import dev.joseluisgs.clientes.mapper.toModel
import dev.joseluisgs.clientes.model.Cliente
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging
import java.time.LocalDateTime

@Singleton
class ClientesRepositoryImpl(
    private val dao: ClientesDao
) : ClientesRepository {
    private val logger = logging()
    override fun findAll(): List<Cliente> {
        logger.debug { "Buscando todos los clientes" }
        return dao.findAll().map { it.toModel() }
    }
    
    override fun findById(id: Long): Cliente? {
        logger.debug { "Buscando cliente por ID: $id" }
        return dao.findById(id)?.toModel()
    }
    
    override fun save(t: Cliente): Cliente {
        logger.debug { "Guardando cliente: $t" }
        val timestamp = LocalDateTime.now()
        val toSave = t.copy(createdAt = timestamp, updatedAt = timestamp)
        val id = dao.save(toSave.toEntity())
        return toSave.copy(id = id)
    }
    
    override fun update(id: Long, t: Cliente): Cliente? {
        logger.debug { "Actualizando cliente por id: $id, nuevo valor: $t" }
        val timestamp = LocalDateTime.now()
        findById(id) ?: return null // Si no existe no actualizamos
        val toUpdate = t.copy(id = id, updatedAt = timestamp)
        dao.update(toUpdate.toEntity())
        return toUpdate
    }
    
    override fun delete(id: Long): Cliente? {
        logger.debug { "Eliminando cliente por id: $id" }
        val toFind = findById(id) ?: return null // Si no existe no eliminamos
        dao.delete(id)
        return toFind.copy(deleted = true)
    }
    
    override fun deleteLogico(id: Long): Cliente? {
        logger.debug { "Eliminando lógicamente cliente por id: $id" }
        val toFind = findById(id) ?: return null // Si no existe no eliminamos
        val timestamp = LocalDateTime.now()
        findById(id) ?: return null // Si no existe no actualizamos
        val toUpdate = toFind.copy(id = id, updatedAt = timestamp, deleted = true)
        dao.update(toUpdate.toEntity())
        return toUpdate
    }
    
}
