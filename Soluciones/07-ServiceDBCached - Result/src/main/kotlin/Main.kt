package dev.joseluisgs

import com.github.michaelbull.result.*
import dev.joseluisgs.di.Dependecies
import dev.joseluisgs.error.EstudiantesError
import dev.joseluisgs.models.Estudiante
import dev.joseluisgs.service.EstudiantesService
import java.time.LocalDate

fun main() {
    
    // Inicializamos la base de datos
    val service: EstudiantesService = Dependecies.getEstudiantesService()
    
    // Obtenemos todos los estudiantes
    val res = service.findAll()
    res.forEach { println(it) }
    
    val estudianteFound = res.first()
    
    // Obtenemos un estudiante, para ello cogemos la rama de éxito
    println(service.findById(estudianteFound.id).value) // Obtenemos el estudiante de la caché
    
    println(service.findById(estudianteFound.id).get()) // Obtenemos el estudiante de la caché
    
    service.findById(estudianteFound.id).onSuccess {
        println(it)
    }.onFailure {
        println("ERROR: ${it.message}")
    }
    
    // Obtenemos un estudiante inexistente
    
    // podemos mezclar acciones de éxito y error
    service.findById(10).mapBoth(
        success = { println(it) },
        failure = { println("ERROR: ${it.message}") }
    )
    
    // Otra forma
    service.findById(10).fold(
        { println(it) },
        { println("ERROR: ${it.message}") }
    )
    
    // O solo error
    service.findById(10).mapError { println("ERROR: ${it.message}") }
    service.findById(10).onFailure { println("ERROR: ${it.message}") }
    
    
    // Guardamos un estudiante
    val estudiante =
        Estudiante(nombre = "Juan", fechaNacimiento = LocalDate.of(1990, 1, 1), calificacion = 5.5, repetidor = true)
    service.save(estudiante).mapBoth(
        success = { println(it) },
        failure = { println("ERROR: ${it.message}") }
    )
    
    // Guardamos un estudiante con error de validación
    
    val estudianteError =
        Estudiante(
            nombre = "Juan",
            fechaNacimiento = LocalDate.of(1990, 1, 1),
            calificacion = 15.5,
            repetidor = true
        )
    
    service.save(estudianteError).mapBoth(
        success = { println(it) },
        // failure = { println("ERROR: ${it.message}") }
        failure = {
            when (it) {
                is EstudiantesError.ValidationError -> println("ERROR VALIDACIÓN: ${it.message}")
                else -> println("ERROR: INESPERADO, joder!!! es el fin del mundo!!! ${it.message}")
            }
        }
    )
    
    // Actualizamos un estudiante
    val estudianteUpdate =
        Estudiante(
            nombre = "Actualizado",
            fechaNacimiento = LocalDate.of(1990, 1, 1),
            calificacion = 6.5,
            repetidor = true
        )
    
    service.update(estudianteFound.id, estudianteUpdate).mapBoth(
        success = { println(it) },
        failure = { println("ERROR: ${it.message}") }
    )
    
    
    // Borramos un estudiante
    service.delete(estudianteFound.id).mapBoth(
        success = { println(it) },
        failure = { println("ERROR: ${it.message}") }
    )
    
    // Obtenemos todos los estudiantes
    service.findAll().forEach { println(it) }
    
    
}
