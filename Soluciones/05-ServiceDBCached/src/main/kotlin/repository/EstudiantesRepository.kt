package dev.joseluisgs.repositories

import dev.joseluisgs.models.Estudiante

interface EstudiantesRepository : CrudRepository<Estudiante, Int> {
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