package dev.joseluisgs

import com.github.michaelbull.result.fold
import com.github.michaelbull.result.get
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.mapError
import dev.joseluisgs.models.Estudiante
import dev.joseluisgs.service.EstudiantesService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.fileProperties
import org.koin.ksp.generated.defaultModule
import java.time.LocalDate

fun main() {
    
    startKoin {
        // declare used logger
        printLogger()
        // Leemos las propiedades de un fichero
        fileProperties("/config.properties") // Por defecto busca en src/main/resources/config.properties, pero puede ser otro fichero si se lo pasas como parametro
        // declara modulos de inyección de dependencias, pero lo verificamos antes de inyectarlos
        modules(defaultModule)
    }
    
    // Creamos una instancia de nuestra aplicación y la ejecutamos
    val app = EstudiantesApp()
    app.run()
    
}

class EstudiantesApp : KoinComponent {
    
    fun run() {
        // Instanciamos el servicio de estudiantes de manera inyectada
        val service: EstudiantesService by inject()
        
        // Obtenemos todos los estudiantes
        val res = service.findAll()
        res.forEach { println(it) }
        
        val estudianteFound = res.first()
        
        // Obtenemos un estudiante, para ello cogemos la rama de éxito
        println(service.findById(estudianteFound.id).get()) // Obtenemos el estudiante de la caché
        
        println(service.findById(estudianteFound.id).get()) // Obtenemos el estudiante de la caché
        
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
        
        
        // Guardamos un estudiante
        val estudiante =
            Estudiante(
                nombre = "Juan",
                fechaNacimiento = LocalDate.of(1990, 1, 1),
                calificacion = 5.5,
                repetidor = true
            )
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
            failure = { println("ERROR: ${it.message}") }
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
}
