package dev.joseluisgs.exception

/**
 * Excepciones de Estudiantes
 * @param message Mensaje de error
 */
sealed class EstudiantesException(message: String) : Exception(message) {
    class NotFoundException(id: Int) : EstudiantesException("Estudiante no encontrado con id: $id")
    class ValidationException(message: String) : EstudiantesException("Estudiante no válido: $message")
}