package dev.joseluisgs.service

import com.github.benmanes.caffeine.cache.Cache
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import dev.joseluisgs.error.EstudiantesError
import dev.joseluisgs.models.Estudiante
import dev.joseluisgs.repositories.EstudiantesRepository
import dev.joseluisgs.validator.Validator
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging

@Singleton
class EstudiantesServiceImpl(
    private val repository: EstudiantesRepository,
    private val cache: Cache<Int, Estudiante>,
    private val validator: Validator<Estudiante, EstudiantesError>,
) : EstudiantesService {
    private val logger = logging()
    
    override fun findAll(): List<Estudiante> {
        logger.debug { "Obteniendo todos los estudiantes" }
        return repository.findAll()
    }
    
    override fun findById(id: Int): Result<Estudiante, EstudiantesError> {
        logger.debug { "Obteniendo estudiante con id $id" }
        return cache.getIfPresent(id)?.let {
            Ok(it)
        } ?: repository.findById(id)?.let {
            cache.put(id, it) // Guardamos en cache
            Ok(it)
        } ?: Err(EstudiantesError.NotFoundError(id))
    }
    
    
    override fun save(estudiante: Estudiante): Result<Estudiante, EstudiantesError> {
        logger.debug { "Guardando estudiante $estudiante" }
        return validator.validate(estudiante).andThen { Ok(repository.save(it)) }
    }
    
    override fun update(id: Int, estudiante: Estudiante): Result<Estudiante, EstudiantesError> {
        logger.debug { "Actualizando estudiante con id $id" }
        return validator.validate(estudiante).andThen { es ->
            repository.update(id, es)?.let {
                cache.invalidate(id) // Borramos de cache, ya que se ha actualizado
                Ok(es)
            } ?: Err(EstudiantesError.NotFoundError(id))
        }
    }
    
    override fun delete(id: Int): Result<Estudiante, EstudiantesError> {
        logger.debug { "Borrando estudiante con id $id" }
        
        return repository.delete(id)?.let {
            cache.invalidate(id) // Borramos de cache
            Ok(it)
        } ?: Err(EstudiantesError.NotFoundError(id))
        
    }
    
}