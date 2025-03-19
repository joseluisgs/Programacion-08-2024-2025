package dev.joseluisgs.productos.validator

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.joseluisgs.common.validator.Validator
import dev.joseluisgs.productos.error.ProductoError
import dev.joseluisgs.productos.model.Producto
import org.koin.core.annotation.Singleton

@Singleton
class ProductoValidator : Validator<Producto, ProductoError> {
    override fun validate(t: Producto): Result<Producto, ProductoError> {
        return when {
            t.nombre.isBlank() -> Err(ProductoError.ProductoNoValido("Nombre no puede estar vacío"))
            t.precio < 0 -> Err(ProductoError.ProductoNoValido("Precio no puede ser menor a 0"))
            t.stock < 0 -> Err(ProductoError.ProductoNoValido("Stock no puede ser menor a 0"))
            else -> Ok(t)
        }
    }
}