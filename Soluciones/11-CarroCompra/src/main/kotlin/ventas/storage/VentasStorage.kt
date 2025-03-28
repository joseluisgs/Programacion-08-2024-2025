package dev.joseluisgs.ventas.storage

import com.github.michaelbull.result.Result
import dev.joseluisgs.ventas.error.VentaError
import dev.joseluisgs.ventas.models.Venta

interface VentasStorage {
    fun export(venta: Venta, file: String): Result<Unit, VentaError>
}