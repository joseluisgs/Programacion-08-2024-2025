package dev.joseluisgs

import dev.joseluisgs.di.Dependecies
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
    
    // Obtenemos un estudiante
    println(service.findById(estudianteFound.id)) // Obtenemos el estudiante de la caché
    
    println(service.findById(estudianteFound.id)) // Obtenemos el estudiante de la caché
    
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
    
    println(service.update(estudianteFound.id, estudianteUpdate))
    
    // Borramos un estudiante
    println(service.delete(estudianteFound.id))
    
    // Obtenemos todos los estudiantes
    service.findAll().forEach { println(it) }
    
    
}
