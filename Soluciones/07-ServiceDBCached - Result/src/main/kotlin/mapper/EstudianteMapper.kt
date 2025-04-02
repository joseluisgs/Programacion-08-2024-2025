package dev.joseluisgs.mappers

import dev.joseluisgs.dao.EstudianteEntity
import dev.joseluisgs.models.Estudiante


/**
 * Mapeo de la base de datos a la entidad
 * Por favor consulta bien los tipos de datos de la base de datos
 * @return Estudiante
 * @see Estudiante
 *
 */


fun EstudianteEntity.toModel(): Estudiante = Estudiante(
    id = this.id,
    nombre = this.nombre,
    fechaNacimiento = this.fechaNacimiento,
    calificacion = this.calificacion,
    repetidor = this.repetidor,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun Estudiante.toEntity(): EstudianteEntity = EstudianteEntity(
    id = this.id,
    nombre = this.nombre,
    fechaNacimiento = this.fechaNacimiento,
    calificacion = this.calificacion,
    repetidor = this.repetidor,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)