package dev.joseluisgs.productos.mapper

import dev.joseluisgs.common.locale.LocaleFormatter.roundTo
import dev.joseluisgs.productos.dao.ProductoEntity
import dev.joseluisgs.productos.dto.ProductoDto
import dev.joseluisgs.productos.model.Categoria
import dev.joseluisgs.productos.model.Producto
import java.time.LocalDateTime

fun ProductoEntity.toModel(): Producto {
    return Producto(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio,
        stock = this.stock,
        categoria = Categoria.valueOf(this.categoria),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deleted = this.deleted
    )
}

fun ProductoDto.toModel(): Producto {
    return Producto(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio.roundTo(2),
        stock = this.stock,
        categoria = Categoria.valueOf(this.categoria),
        createdAt = LocalDateTime.parse(this.createdAt),
        updatedAt = LocalDateTime.parse(this.updatedAt),
        deleted = this.deleted
    )
}

fun Producto.toDto(): ProductoDto {
    return ProductoDto(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio.roundTo(2),
        stock = this.stock,
        categoria = this.categoria.name,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
        deleted = this.deleted
    )
}

fun Producto.toEntity(): ProductoEntity {
    return ProductoEntity(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio,
        stock = this.stock,
        categoria = this.categoria.name,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deleted = this.deleted
    )
}