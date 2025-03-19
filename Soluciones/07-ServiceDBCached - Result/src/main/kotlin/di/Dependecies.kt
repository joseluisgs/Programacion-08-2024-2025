package dev.joseluisgs.di

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import dev.joseluisgs.dao.EstudiantesDao
import dev.joseluisgs.database.JdbiManager
import dev.joseluisgs.error.EstudiantesError
import dev.joseluisgs.models.Estudiante
import dev.joseluisgs.repositories.EstudiantesRepository
import dev.joseluisgs.repositories.EstudiantesRepositoryImpl
import dev.joseluisgs.service.EstudiantesService
import dev.joseluisgs.service.EstudiantesServiceImpl
import dev.joseluisgs.validator.EstudianteValidator
import dev.joseluisgs.validator.Validator
import org.jdbi.v3.core.Jdbi
import org.lighthousegames.logging.logging
import java.util.concurrent.TimeUnit


/**
 * Gestor de dependencias
 * Esta clase centraliza la creación de objetos y provee acceso a sus dependencias.
 */
object Dependecies {
    
    private val logger = logging()
    
    init {
        logger.debug { "Inicializando gestor de dependencias" }
    }
    
    
    /**
     * Función para proporcionar el gestor de base de datos
     */
    fun provideDatabaseManager(): Jdbi {
        logger.debug { "Proporcionando JDBI" }
        return JdbiManager.instance
    }
    
    /**
     * Función para proporcionar el DAO de Estudiantes
     * @param jdbi Instancia de JDBI
     */
    fun provideEstudiantesDao(jdbi: Jdbi): EstudiantesDao {
        logger.debug { "Proporcionando DAO de Estudiantes" }
        return jdbi.onDemand(EstudiantesDao::class.java)
    }
    
    /**
     * Función para proporcionar la caché
     * @param capacity Capacidad de la caché
     * @param duration Duración de la caché
     */
    fun provideEstudiantesCache(capacity: Long, duration: Int): Cache<Int, Estudiante> {
        logger.debug { "Proporcionando caché de Estudiantes: Capacidad: $capacity, Duración: $duration" }
        // Configura la caché con un tamaño máximo de 5 y expiración de 1 minuto
        return Caffeine.newBuilder()
            .maximumSize(capacity) // LRU con máximo de 5 elementos
            .expireAfterWrite(1, TimeUnit.MINUTES) // Expira 1 minuto después de la escritura
            .build<Int, Estudiante>()
    }
    
    /**
     * Función para proporcionar el repositorio de Estudiantes
     * @param dao DAO de Estudiantes
     */
    fun provideEstudiantesRepository(dao: EstudiantesDao): EstudiantesRepository {
        logger.debug { "Proporcionando Repositorio de Estudiantes" }
        return EstudiantesRepositoryImpl(dao)
    }
    
    /**
     * Función para proporcionar el validador de Estudiantes
     */
    fun provideValidator(): Validator<Estudiante, EstudiantesError> {
        logger.debug { "Proporcionando Validador de Estudiantes" }
        return EstudianteValidator()
    }
    
    
    fun provideEstudiantesService(
        repository: EstudiantesRepository,
        cache: Cache<Int, Estudiante>,
        validator: Validator<Estudiante, EstudiantesError>
    ): EstudiantesService {
        logger.debug { "Proporcionando Servicio de Estudiantes" }
        return EstudiantesServiceImpl(
            repository = repository,
            cache = cache,
            validator = validator
        )
    }
    
    fun getEstudiantesService(): EstudiantesService {
        return provideEstudiantesService(
            repository = provideEstudiantesRepository(provideEstudiantesDao(provideDatabaseManager())),
            cache = provideEstudiantesCache(5, 1),
            validator = provideValidator()
        )
    }
}
