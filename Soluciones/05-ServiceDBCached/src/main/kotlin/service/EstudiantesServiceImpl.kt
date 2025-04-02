package dev.joseluisgs.service

import dev.joseluisgs.cache.Cache
import dev.joseluisgs.cache.CacheImpl
import dev.joseluisgs.exception.EstudiantesException
import dev.joseluisgs.models.Estudiante
import dev.joseluisgs.repositories.EstudiantesRepository
import dev.joseluisgs.repositories.EstudiantesRepositoryImpl
import dev.joseluisgs.validator.EstudianteValidator
import dev.joseluisgs.validator.Validator
import org.lighthousegames.logging.logging

class EstudiantesServiceImpl(
    private val repository: EstudiantesRepository = EstudiantesRepositoryImpl(),
    private val cache: Cache<Int, Estudiante> = CacheImpl(5, CacheImpl.CacheType.LRU),
    private val validator: Validator<Estudiante> = EstudianteValidator()
) : EstudiantesService {
    private val logger = logging()
    
    override fun findAll(): List<Estudiante> {
        logger.debug { "Obteniendo todos los estudiantes" }
        return repository.findAll()
    }
    
    override fun findById(id: Int): Estudiante {
        logger.debug { "Obteniendo estudiante con id $id" }
        return cache.get(id) ?: repository.findById(id)?.let {
            cache.put(id, it) // Guardamos en cache
            it
        } ?: throw EstudiantesException.NotFoundException(id)
    }
    
    override fun save(estudiante: Estudiante): Estudiante {
        logger.debug { "Guardando estudiante $estudiante" }
        validator.validate(estudiante) // Validamos
        return repository.save(estudiante)
    }
    
    override fun update(id: Int, estudiante: Estudiante): Estudiante {
        logger.debug { "Actualizando estudiante con id $id" }
        validator.validate(estudiante) // Validamos
        return repository.update(id, estudiante)?.let {
            cache.remove(id) // Borramos de cache, ya que se ha actualizado
            it
        } ?: throw EstudiantesException.NotFoundException(id)
    }
    
    override fun delete(id: Int): Estudiante {
        logger.debug { "Borrando estudiante con id $id" }
        return repository.delete(id)?.let {
            cache.remove(id) // Borramos de cache
            it
        } ?: throw EstudiantesException.NotFoundException(id)
    }
}