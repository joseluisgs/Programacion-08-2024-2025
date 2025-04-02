package dev.joseluisgs.clientes.mapper

import dev.joseluisgs.clientes.dao.ClienteEntity
import dev.joseluisgs.clientes.dto.ClienteDto
import dev.joseluisgs.clientes.model.Cliente
import java.time.LocalDateTime

fun ClienteEntity.toModel(): Cliente {
    return Cliente(
        id = this.id,
        nombre = this.nombre,
        email = this.email,
        direccion = this.direccion,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deleted = this.deleted
    )
}

fun Cliente.toEntity(): ClienteEntity {
    return ClienteEntity(
        id = this.id,
        nombre = this.nombre,
        email = this.email,
        direccion = this.direccion,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deleted = this.deleted
    )
}

fun Cliente.toDto(): ClienteDto {
    return ClienteDto(
        id = this.id,
        nombre = this.nombre,
        email = this.email,
        direccion = this.direccion,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
        deleted = this.deleted
    )
}

fun ClienteDto.toModel(): Cliente {
    return Cliente(
        id = this.id,
        nombre = this.nombre,
        email = this.email,
        direccion = this.direccion,
        createdAt = LocalDateTime.parse(this.createdAt),
        updatedAt = LocalDateTime.parse(this.updatedAt),
        deleted = this.deleted
    )
}