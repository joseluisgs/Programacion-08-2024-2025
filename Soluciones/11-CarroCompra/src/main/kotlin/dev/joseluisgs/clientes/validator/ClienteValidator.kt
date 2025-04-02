package dev.joseluisgs.clientes.validator

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.joseluisgs.clientes.error.ClienteError
import dev.joseluisgs.clientes.model.Cliente
import dev.joseluisgs.common.validator.Validator
import org.koin.core.annotation.Singleton

@Singleton
class ClienteValidator : Validator<Cliente, ClienteError> {
    override fun validate(t: Cliente): Result<Cliente, ClienteError> {
        return when {
            t.nombre.isBlank() -> Err(ClienteError.ClienteNoValido("Nombre no puede estar vacío"))
            t.email.isBlank() -> Err(ClienteError.ClienteNoValido("Email no puede estar vacío"))
            // Expresión regular para validar un email
            t.email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$")) -> Err(ClienteError.ClienteNoValido("Email no es válido"))
            else -> Ok(t)
        }
    }
}