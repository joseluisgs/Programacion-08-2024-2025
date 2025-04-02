package dev.joseluisgs.productos.error


sealed class ProductoError(val message: String) {
    class ProductoNoEncontrado(id: Long) : ProductoError("Producto con id $id no encontrado")
    class ProductoNoValido(message: String) : ProductoError("Producto no válido. $message")
    
    //class ProductoNoActualizado(message: String) : ProductoError("Producto no actualizado. $message")
    //class ProductoNoEliminado(message: String) : ProductoError("Producto no eliminado. $message")
    class ProductoStorageError(message: String) : ProductoError("Error en almacenamiento de productos. $message")
    class ProductoOtherError(message: String) : ProductoError(message)
}
