package dev.joseluisgs.service

import dev.joseluisgs.models.Estudiante

interface EstudiantesService {
    fun findAll(): List<Estudiante>
    fun findById(id: Int): Estudiante
    fun save(estudiante: Estudiante): Estudiante
    fun update(id: Int, estudiante: Estudiante): Estudiante
    fun delete(id: Int): Estudiante
    
    // el resto de consultas hazlas tú
}