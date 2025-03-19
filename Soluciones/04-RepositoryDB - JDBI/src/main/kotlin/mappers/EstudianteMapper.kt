package dev.joseluisgs.mappers

import dev.joseluisgs.dao.EstudianteEntity
import dev.joseluisgs.models.Estudiante
import java.time.LocalDate
import java.time.LocalDateTime


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
    fechaNacimiento = LocalDate.parse(this.fechaNacimiento),
    calificacion = this.calificacion,
    repetidor = this.repetidor == 1,
    createdAt = LocalDateTime.parse(this.createdAt),
    updatedAt = LocalDateTime.parse(this.updatedAt)
)

fun Estudiante.toEntity(): EstudianteEntity = EstudianteEntity(
    id = this.id,
    nombre = this.nombre,
    fechaNacimiento = this.fechaNacimiento.toString(),
    calificacion = this.calificacion,
    repetidor = if (this.repetidor) 1 else 0,
    createdAt = this.createdAt.toString(),
    updatedAt = this.updatedAt.toString()
)