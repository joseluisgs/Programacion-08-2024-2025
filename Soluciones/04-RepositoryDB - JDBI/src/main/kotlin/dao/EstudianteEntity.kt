package dev.joseluisgs.dao


data class EstudianteEntity(
    val id: Int,
    val nombre: String,
    val fechaNacimiento: String,
    val calificacion: Double,
    val repetidor: Int,
    val createdAt: String,
    val updatedAt: String
)