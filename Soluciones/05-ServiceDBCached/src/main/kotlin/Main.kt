package dev.joseluisgs

import dev.joseluisgs.cache.Cache
import dev.joseluisgs.cache.CacheImpl
import dev.joseluisgs.models.Estudiante
import dev.joseluisgs.repositories.EstudiantesRepository
import dev.joseluisgs.repositories.EstudiantesRepositoryImpl
import dev.joseluisgs.service.EstudiantesService
import dev.joseluisgs.service.EstudiantesServiceImpl
import dev.joseluisgs.validator.EstudianteValidator
import dev.joseluisgs.validator.Validator
import java.time.LocalDate

fun main() {
    val cache: Cache<Int, Estudiante> = CacheImpl(5, CacheImpl.CacheType.LRU)
    val validator: Validator<Estudiante> = EstudianteValidator()
    val repository: EstudiantesRepository = EstudiantesRepositoryImpl()
    
    val service: EstudiantesService = EstudiantesServiceImpl(
        repository = repository,
        cache = cache,
        validator = validator
    )
    
    // Obtenemos todos los estudiantes
    service.findAll().forEach { println(it) }
    
    // Obtenemos un estudiante
    println(service.findById(1))
    
    println(service.findById(1)) // Obtenemos el estudiante de la caché
    
    // Obtenemos un estudiante inexistente
    try {
        println(service.findById(10))
    } catch (e: Exception) {
        println("ERROR: ${e.message}")
    }
    
    // Guardamos un estudiante
    val estudiante =
        Estudiante(nombre = "Juan", fechaNacimiento = LocalDate.of(1990, 1, 1), calificacion = 5.5, repetidor = true)
    println(service.save(estudiante))
    
    // Guardamos un estudiante con error de validación
    
    try {
        val estudianteError =
            Estudiante(
                nombre = "Juan",
                fechaNacimiento = LocalDate.of(1990, 1, 1),
                calificacion = 15.5,
                repetidor = true
            )
        println(service.save(estudianteError))
    } catch (e: Exception) {
        println("ERROR: ${e.message}")
    }
    
    // Actualizamos un estudiante
    val estudianteUpdate =
        Estudiante(
            nombre = "Actualizado",
            fechaNacimiento = LocalDate.of(1990, 1, 1),
            calificacion = 6.5,
            repetidor = true
        )
    
    println(service.update(1, estudianteUpdate))
    
    // Borramos un estudiante
    println(service.delete(1))
    
    // Obtenemos todos los estudiantes
    service.findAll().forEach { println(it) }
    
    
}
