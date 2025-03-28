package dev.joseluisgs.repositories.estudiantes

import dev.joseluisgs.models.Estudiante
import dev.joseluisgs.repositories.base.CrudRepository

interface EstudiantesRepository : CrudRepository<Estudiante, Long> {
    fun findAllPaginated(page: Int = 1, size: Int = 10): List<Estudiante>
    fun findByNombre(nombre: String): List<Estudiante>
    fun findByCalificacion(calificacion: Double): List<Estudiante>
    fun findByRepetidor(repetidor: Boolean): List<Estudiante>
    fun mediaCalificaciones(): Double
    fun maximaCalificacion(): Double
    fun minimaCalificacion(): Double
    fun numAprobados(): Int
    fun numSuspensos(): Int
}