package cafeteras

import java.util.*

data class Cafetera(
    private val calentador: Calentador, // Eso es una propiedad que se inicializa en el momento de su uso
    private val bomba: Bomba
) {
    private val id = UUID.randomUUID()

    fun servir() {
        calentador.encender()
        bomba.bombear()
        println("[_]P !Taza de Café! [_]P ")
        calentador.apagar()
    }

    override fun toString(): String {
        return "Cafetera(id=$id, calentador=$calentador, bomba=$bomba)"
    }
}