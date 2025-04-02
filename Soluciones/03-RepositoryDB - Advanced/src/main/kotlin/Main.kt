package dev.joseluisgs

import dev.joseluisgs.models.Estudiante
import dev.joseluisgs.repositories.EstudiantesRepositoryImpl
import java.time.LocalDate

fun main() {
    
    val repository = EstudiantesRepositoryImpl()
    
    repository.findAll().forEach {
        println(it)
    }
    
    val estudiante = Estudiante(
        nombre = "Pepe",
        fechaNacimiento = LocalDate.parse("1990-01-01"),
        calificacion = 10.0,
        repetidor = false
    )
    val res = repository.save(estudiante)
    println(res)
    repository.findAll().forEach {
        println(it)
    }
    
    val estudiante2 = repository.findById(1)
    estudiante2?.let {
        println(it)
    } ?: println("No encontrado")
    
    val estudiante3 = repository.findById(2)
    estudiante3?.let {
        println(it)
    } ?: println("No encontrado")
    
    val estudiante5 = repository.findById(99)
    estudiante5?.let {
        println(it)
    } ?: println("No encontrado")
    
    val estudiante4 = estudiante.copy(id = 99, nombre = "Demo_Update", calificacion = 5.0)
    val res2 = repository.update(1, estudiante4)
    res2?.let {
        println(it)
    } ?: println("No encontrado")
    
    val res3 = repository.delete(2)
    res3?.let {
        println(it)
    } ?: println("No encontrado")
    
    repository.findAll().forEach {
        println(it)
    }
    
    val e2 = Estudiante(
        nombre = "Eva",
        fechaNacimiento = LocalDate.parse("1990-01-01"),
        calificacion = 10.0,
        repetidor = false
    )
    val r2 = repository.save(e2)
    println(res)
    
    val e3 = Estudiante(
        nombre = "Pepe",
        fechaNacimiento = LocalDate.parse("1990-01-01"),
        calificacion = 4.0,
        repetidor = true
    )
    val r3 = repository.save(e3)
    
    repository.findAll().forEach {
        println(it)
    }
    
    repository.findByNombre("Pepe").forEach {
        println(it)
    }
    
    repository.findByCalificacion(10.0).forEach {
        println(it)
    }
    
    repository.findByRepetidor(false).forEach {
        println(it)
    }
    
    println(repository.mediaCalificaciones())
    
    println(repository.maximaCalificacion())
    
    println(repository.minimaCalificacion())
    
    println(repository.numAprobados())
    
    println(repository.numSuspensos())
    
    repository.findAll().forEach {
        println(it)
    }
    
    repository.findAllPaginated(1, 2).forEach {
        println(it)
    }
    
    repository.findAllPaginated(size = 100).forEach {
        println(it)
    }
}
