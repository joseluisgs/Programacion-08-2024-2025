package dev.joseluisgs.ventas.mapper

import dev.joseluisgs.clientes.mapper.toDto
import dev.joseluisgs.clientes.model.Cliente
import dev.joseluisgs.productos.mapper.toDto
import dev.joseluisgs.productos.model.Producto
import dev.joseluisgs.ventas.dao.LineaVentaEntity
import dev.joseluisgs.ventas.dao.VentaEntity
import dev.joseluisgs.ventas.dto.LineaVentaDto
import dev.joseluisgs.ventas.dto.VentaDto
import dev.joseluisgs.ventas.models.LineaVenta
import dev.joseluisgs.ventas.models.Venta
import java.util.*

fun LineaVentaEntity.toModel(producto: Producto): LineaVenta {
    return LineaVenta(
        id = this.id,
        producto = producto,
        cantidad = this.cantidad,
        precio = this.precio,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )
}

fun VentaEntity.toModel(cliente: Cliente, lineas: List<LineaVenta>): Venta {
    return Venta(
        id = id,
        cliente = cliente,
        lineas = lineas,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deleted = deleted
    )
}

fun LineaVenta.toEntity(idVenta: UUID): LineaVentaEntity {
    return LineaVentaEntity(
        id = id,
        ventaId = idVenta,
        productoId = producto.id,
        cantidad = cantidad,
        precio = producto.precio,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

fun Venta.toEntity(): VentaEntity {
    return VentaEntity(
        id = id,
        clienteId = cliente.id,
        total = lineas.sumOf { it.precio * it.cantidad },
        createdAt = createdAt,
        updatedAt = updatedAt,
        deleted = deleted
    )
}

fun LineaVenta.toDto(): LineaVentaDto {
    return LineaVentaDto(
        id = this.id.toString(),
        producto = this.producto.toDto(),
        cantidad = this.cantidad,
        precio = this.precio,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
    )
}

fun Venta.toDto(): VentaDto {
    return VentaDto(
        id = this.id.toString(),
        cliente = this.cliente.toDto(),
        lineas = this.lineas.map { it.toDto() },
        total = this.lineas.sumOf { it.precio },
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
    )
}