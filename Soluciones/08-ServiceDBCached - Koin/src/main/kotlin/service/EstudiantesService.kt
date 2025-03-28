package dev.joseluisgs.service

import com.github.michaelbull.result.Result
import dev.joseluisgs.error.EstudiantesError
import dev.joseluisgs.models.Estudiante

interface EstudiantesService {
    fun findAll(): List<Estudiante>
    fun findById(id: Int): Result<Estudiante, EstudiantesError>
    fun save(estudiante: Estudiante): Result<Estudiante, EstudiantesError>
    fun update(id: Int, estudiante: Estudiante): Result<Estudiante, EstudiantesError>
    fun delete(id: Int): Result<Estudiante, EstudiantesError>
    
    // el resto de consultas hazlas tú
}