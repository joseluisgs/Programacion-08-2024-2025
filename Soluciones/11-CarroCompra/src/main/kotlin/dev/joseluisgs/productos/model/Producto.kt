package dev.joseluisgs.productos.model


import dev.joseluisgs.common.locale.LocaleFormatter.toLocalMoney
import java.time.LocalDateTime

data class Producto(
    val id: Long = NEW_ID,
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val categoria: Categoria,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val deleted: Boolean = false
) {
    companion object {
        const val NEW_ID = -1L
    }
    
    // Método específico para formateo con locale
    fun toStringLocaleFormatted(): String {
        return "Producto(id=$id, nombre='$nombre', precio=${precio.toLocalMoney()}, stock=$stock, categoria=$categoria, createdAt=$createdAt, updatedAt=$updatedAt, deleted=$deleted)"
    }
}

enum class Categoria(val categoria: String) {
    ELECTRONICA("ELECTRONICA"),
    DEPORTE("DEPORTE"),
    MODA("MODA"),
    OTROS("OTROS")
}