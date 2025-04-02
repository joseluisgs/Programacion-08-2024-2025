package dev.joseluisgs.clientes.error


sealed class ClienteError(val message: String) {
    class ClienteNoEncontrado(id: Long) : ClienteError("Cliente con id $id no encontrado")
    class ClienteNoValido(message: String) : ClienteError("Cliente no válido. $message")
    class ClienteOtherError(message: String) : ClienteError(message)
}
