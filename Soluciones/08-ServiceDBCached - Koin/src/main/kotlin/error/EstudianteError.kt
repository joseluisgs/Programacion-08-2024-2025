package dev.joseluisgs.error

sealed class EstudiantesError(val message: String) {
    class NotFoundError(id: Int) : EstudiantesError("Estudiante no encontrado con id: $id")
    class ValidationError(message: String) : EstudiantesError("Estudiante no válido: $message")
}